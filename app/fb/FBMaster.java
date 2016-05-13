package fb;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.restfb.Connection;
import com.restfb.types.Page;
import com.restfb.types.Post;

import play.db.jpa.JPA;

public class FBMaster {
	
	public static void fetchFeeds(){
		
		String query = "from FBPage p";
		
		List<FBPage> fbPages = JPA.em().createQuery(query, FBPage.class).getResultList();
		System.out.println("number of FBPages : " + fbPages.size());
		for(FBPage fbPage : fbPages){
			
			FeedFetch feedFetch = FBdao.getFeedFetch(fbPage);
			if(feedFetch == null){
				System.out.println("was null");
				feedFetch = new FeedFetch();
				feedFetch.setFbPage(fbPage);
				JPA.em().persist(feedFetch);
				JPA.em().getTransaction().commit();
				JPA.em().getTransaction().begin();
			}
			try {
				processFeedFetch(feedFetch);
				
				
				feedFetch.setErrorMessage(null);
			}
			catch(Exception e) {
				feedFetch.setErrorMessage(e.getClass().getSimpleName() + " exception while processing feed fetch : " + e.getMessage());
				FB.getInstance().holdUp();
				e.printStackTrace();
			}
			
			
		}
	}
	
	public static void processFeedFetch(FeedFetch feedFetch) {
		FBPage fbPage = feedFetch.getFbPage();
		Connection<Post> connection = null;
		System.out.println("feedFetch null? : " + feedFetch);
		
		if(feedFetch.getNextPageUrl() == null && !feedFetch.getReachedEnd()){
			System.out.println("getting first connection");
			connection = FB.getInstance().getFeedStart(fbPage.getFbId());
		}else if(feedFetch.getNextPageUrl() != null && !feedFetch.getReachedEnd()){
			System.out.println("getting next connection");
			connection = FB.getInstance().continueFeed(feedFetch.getNextPageUrl());
			
		} else if ( feedFetch.getReachedEnd() == true){
			System.out.println("This feed fetch has already reached the end");
			return;
		} else {
			System.out.println("here for some reason?");
			return;
		}
		
		do{
			
			List<Post> posts = connection.getData();
			List<FBPost> fbPosts = FBParser.parsePosts(posts);
			if(fbPosts.size() > 0){
				feedFetch.setEarliestPost(fbPosts.get(fbPosts.size() - 1).getCreatedTime());
				System.out.println("earliest : " + feedFetch.getEarliestPost());
			}
			for(FBPost fbPost : fbPosts) {
				fbPost.setFbPage(fbPage);
				JPA.em().persist(fbPost);
			}
			
			String nextPageUrl = connection.getNextPageUrl();
			if(nextPageUrl == null){
				feedFetch.setReachedEnd(true);
			}
			else {
				feedFetch.addNextPageUrl(nextPageUrl);
				System.out.println("nextpageurl : " + nextPageUrl);
			}
			
			JPA.em().getTransaction().commit();
			JPA.em().getTransaction().begin();
			
			if(nextPageUrl == null || "".equals(nextPageUrl)) {
				connection = null;
			} else {
				connection = FB.getInstance().continueFeed(nextPageUrl);
			} 
			
		}while(connection != null);
			
			
		
	
	}
	
	public static void processPost(FeedFetch feedFetch, FBPost fbPost){
		
	}

	
	public static void readAndFetchPages() throws IOException { 
		
		Reader in = new FileReader("./data/in/companies.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
		
		EntityManager em = JPA.em();
		int total = 0;
		for(CSVRecord record : records) {
			String type = record.get("Site (FB/Tw/LI)");
			if(type.toLowerCase().equals("fb")){
				importFBPage(record);
//				if(total > 0){
//					break;
//				}
			} 
			total++;
			
			System.out.println("Processed : " + total);
		}
		
		in.close();
	}
	public static void importFBPage(CSVRecord record){
		String givenUrl = record.get("URL");
		String companyString = record.get("Company");
		Boolean job = Boolean.parseBoolean(record.get("Job/Career (Yes/No)"));
		Boolean liTab = Boolean.parseBoolean(record.get("LI Tab?"));
		String geoSpecial = record.get("Geography specific");
		
		if(FBdao.alreadyPresent(givenUrl)){
			return;
		}
		
		String identifier = FBParser.getIdentifier(givenUrl);
		System.out.println("identifier : " + identifier);
		System.out.println("givenUrl : "  + givenUrl);
		Page page = FB.getInstance().fetchPage(identifier);
		FBPage fbPage = FBParser.parsePage(page);
		System.out.println("after parse : " + fbPage.getFbPageId());
		fbPage.setGivenUrl(givenUrl);
		fbPage.setCompanyString(companyString);
		fbPage.setJob(job);
		fbPage.setLiTab(liTab);
		fbPage.setGeoSpecial(geoSpecial);
		
		FBdao.persistPage(fbPage);
	}
	
	
}
