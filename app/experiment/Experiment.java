package experiment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.JsonMapper;
import com.restfb.scope.ScopeBuilder;
import com.restfb.scope.UserDataPermissions;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.batch.BatchRequest;
import com.restfb.batch.BatchRequest.BatchRequestBuilder;
import com.restfb.batch.BatchResponse;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Category;
import com.restfb.types.Comment;
import com.restfb.types.Comments;
import com.restfb.types.Page;
import com.restfb.types.Photo;
import com.restfb.types.Post;
import com.restfb.types.User;

import fb.FB;
import fb.FBComment;
import fb.FBMaster;
import fb.FBPage;
import fb.FBPost;
import fb.FeedFetch;
import fb.FeedType;
import linkedin.LI;
import linkedin.LIMaster;
import linkedin.LIPage;
import linkedin.LIParser;
import linkedin.LIUpdate;
import persistence.Test;
import persistence.TwitterUser;
import play.db.jpa.JPA;
import reporting.CSV;
import twitter.TweetType;
import twitter.TwitterInterface;
import twitter.TwitterMaster;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.SearchResource;
import twitter4j.conf.ConfigurationBuilder;
import utils.Utils;

public class Experiment {
	
	private static final String APP_SECRET = "17b49f9d44934c08902965568727117e"; 
	private static final String APP_ID = "847051758681799";
	private static final String REDIRECT = "https://www.facebook.com/connect/login_success.html";
	private static final String TEMP_USER_ACCESS = "AQBhzmEw3hmKQ_ffKvyVxI1GIdTop6-epXaeGIwRQzkR54VTfSJkJ8cNBwWn_jKqAbIi00bSQGyaGoC84dT4Nl2DXM6B04X6Ypt_ifeuenuf0UPVse4lqbFBKD86ZQ6dmvnL_T3Pvf4iTL9OytEJGQklKhR3lkv28EdYpdhJOBHszq4FM8qc28BRrjCUpnO-i3dk6kZyFVt9-k1kHsmxg-g1blfYWOmZShlCKvXYk-bOqbqJiu6D0Q_qroof8_yStP1IWa5HFnIswBKdhC6FmimmvrCGxGNUlv6qRVczTnhIkaUBN5vrh4vCmeUF1Cr2VcY";
	
	private static final String CHROME_EXE = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
	private static final String COOKIES = "--user-data-dir=C:\\Users\\jdclark\\AppData\\Local\\Google\\Chrome\\User Data";

	public static void runExperiment() throws IOException, SQLException {
		CSV.fbPhotosReport();
	}
	
	public static void jsonExperiment() throws IOException, InterruptedException, TwitterException {
		FBMaster.fetchFeeds(FeedType.PHOTOS);
////		FBMaster.getPhotoLikes();
//		JsonObject rawConnection = FB.getInstance().getGenericConnectionStart("DragonsKeepUtah"); 
////		System.out.println("raw : " + rawConnection);
//		JsonArray dataArray = rawConnection.getJsonArray("data");
//		System.out.println("dataArray.size(): " + dataArray.length());
////		
////		JsonObject first = dataArray.getJsonObject(0);
////		JsonObject comments = first.getJsonObject("comments");
////		JsonObject summary = comments.getJsonObject("summary");
////		int commentsCount = summary.getInt("total_count");
////		System.out.println("totalcount : " + commentsCount);
//		
//		JsonObject paging = rawConnection.getJsonObject("paging");
//		JsonObject cursors = paging.getJsonObject("cursors");
//		String after = cursors.getString("after");
//		System.out.println("after : " + after);
//		
//		JsonObject nextPage = FB.getInstance().continueGenericConnection("DragonsKeepUtah", after);
//		System.out.println("nextPage : " + nextPage);
//		JsonMapper jsonMapper = new DefaultJsonMapper();
////		Photo photo = jsonMapper.toJavaObject(first.toString(), Photo.class);
////		System.out.println("photo id : " + photo.getId());
		
	}
	
	public static void seleniumExperiment() throws InterruptedException {
		System.out.println("content : " + LI.getFullDocument("https://www.linkedin.com/company/kimley-horn-and-associates-inc-?trk=top_nav_home"));
		Thread.sleep(1000);
		System.out.println("content : " + LI.getFullDocument("https://www.linkedin.com/company/2928?trk=tyah&trkInfo=clickedVertical%3Acompany%2CclickedEntityId%3A2928%2Cidx%3A2-1-4%2CtarId%3A1445916217440%2Ctas%3ADiscovery%20Communications"));
	} 
	
	public static void liExperiment() throws Exception {
		System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");  
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		
//		Proxy proxy = new Proxy();
//		String proxyString = Global.getProxyUrl() + ":" + Global.getProxyPort(); 
//		proxy.setFtpProxy(proxyString);
//		proxy.setHttpProxy(proxyString);
//		proxy.setSslProxy(proxyString);
//		capabilities.setCapability(CapabilityType.PROXY, proxy);
//		capabilities.setCapability("chrome.switches",  Arrays.asList("--disable-javascript"));
//		WebDriver driver = new ChromeDriver(capabilities);
		ChromeOptions opt = new ChromeOptions();
		opt.setBinary(CHROME_EXE);
		opt.addArguments(COOKIES);
		WebDriver driver = new ChromeDriver(opt);
		
		String seed = "https://www.linkedin.com/";
		System.out.println("Performing faux  crawl : " + seed);
		
		/*********************** Perform crawl with small windowed normal browser ************************************/
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(seed);
		try{
			WebElement bodyElement = driver.findElement(By.cssSelector("body"));
			Thread.sleep(10);
		}
		catch(Exception e){
			System.out.println("caught an exception ");
			throw e;
		}
	}
	
	public static void fetchTwitter() throws Exception{
		
//		TwitterUser twitterUser = JPA.em().find(TwitterUser.class, 28L);
//		System.out.println("tweets : " + twitterUser.getTweets().size());
		
		TwitterMaster.readAndFetchPages();
		TwitterMaster.fetchTimelines(TweetType.STATUS);
		TwitterMaster.fetchTimelines(TweetType.MENTION); 
		
	}
	
	
	
	public static void twitterExperiments() throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("4PZxEOm6whMFjBc7Bi9XDA")
		  .setOAuthConsumerSecret("TJntYfhSNjDAN997BWtVYbw74AXwIxc6x9EYD7VcMok")
		  .setOAuthAccessToken("381118490-zArlTcjR1JHlgrhOMUH6RowcaUXwOKF6cuAVGyA3")
		  .setOAuthAccessTokenSecret("lgzUKc6IDtt6afR1dmDzlm7l1Rpp1RUf2froBfrzw");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		Query query = new Query("@Google").count(100);
		QueryResult result = twitter.search(query);
		List<Status> tweets = result.getTweets();
		System.out.println("tweets : " + tweets.size());
		System.out.println("next query : " + result.nextQuery());
		
		
		
//		List<Status> statuses = twitter.getUserTimeline("TlkngMchns");
		
//		System.out.println("statuses : " + statuses.size());
//		
//		for(Status status : statuses) {
//			System.out.println("status : " + status.getId() + " " + status.getCreatedAt());
//		}
//		
//		Paging paging = new Paging(1,400).maxId(647712548406591488L);
//		
//		statuses = twitter.getUserTimeline("TlkngMchns", paging);
//		System.out.println("second page statuses : " + statuses.size());
//		for(Status status : statuses) {
//			System.out.println("status : " + status.getId() + " " + status.getCreatedAt());
//		}
	}
	
	public static void batchRequests() {
		System.out.println("getting access token");
		AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(APP_ID, APP_SECRET);
		System.out.println("creating client");
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
		
		BatchRequest pageRequest = new BatchRequestBuilder("kengarffauto").parameters(Parameter.with("summary",true), 
				Parameter.with("fields", "about,affiliation,app_id,attire,best_page,built,can_checkin,category,category_list,"
						+ "checkins,company_overview,contact_address,culinary_team,current_location,description,display_subtext,"
						+ "emails,fan_count,features,food_styles,founded,general_info,general_manager,global_brand_root_id,"
						+ "hours,impressum, is_always_open,is_community_page,is_permanently_closed,is_unclaimed,is_verified,link,"
						+ "location,members,mission,mpg,name, parent_page,parking,payment_options, personal_info, personal_interests,"
						+ "pharma_safety_info,phone,place_type,price_range, products,public_transit,restaurant_services,"
						+ "restaurant_specialties,single_line_address,start_info,store_location_descriptor,store_number,"
						+ "talking_about_count,username,verification_status,voip_info,website,were_here_count")).build();
		
		BatchRequest feedRequest = new BatchRequestBuilder("kengarffauto/feed").parameters(Parameter.with("include_hidden", false), Parameter.with("fields", 
				"application, created_time, caption, feed_targeting,from{id}, is_hidden, link, message, message_tags, object_id, parent_id,"
				+ "place, privacy, shares, source, status_type, story, targeting,to{id},type, updated_time,"
				+ "with_tags, likes.limit(0).summary(true), "
				+ "comments.limit(0).summary(true)")).build();
		
		List<BatchResponse> batchResponses = facebookClient.executeBatch(pageRequest, feedRequest);

		// Responses are ordered to match up with their corresponding requests.

		BatchResponse pageResponse = batchResponses.get(0);
		BatchResponse feedResponse = batchResponses.get(1);
		
		if(pageResponse.getCode() != 200)
			  System.out.println("Batch request failed: " + pageResponse);
		else {
			JsonMapper jsonMapper = new DefaultJsonMapper();
			Page page = jsonMapper.toJavaObject(pageResponse.getBody(), Page.class);
			System.out.println("page : " + page);
	
			Connection<Post> feed = new Connection<Post>(facebookClient, feedResponse.getBody(), Post.class);
			System.out.println("feed : " + feed.getData().size());
		}
	}
	
	public static void fetchPosts(){
		FBPage fbPage = fetchPage();
		
		System.out.println("getting access token");
		AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(APP_ID, APP_SECRET);
		System.out.println("creating client");
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
		System.out.println("creating connection");
		String url = fbPage.getFbId() + "/feed";
		System.out.println("url : " + url);
		Connection<Post> feed = facebookClient.fetchConnection(url, Post.class, Parameter.with("include_hidden", false), Parameter.with("fields", 
				"application, created_time, caption, feed_targeting,from{id}, is_hidden, link, message, message_tags, object_id, parent_id,"
				+ "place, privacy, shares, source, status_type, story, targeting,to{id},type, updated_time,"
				+ "with_tags, likes.limit(0).summary(true), "
				+ "comments.limit(0).summary(true)"));
		
		int index = 0;
		System.out.println("getting data");
		List<Post> posts = feed.getData();
		System.out.println("Got data");
		System.out.println("posts : " + posts.size());
		for(Post post : posts) {
			Comments commentsObject;
			if((commentsObject = post.getComments()) == null){
				System.out.println("no comments");
			}
			else {
				System.out.println("num comments : " + post.getCommentsCount());
				List<Comment> comments = commentsObject.getData();
				int commentIndex = 0;
				for(Comment comment : comments) {
					FBComment fbComment = new FBComment();
					fbComment.setId(comment.getId());
					fbComment.setCommentCount(comment.getCommentCount());
					fbComment.setCreatedTime(comment.getCreatedTime() + "");
					fbComment.setFromId(comment.getFrom().getId());
					fbComment.setLikeCount(comment.getLikeCount());
					fbComment.setMessage(comment.getMessage());
					
					JPA.em().persist(fbComment);
					System.out.println("comment : " + ++commentIndex + comment.getMessage());
				}
			}
			FBPost fbPost = new FBPost();
			fbPost.setFbPage(fbPage);
			fbPost.setApp(post.getApplication() + "");
			fbPost.setCreatedTime(post.getCreatedTime() + "");
			fbPost.setCaption(post.getCaption());
			fbPost.setFeedTargeting(post.getFeedTargeting()+"");
			fbPost.setFromId(post.getFrom().getId());
			fbPost.setIsHidden(post.getIsHidden());
			fbPost.setLink(post.getLink());
			fbPost.setMessage(post.getMessage());
			fbPost.setMessageTags(post.getMessageTags() + "");
			fbPost.setObjectId(post.getObjectId());
			fbPost.setParentId(post.getParentId());
			fbPost.setPlace(post.getPlace() + "");
			fbPost.setPrivacy(post.getPrivacy() + "");
			fbPost.setShares(post.getSharesCount());
			fbPost.setSource(post.getSource());
			fbPost.setStatusType(post.getStatusType());
			fbPost.setStory(post.getStory());
			fbPost.setTargeting(post.getTargeting() + "");
			fbPost.setToId(Utils.listToIdCSV(post.getTo()));
			fbPost.setType(post.getType());
			fbPost.setUpdatedTime(post.getUpdatedTime() + "");
			fbPost.setWithTags(post.getWithTags() + "");
			fbPost.setLikesCount(post.getLikesCount());
			fbPost.setCommentsCount(post.getCommentsCount());
			fbPost.setReactionsCount(post.getReactionsCount());
			
			JPA.em().persist(fbPost);
			
		}
	}
	
	public static FBPage fetchPage() {
		AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(APP_ID, APP_SECRET);
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
		Page page = facebookClient.fetchObject("kengarffauto",  Page.class, Parameter.with("summary",true), 
				Parameter.with("fields", "about,affiliation,app_id,attire,best_page,built,can_checkin,category,category_list,"
						+ "checkins,company_overview,contact_address,culinary_team,current_location,description,display_subtext,"
						+ "emails,fan_count,features,food_styles,founded,general_info,general_manager,global_brand_root_id,"
						+ "hours,impressum, is_always_open,is_community_page,is_permanently_closed,is_unclaimed,is_verified,link,"
						+ "location,members,mission,mpg,name, parent_page,parking,payment_options, personal_info, personal_interests,"
						+ "pharma_safety_info,phone,place_type,price_range, products,public_transit,restaurant_services,"
						+ "restaurant_specialties,single_line_address,start_info,store_location_descriptor,store_number,"
						+ "talking_about_count,username,verification_status,voip_info,website,were_here_count"));
		FBPage fbPage = new FBPage();
		fbPage.setGivenUrl("kengarffauto");
		fbPage.setFbId(page.getId());
		fbPage.setAbout(page.getAbout());
		fbPage.setAffiliation(page.getAffiliation());
		fbPage.setAppId(page.getAppId());
		fbPage.setAttire(page.getAttire());
		if(page.getBestPage() != null) {
			fbPage.setBestPage(page.getBestPage().getId());
		}
		fbPage.setBuilt(page.getBuilt()); 
		fbPage.setCanCheckin(page.getCanCheckin());
		fbPage.setCategory(page.getCategory());
		if(page.getCategoryList() != null){
			List<String> categories = new ArrayList<String>();
			for(Category category : page.getCategoryList()){
				categories.add(category.getName());
			}
			fbPage.setSubCategories(categories);
		}
		fbPage.setCheckins(page.getCheckins());
		fbPage.setOverview(page.getCompanyOverview());
		fbPage.setContactAddress(page.getContactAddress() + "");
		fbPage.setCulinaryTeam(page.getCulinaryTeam());
		fbPage.setCurrentLocation(page.getCurrentLocation());
		fbPage.setDescription(page.getDescription());
		fbPage.setDisplaySubtext(page.getDisplaySubtext());
		if(page.getEmails() != null){
			fbPage.setEmails(page.getEmails());
		}
		fbPage.setLikes(page.getFanCount());
		fbPage.setFeatures(page.getFeatures());
		if(page.getFoodStyles() != null){
			fbPage.setFoodStyles(page.getFoodStyles());
		}
		fbPage.setFounded(page.getFounded());
		fbPage.setGeneralInfo(page.getGeneralInfo());
		fbPage.setGeneralManager(page.getGeneralManager());
		fbPage.setGlobalBrandRootId(page.getGlobalBrandRootId());
		fbPage.setHours(page.getHours() + "");
		fbPage.setImpressum(page.getImpressum());
		fbPage.setAlwaysOpen(page.getIsAlwaysOpen());
		fbPage.setCommunityPage(page.getIsCommunityPage());
		fbPage.setPermanentlyClosed(page.getIsPermanentlyClosed());
		fbPage.setUnclaimed(page.getIsUnclaimed());
		fbPage.setVerified(page.getIsVerified());
		fbPage.setLink(page.getLink());
		fbPage.setLocation(page.getLocation() + "");
		fbPage.setMembers(page.getMembers());
		fbPage.setMission(page.getMission());
		fbPage.setMpg(page.getMpg());
		fbPage.setName(page.getName());
		fbPage.setPaymentOptions(page.getPaymentOptions() + "");
		fbPage.setPersonalInfo(page.getPersonalInfo());
		fbPage.setPersonalInterests(page.getPersonalInterests());
		fbPage.setPharmaSafetyInfo(page.getPharmaSafetyInfo());
		fbPage.setPhone(page.getPhone());
		fbPage.setPlaceType(page.getPlaceType());
		fbPage.setPriceRange(page.getPriceRange());
		fbPage.setProducts(page.getProducts());
		fbPage.setPublicTransit(page.getPublicTransit());
		fbPage.setRestaurantServices(page.getRestaurantServices() + "");
		fbPage.setRestaurantSpecialties(page.getRestaurantSpecialties() + "");
		fbPage.setSingleLineAddress(page.getSingleLineAddress());
		if(page.getStartInfo() != null){
			fbPage.setStartDate(page.getStartInfo().getDate() + "");
			fbPage.setStartType(page.getStartInfo().getType()); 
		}
		fbPage.setStoreLocationDescriptor(page.getNameWithLocationDescriptor());
		fbPage.setStoreNumber(page.getStoreNumber());
		fbPage.setTalkingAbout(page.getTalkingAboutCount());
		fbPage.setUsername(page.getUsername());
		fbPage.setVerificationStatus(page.getVerificationStatus());
		fbPage.setVoipInfo(page.getVoipInfo() + "");
		fbPage.setWebsite(page.getWebsite());
		fbPage.setWereHere(page.getWereHereCount());
		page.getArtistsWeLike();
		
		JPA.em().persist(fbPage);
		System.out.println("page : " + page.getHours());
		System.out.println("page likes : "  + page.getDisplaySubtext());
		return fbPage;
	}
	
	
	public static void fbClientTesting() throws Exception {
		try{
			System.out.println("Running experiment");
			ScopeBuilder scopeBuilder = new ScopeBuilder();
			scopeBuilder.addPermission(UserDataPermissions.USER_POSTS);
			
			
			AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(APP_ID, APP_SECRET);
			FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
			//Page page = facebookClient.fetchObject("kengarffauto",  Page.class, Parameter.with("summary",true), Parameter.with("fields", "phone,fan_count"));
//			System.out.println("page : " + page.getPhone());
//			System.out.println("page likes : "  + page.getFanCount());
			
			
			Connection<Post> feed = facebookClient.fetchConnection("kengarffauto/feed", Post.class, Parameter.with("include_hidden", false), Parameter.with("fields",  "created_time,from, to, likes.limit(0).summary(true), comments"));
			int index = 0;
			List<Post> posts = feed.getData();
			for(Post post : posts) {
				System.out.println("post : "  + ++index + post.getMessage());
				System.out.println("likes : " + post.getLikesCount());
				System.out.println("date : " + post.getCreatedTime()); 
				System.out.println("from : " + post.getFrom());
//				Comments commentsObject;
//				if((commentsObject = post.getComments()) == null){
//					System.out.println("no comments");
//				}
//				else {
//					List<Comment> comments = commentsObject.getData();
//					int commentIndex = 0;
//					for(Comment comment : comments) {
//						System.out.println("comment : " + ++commentIndex + comment.getMessage());
//					}
//				}
			}
			
			
		}
		catch(Exception e){
			System.out.println("type : " + e.getClass().getSimpleName());
			System.out.println("message : " + e.getMessage());
			
		}
	}
} 
