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
import com.restfb.DefaultJsonMapper;
import com.restfb.JsonMapper;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Page;
import com.restfb.types.Photo;
import com.restfb.types.Post;

import play.db.jpa.JPA;

public class FBMaster {
	
	public static void getPhotoLikes() {
		String query = "from FBPhoto p where p.likesCount = 20";
		List<FBPhoto> fbPhotos = JPA.em().createQuery(query, FBPhoto.class).getResultList();
		
		for(FBPhoto fbPhoto : fbPhotos) {
			int commentsCount = FB.getInstance().getPhotoComments(fbPhoto.getId());
			int likesCount = FB.getInstance().getPhotoLikes(fbPhoto.getId());
			int reactionsCount = FB.getInstance().getReactions(fbPhoto.getId()); 
			System.out.println("likesCount  : " + likesCount);
			System.out.println("commentsCount : " + commentsCount);
			System.out.println("reactionsCount : " + reactionsCount);
			fbPhoto.setLikesCount(likesCount);
			fbPhoto.setCommentsCount(commentsCount);
			fbPhoto.setReactionsCount(reactionsCount);
		}
	}
	
	public static void fetchFeeds(FeedType feedType){
		
		String query = "from FBPage p";
		
		List<FBPage> fbPages = JPA.em().createQuery(query, FBPage.class).getResultList();
		System.out.println("number of FBPages : " + fbPages.size());
		for(FBPage fbPage : fbPages){
			
			FeedFetch feedFetch = FBdao.getFeedFetch(fbPage, feedType);
			if(feedFetch == null){
				System.out.println("was null");
				feedFetch = new FeedFetch();
				feedFetch.setFbPage(fbPage);
				feedFetch.setFeedType(feedType);
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
	
	public static void processPhotoFetch(FeedFetch feedFetch) {
		FBPage fbPage = feedFetch.getFbPage();
		System.out.println("processing photo fetch : " + fbPage.getCompanyString());
		
		while(!feedFetch.getReachedEnd()){
			JsonObject rawConnection = FB.getInstance().getGenericConnection(fbPage.getFbId(), feedFetch.getNextPageUrl());
			JsonArray dataArray = rawConnection.getJsonArray("data");
			if(dataArray.length() > 0){
				System.out.println("data array: "  + dataArray.length());
				List<FBPhoto> fbPhotos = FBParser.parseRawPhotos(dataArray);
				System.out.println("fbPhotos : " + fbPhotos.size());
				for(FBPhoto fbPhoto : fbPhotos) {
					fbPhoto.setFbPage(fbPage);
					JPA.em().persist(fbPhoto);
				}
				
				JPA.em().getTransaction().commit();
				JPA.em().getTransaction().begin();
				
				
				feedFetch.setEarliestPost(fbPhotos.get(fbPhotos.size() - 1).getCreatedTime() + "");
				feedFetch.addNextPageUrl(rawConnection.getJsonObject("paging").getJsonObject("cursors").getString("after"));
				System.out.println("earliest : "+ feedFetch.getEarliestPost());
			} else {
				feedFetch.setReachedEnd(true);
			}
		}
	}
	
	public static void processFeedFetch(FeedFetch feedFetch) {
		if(feedFetch.getFeedType() == FeedType.PHOTOS){
			processPhotoFetch(feedFetch);
			return;
		}
		FBPage fbPage = feedFetch.getFbPage();
		Class<? extends NamedFacebookType> feedTypeClass = feedFetch.getFeedType().getFeedClass();
		Connection<? extends NamedFacebookType> connection = null;
		System.out.println("feedFetch null? : " + feedFetch);
		
		if ( feedFetch.getReachedEnd() == true){
			System.out.println("This feed fetch has already reached the end");
			return;
		} else if(feedFetch.getNextPageUrl() == null){
			System.out.println("getting first connection");
			connection = FB.getInstance().getFeedStart(fbPage.getFbId(), feedTypeClass);
			
		}else if(feedFetch.getNextPageUrl() != null){
			System.out.println("getting next connection");
			connection = FB.getInstance().continueFeed(feedFetch.getNextPageUrl(), feedTypeClass);
			
		} else{
			System.out.println("here for some reason?");
			return;
		}
		
		do{
			if(feedTypeClass == Post.class){
				List<Post> posts = (List<Post>) connection.getData();
				List<FBPost> fbPosts = FBParser.parsePosts(posts);
				if(fbPosts.size() > 0){
					feedFetch.setEarliestPost(fbPosts.get(fbPosts.size() - 1).getCreatedTime());
					System.out.println("earliest : " + feedFetch.getEarliestPost());
				}
				for(FBPost fbPost : fbPosts) {
					fbPost.setFbPage(fbPage);
					JPA.em().persist(fbPost);
				}
			} else if(feedTypeClass == Photo.class){
				List<Photo> photos = (List<Photo>) connection.getData();
				List<FBPhoto> fbPhotos = FBParser.parsePhotos(photos);
				if(fbPhotos.size() > 0) {
					feedFetch.setEarliestPost(fbPhotos.get(fbPhotos.size() - 1).getCreatedTime() + "");
					System.out.println("earliest : " + feedFetch.getEarliestPost());
				}
				for(FBPhoto fbPhoto : fbPhotos) {
					fbPhoto.setFbPage(fbPage);
					JPA.em().persist(fbPhoto);
				}
			} else {
				System.out.println("nothing to persist?");
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
				connection = FB.getInstance().continueFeed(nextPageUrl, feedTypeClass);
			} 
			
		}while(connection != null);
	}
	
	public static void fixPageDates() {
		String query = "from FBPage fb";
		List<FBPage> fbPages = JPA.em().createQuery(query, FBPage.class).getResultList();
		for(FBPage fbPage : fbPages){
			Page page = FB.getInstance().fetchPage(fbPage.getFbId());
			System.out.println("startdate : " + page.getStartInfo().getDate());
//			System.out.println("year : " + page.getStartInfo().getDate().getYear());
//			System.out.println("month : " + page.getStartInfo().getDate().getMonth());
//			System.out.println("day : " + page.getStartInfo().getDate().getDay());
			fbPage.setRealStartDate(FBParser.parseStartDate(page.getStartInfo().getDate()));
		}
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
