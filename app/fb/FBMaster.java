package fb;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
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

import fb.DatedFeedFetch.DateGranularity;
import persistence.FBDateMarker;
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
	
	public static void processManualFetch(FeedFetch feedFetch) {
		FBPage fbPage = feedFetch.getFbPage();
		System.out.println("processing photo fetch : " + fbPage.getCompanyString());
		
		while(!feedFetch.getReachedEnd()){
			
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
		
		Reader in = new FileReader("./data/in/companies2.csv");
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
		String companyId = record.get("Co ID");
		Boolean job = Boolean.parseBoolean(record.get("Job/Career (Yes/No)"));
//		Boolean liTab = Boolean.parseBoolean(record.get("LI Tab?"));
		Boolean fbTab = Boolean.parseBoolean(record.get("FB Tab"));
//		String geoSpecial = record.get("Geography specific");
		
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
		fbPage.setCompanyId(companyId);
		fbPage.setJob(job);
//		fbPage.setLiTab(liTab);
//		fbPage.setGeoSpecial(geoSpecial);
		fbPage.setFbTab(fbTab);
		
		FBdao.persistPage(fbPage);
	}
	
	public static void readAndFetchDateMarkers() throws IOException, ParseException { 
		
		Reader in = new FileReader("./data/in/fbid_accountage.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		
		EntityManager em = JPA.em();
		int total = 0;
		for(CSVRecord record : records) {
			String fbId = record.get(0);
			String timestamp = record.get(1);
			String creationDateString = record.get(2);
			Date creationDate = FBParser.convertMarkerDate(creationDateString);
			
			FBDateMarker marker = new FBDateMarker();
			marker.setCreationDate(creationDate);
			marker.setFbId(fbId);
			String fixedFbId = fbId.replace("x", "0");
			marker.setFbIdLong(Long.parseLong(fixedFbId));
			marker.setTimestamp(timestamp);
			JPA.em().persist(marker);
			total++;
			
			System.out.println("Processed : " + total);
		}
		
		in.close();
	}
	
	public static void createFetchesByDay(FBPage fbPage) {
		fbPage.getFetchesByDay().clear();
		DatedFeedFetch feedFetch = fbPage.getFetchByMonth();
		int numDays = feedFetch.getDateGranularity().getNumDays();
		
		Date lastDate  = feedFetch.getLastDate();
		int count = 0;
		for(FeedPage feedPage : feedFetch.getFeedPages()){
			System.out.println("processing feedPage : " + count);
			count++;
			if(!feedPage.getZoomed() && feedPage.getNumPosts() >= feedPage.getMaxPosts()){
				Date firstDate = feedPage.getFirstDate();
				System.out.println("found one at maximum");
				for(int i = 0; i <= numDays; i++){
					DatedFeedFetch byDay = new DatedFeedFetch();
					byDay.setDateGranularity(DateGranularity.DAY);
					byDay.setFirstDate(addDays(firstDate, i));
					byDay.setLastDate(addDays(firstDate, i + 1));
					JPA.em().persist(byDay);
					fbPage.getFetchesByDay().add(byDay);
				}
				feedPage.setZoomed(true);
			}
		}
	}
	
	public static void runDatedFeedFetch(DatedFeedFetch feedFetch, FBPage fbPage) {
		System.out.println("running dated feed fetch");
		
		try{
			Date currentDate = feedFetch.getCurrentDate();
			System.out.println("currentDate : " + currentDate);
			int numDays = feedFetch.getDateGranularity().getNumDays();
			Date targetDate = addDays(currentDate, numDays);
			while(currentDate.compareTo(feedFetch.getLastDate()) <= 0){
				System.out.println("fetching until : " + targetDate);
				Connection<Post> feed = FB.getInstance().getTimedConnection(fbPage.getFbId(), currentDate, targetDate);
				List<Post> posts = feed.getData();
				System.out.println("found posts : " + posts.size());
				List<FBPost> fbPosts = FBParser.parsePosts(posts);
				for(FBPost fbPost : fbPosts) {
					fbPost.setFbPage(fbPage);
					JPA.em().persist(fbPost);
				}
				
				FeedPage feedPage = new FeedPage();
				feedPage.setFirstDate(currentDate);
				feedPage.setLastDate(targetDate);
				feedPage.setPreviousPageUrl(feed.getPreviousPageUrl());
				feedPage.setNextPageUrl(feed.getNextPageUrl());
				feedPage.setNumPosts(posts.size());
				feedPage.setMaxPosts(FB.POST_LIMIT);
				JPA.em().persist(feedPage);
				feedFetch.getFeedPages().add(feedPage);
				
				
				
				currentDate = targetDate;
				feedFetch.setCurrentDate(currentDate);
				targetDate = addDays(targetDate, numDays);
				
				JPA.em().getTransaction().commit();
				JPA.em().getTransaction().begin();
			}
		} catch(Exception e){
			feedFetch.setErrorMessage(e.getClass().getSimpleName() + " exception : " + e.getMessage());
		}
	}
	
	public static Date addDays(Date currentDate, int numDays){
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, numDays);
		return c.getTime();
	}
	
}
