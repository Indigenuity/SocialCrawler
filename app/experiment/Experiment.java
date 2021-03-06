package experiment;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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

import fb.DatedFeedFetch;
import fb.FB;
import fb.FBComment;
import fb.FBMaster;
import fb.FBPage;
import fb.FBParser;
import fb.FBPost;
import fb.FBdao;
import fb.FeedFetch;
import fb.FeedPage;
import fb.FeedType;
import fb.DatedFeedFetch.DateGranularity;
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
import utils.ThrottledLimiter;
import utils.Utils;

public class Experiment {

	private static final String APP_SECRET = "17b49f9d44934c08902965568727117e";
	private static final String APP_ID = "847051758681799";
	private static final String REDIRECT = "https://www.facebook.com/connect/login_success.html";
	private static final String TEMP_USER_ACCESS = "AQBhzmEw3hmKQ_ffKvyVxI1GIdTop6-epXaeGIwRQzkR54VTfSJkJ8cNBwWn_jKqAbIi00bSQGyaGoC84dT4Nl2DXM6B04X6Ypt_ifeuenuf0UPVse4lqbFBKD86ZQ6dmvnL_T3Pvf4iTL9OytEJGQklKhR3lkv28EdYpdhJOBHszq4FM8qc28BRrjCUpnO-i3dk6kZyFVt9-k1kHsmxg-g1blfYWOmZShlCKvXYk-bOqbqJiu6D0Q_qroof8_yStP1IWa5HFnIswBKdhC6FmimmvrCGxGNUlv6qRVczTnhIkaUBN5vrh4vCmeUF1Cr2VcY";

	private static final String CHROME_EXE = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
	private static final String COOKIES = "--user-data-dir=C:\\Users\\JD\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 1";
	
	private static final ThrottledLimiter rateLimiter;
	private static final ThrottledLimiter shortLimiter;
	private static WebDriver driver;
	 
	static {
		System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");   
		ChromeOptions opt = new ChromeOptions();
		opt.setBinary(CHROME_EXE);
		opt.addArguments(COOKIES);
//		driver = new ChromeDriver(opt);
		driver = null;
		
		rateLimiter = new ThrottledLimiter(.2, 60, TimeUnit.SECONDS); 
		shortLimiter = new ThrottledLimiter(2, 60, TimeUnit.SECONDS);
	}
	private static boolean started = false;
	
	public static void parseRawPosts(){
		String query = "from FBPost p where p.fromText is not null";
		int count = 0;
		int offset = 0;
		List<FBPost> fbPosts;  
		
			fbPosts = JPA.em().createQuery(query, FBPost.class).getResultList();
			System.out.println("fbPosts size : " + fbPosts.size());
			for(FBPost fbPost : fbPosts) {
				FBParser.parseTextPost(fbPost);
				count++;
				if(count % 500 == 0) {
					JPA.em().getTransaction().commit();
					JPA.em().getTransaction().begin(); 
					System.out.println("Processed : " + count);
				}
			}
			
	}
	
	
	public static void runExperiment() throws Exception {
		System.out.println("running experiment");
		doLinkedIn();
	}
	
	public static void doLinkedIn() throws IOException, InterruptedException{
		System.out.println("doing linked in");
//		LIMaster.readAndFetchPages();
		LIPage liPage = JPA.em().find(LIPage.class, 1L);
		System.out.println("found liPage : "+ liPage);
		Document doc = LI.getMainDocument(liPage.getTrimmedUrl());
//		LIParser.parseDocument(liPage, doc);
	}
	
	public static void runfbExperiment() throws IOException, SQLException, ParseException, InterruptedException {

//		dateUpdate();
//		CSV.fbPostsReport();
//		newCreateDayFetches();
		newFillDayFetches();
		
//		List<FBPage> fbPages = JPA.em().createQuery("from FBPage fb", FBPage.class).getResultList();
//		System.out.println("filling day fetches for pages : " + fbPages.size());
//		
//		for(FBPage fbPage : fbPages) {
//			DatedFeedFetch monthFetch = fbPage.getFetchByMonth();
//			monthFetch.setFbPage(fbPage);
//			for(FeedPage monthFeedPage : monthFetch.getFeedPages()){
//				System.out.println("setting feedPage");
//				monthFeedPage.setFeedFetch(monthFetch);
//				for(DatedFeedFetch dayFetch : monthFeedPage.getSubFetches()){
//					dayFetch.setFbPage(fbPage);
//					System.out.println("set fbPage : " + fbPage);
//				}
//			}
//		}
		
	}
	
	public static void runFbFetches() throws IOException { 
//		FBMaster.readAndFetchPages();
//		JPA.em().getTransaction().commit();
//		JPA.em().getTransaction().begin(); 
//		createMonthFetches();
//		fillMonthFetches();
//		JPA.em().getTransaction().commit();
//		JPA.em().getTransaction().begin(); 
//		JPA.em().clear();
		createDayFetches();
		JPA.em().getTransaction().commit();
		JPA.em().getTransaction().begin(); 
		JPA.em().clear();
		fillDayFetches();
	}
	
	public static void createDayFetches() {
		List<FBPage> fbPages = JPA.em().createQuery("from FBPage fb", FBPage.class).getResultList();
		
		for(FBPage fbPage : fbPages){
			FBMaster.createFetchesByDay(fbPage);
		}
	}
	
	public static void fillDayFetches() {
		List<FBPage> fbPages = JPA.em().createQuery("from FBPage fb", FBPage.class).getResultList();
		System.out.println("filling day fetches for pages : " + fbPages.size());
		for(FBPage fbPage : fbPages){
			System.out.println("page has feedfetches : " + fbPage.getFetchesByDay().size());
			for(DatedFeedFetch feedFetch : fbPage.getFetchesByDay()){
				System.out.println("running day fetch : " + feedFetch.getDatedFeedFetchId());
				FBMaster.runDatedFeedFetch(feedFetch, fbPage);
			}
		}
	}
	
	public static void newCreateDayFetches() {
		List<FBPage> fbPages = JPA.em().createQuery("from FBPage fb", FBPage.class).getResultList();
		System.out.println("filling day fetches for pages : " + fbPages.size());
		for(FBPage fbPage : fbPages){
			
			DatedFeedFetch feedFetch = fbPage.getFetchByMonth();
			int numDays = feedFetch.getDateGranularity().getNumDays();
			
			for(FeedPage feedPage : fbPage.getFetchByMonth().getFeedPages()){
				Date firstDate = feedPage.getFirstDate();
				if(!feedPage.getZoomed() && feedPage.getNumPosts() >= feedPage.getMaxPosts()){
					for(int i = 0; i <= numDays; i++){
						DatedFeedFetch dayFetch = new DatedFeedFetch();
						dayFetch.setDateGranularity(DateGranularity.DAY);
						dayFetch.setFirstDate(FBMaster.addDays(firstDate, i));
						dayFetch.setLastDate(FBMaster.addDays(firstDate, i + 1));
						JPA.em().persist(dayFetch);
						feedPage.getSubFetches().add(dayFetch);
					}
					feedPage.setZoomed(true);
				}
			}
			System.out.println("page has monthFetch : " + fbPage.getFetchByMonth());
		}
	}
	
	public static void newFillDayFetches() {
		String queryString = "select sf from FeedPage fp join fp.subFetches sf where sf.completed = false";
//		String queryString = "select dff from DatedFeedFetch dff join dff.feedPages fp where fp.subFetches is not empty";
		int count = 500;
		int offset = 0;
		List<DatedFeedFetch> dayFetches;
//		List<FeedPage> feedPages = JPA.em().createQuery(queryString, FeedPage.class).getResultList();
//		System.out.println("feedPages : " + feedPages.size());
//		
//		for(FeedPage feedPage : feedPages){
//			for(DatedFeedFetch dayFetch : feedPage.getSubFetches()) {
//				System.out.println("dayFetch : " + dayFetch);
//				System.out.println("feedFetch : " + feedPage.getFeedFetch());
//				dayFetch.setFbPage(feedPage.getFeedFetch().getFbPage());
//			}
//		}
		do{
			dayFetches = JPA.em().createQuery(queryString, DatedFeedFetch.class).setMaxResults(count).setFirstResult(offset)
					.getResultList();
			System.out.println("dayFetches size : " + dayFetches.size());
			for (DatedFeedFetch dayFetch : dayFetches) {
//				System.out.println("fbPage : " + dayFetch.getFbPage());
				
				FBMaster.runDatedFeedFetch(dayFetch, dayFetch.getFbPage());
//				Date createdDate = format.parse(fbPost.getCreatedTime());
//				Date updatedTime = format.parse(fbPost.getUpdatedTime());
//				// System.out.println("date: " + createdDate);
//				// System.out.println("updatedTime : " + updatedTime);
//				fbPost.setRealCreatedDate(createdDate);
//				fbPost.setRealLastUpdated(updatedTime);
			}

			offset += count;
			System.out.println("processed : " + offset);
			JPA.em().getTransaction().commit();
			JPA.em().getTransaction().begin();
			JPA.em().clear();
			System.gc();
			
		} while(dayFetches.size() > 0);
	}
	
	public static void fillMonthFetches() throws IOException {
		
		List<FBPage> fbPages = JPA.em().createQuery("from FBPage fb", FBPage.class).getResultList();
		
		for(FBPage fbPage : fbPages){
			System.out.println("running month fetch for page : " + fbPage.getName());
			FBMaster.runDatedFeedFetch(fbPage.getFetchByMonth(), fbPage);
		}
	}
	
	public static void createMonthFetches() throws IOException {
		
		List<FBPage> fbPages = JPA.em().createQuery("from FBPage fb", FBPage.class).getResultList();
		
		for(FBPage fbPage : fbPages){
			System.out.println("creating month fetch for page : " + fbPage.getName());
			DatedFeedFetch feedFetch = new DatedFeedFetch();
			feedFetch.setDateGranularity(DateGranularity.MONTH);
			JPA.em().persist(feedFetch);
			fbPage.setFetchByMonth(feedFetch);
		}
	}
	
	public static void runReports() throws InterruptedException, IOException, SQLException{
		
//		parseRawPosts();
//		CSV.allReports();
		
//		doBasicCrawls();
//		if(!started){
//			driver.get("http://facebook.com/Nordstrom");
//			started = true;
//		}
//		else {
//			WebElement postSection = driver.findElement(By.cssSelector("#PagePostsSectionPagelet-12854644836-3"));
//			System.out.println("postSection : " + postSection);
//			WebElement morePostsButton = postSection.findElement(By.cssSelector(".uiMorePager"));
//			WebElement morePostsPrimaryButton = postSection.findElement(By.cssSelector(".uiMorePagerPrimary"));
//			morePostsButton.click();
////			morePostsPrimaryButton.click();
//		}
	}
	
	public static void doBasicCrawls() throws InterruptedException {
		String query = "select p from FBPage p join p.posts pp group by p.fbPageId order by count(pp)";
		
		
		List<FBPage> fbPages = JPA.em().createQuery(query, FBPage.class).getResultList();
		System.out.println("fbPages : " + fbPages.size());
		for(FBPage fbPage : fbPages) {
			FeedFetch feedFetch = FBdao.getFeedFetch(fbPage, FeedType.MANUAL);
			scrapePage(feedFetch);
		}
		
//		FBPage fbPage = JPA.em().find(FBPage.class, 86L);
//		
//		FeedFetch feedFetch = FBdao.getFeedFetch(fbPage, FeedType.MANUAL);
//		scrapePage(feedFetch);
	}
	
	private static void scrapePage(FeedFetch feedFetch) throws InterruptedException{
		
		try{
			FBPage fbPage = feedFetch.getFbPage();
			if(feedFetch.getReachedEnd()){
				return;
			}
			rateLimiter.acquire();
			driver.get("http://facebook.com/" + fbPage.getFbId());
			
			List<WebElement> yearNavs = driver.findElements(By.cssSelector("ul[aria-label='Timeline'] li"));
			System.out.println("found yearNavs of size : " + yearNavs.size());
			int numPosts = 0;
			for(WebElement yearNav : yearNavs){
				numPosts = scrapeYear(feedFetch, yearNav);
				System.out.println("number of posts for the year : " + numPosts);
				if(numPosts > 200){
					JPA.em().getTransaction().commit();
					JPA.em().getTransaction().begin();
					throw new IllegalStateException("Too Many Damn Posts");
				}
			}
			
			feedFetch.setReachedEnd(true);
			JPA.em().getTransaction().commit();
			JPA.em().getTransaction().begin();
			
		}catch(Exception e){
			feedFetch.setErrorMessage(e.getClass().getSimpleName()+ " Exception : " + e.getMessage());
			System.out.println(feedFetch.getErrorMessage());
		}
		
//		Elements elements = doc.select("ul[aria-label='Timeline'] li");
//		System.out.println("elements size : " + years.size());
//		Thread.sleep(1000);
//		List<WebElement> posts = driver.findElements(By.cssSelector("div.userContentWrapper:not([crawled])"));
//		List<FBPost> fbPosts = FBParser.parseDomPosts(posts);
//		System.out.println("posts : " + posts.size());
//		for(WebElement yearNav : years){
//			scrapeYear(yearNav);
//		}
	}
	
	private static void scrapeYear(FeedFetch feedFetch, int year) {
		
	}
	
	private static int scrapeYear(FeedFetch feedFetch, WebElement yearNav){
		boolean shouldCrawl = false;
		int numPosts = 0;
		Integer yearInt = null;
		FBPage fbPage = feedFetch.getFbPage();
		Date earliestPost = fbPage.getFirstPost();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(earliestPost);
		
		try{
			 yearInt = Integer.parseInt(yearNav.getText());
			 
			if(yearInt <= calendar.get(Calendar.YEAR) && yearInt < feedFetch.getEarliestBasicYear() && yearInt > 2005){
				shouldCrawl = true;
			}
		} catch(Exception e){
			
			System.out.println("year text not parsable to int : " + yearNav.getText());
		}
		System.out.println("yearInt : " + yearInt);
		System.out.println("schouldCrawl : " + shouldCrawl);
		if(shouldCrawl) {
			System.out.println("clicking yearNav : " + yearInt);
			shortLimiter.acquire();
			yearNav.click();
			shortLimiter.acquire();
			List<WebElement> yearPostSections = driver.findElements(By.cssSelector("div[id^=PagePostsSectionPagelet"));
			System.out.println("yearPostSections : " + yearPostSections.size());
			for(WebElement postSection : yearPostSections) {
				WebElement yearLabel = postSection.findElement(By.cssSelector(".lfloat"));
				
				String yearLabelText = yearLabel.getText();
				if(yearNav.getText().equals(yearLabelText)){
					System.out.println("found appropriate section");
					numPosts += scrapePostSection(feedFetch, postSection); 
				}
			}
			feedFetch.setEarliestBasicYear(yearInt);
		}
		return numPosts;
	}
	
	private static int scrapePostSection(FeedFetch feedFetch, WebElement postSection) {
		int numPosts = 0;
		shortLimiter.acquire();
		WebElement popoverButton = postSection.findElement(By.cssSelector(".clearfix div.uiPopover.rfloat"));
		System.out.println("clicking highlights popdown");
		
		popoverButton.click();
		shortLimiter.acquire();
		WebElement allStoriesButton = driver.findElement(By.cssSelector("div.uiLayer:not(.hidden_elem) span span span")); 
		System.out.println("clicking all stories button");
		allStoriesButton.click();
		List<FBPost> fbPosts;
		boolean keepGoing = true;
		do{
			System.out.println("scrolling down");
			rateLimiter.acquire();
			scrollDown();
			rateLimiter.acquire();
			System.out.println("extracting posts");
			fbPosts = extractNewPosts(postSection);
			if(fbPosts.size() < 1){
				try{
					System.out.println("looking for more posts button");
					rateLimiter.acquire();
					WebElement morePostsButton = postSection.findElement(By.cssSelector(".uiMorePager"));
					System.out.println("clicking more posts button");
					morePostsButton.click();
					rateLimiter.acquire();
					scrollDown();
					rateLimiter.acquire();
					fbPosts = extractNewPosts(postSection);
				}catch(Exception e){
					System.out.println("Error following more posts button:  " + e.getMessage());
				}
			}
			for(FBPost fbPost : fbPosts) {
				fbPost.setFbPage(feedFetch.getFbPage());
				JPA.em().persist(fbPost);
				feedFetch.setEarliestPost(fbPost.getCreatedTime());
				System.out.println("Earliest : "+ feedFetch.getEarliestPost());
			}
			numPosts += fbPosts.size();
			JPA.em().getTransaction().commit();
			JPA.em().getTransaction().begin();
		}while(fbPosts != null && fbPosts.size() > 0);
		return numPosts;
	}
	
	private static List<FBPost> extractNewPosts(WebElement postSection){
		try{
			List<WebElement> posts = postSection.findElements(By.cssSelector("div._5pat:not([crawled])"));
			System.out.println("posts : " + posts.size());
			for(WebElement post : posts){
				setAttr(post, "crawled", "true");
			}
			return FBParser.parseDomPosts(posts);
		} catch(NoSuchElementException e){
			return new ArrayList<FBPost>();
		} catch(ElementNotVisibleException e){
			return new ArrayList<FBPost>();
		}
		
	}
	
	private static void setAttr(WebElement element, String attr, String value){
		String id = element.getAttribute("id");
//		System.out.println("setting attribute of id : " + id);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("document.getElementById('" + id + "').setAttribute('" + attr + "', '" + value + "');");
	}
	
	private static void scrollDown() {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("scrollBy(0, 100000)");
	}
	
	public static void fbFeedExperiment() throws IOException, SQLException, InterruptedException, ParseException {

		FBPage fbPage = JPA.em().find(FBPage.class, 34L);
		
		Connection<Post> connection = FB.getInstance().getFeedStart(fbPage.getFbId(), Post.class);
		String nextPageUrl = null;
		String previousPageUrl = null;
		while(connection.getData().size() > 0){
			for(Post post : connection.getData()){
				System.out.println("date : " + post.getCreatedTime());
				System.out.println("message : "+ post.getMessage());
			}
			System.out.println("nextPageUrl : " + connection.getNextPageUrl());
			if(connection.getPreviousPageUrl() != null){
				previousPageUrl = connection.getPreviousPageUrl();	
				System.out.println("previousPageUrl : " + previousPageUrl);
			}
			connection = FB.getInstance().continueFeed(connection.getNextPageUrl(), Post.class);
			
		}
		System.out.println("starting reverse traversal");
		connection = FB.getInstance().continueFeed(previousPageUrl, Post.class);
		while(connection.getData().size() > 0){
			for(Post post : connection.getData()){
				System.out.println("date : " + post.getCreatedTime());
				System.out.println("message : "+ post.getMessage());
			}
			previousPageUrl = connection.getPreviousPageUrl();
			System.out.println("previousPageUrl : " + previousPageUrl);
			System.out.println("nextPageUrl : " + connection.getNextPageUrl());
			connection = FB.getInstance().continueFeed(connection.getNextPageUrl(), Post.class);
		}
			
		
//		String query = "from FBPage p";
//		List<FBPage> fbPages = JPA.em().createQuery(query, FBPage.class).getResultList();
//		for(FBPage fbPage : fbPages) {
//			
//		}
	}

	public static void jsonExperiment() throws IOException, InterruptedException, TwitterException {
		FBMaster.fetchFeeds(FeedType.PHOTOS);
		//// FBMaster.getPhotoLikes();
		// JsonObject rawConnection =
		//// FB.getInstance().getGenericConnectionStart("DragonsKeepUtah");
		//// System.out.println("raw : " + rawConnection);
		// JsonArray dataArray = rawConnection.getJsonArray("data");
		// System.out.println("dataArray.size(): " + dataArray.length());
		////
		//// JsonObject first = dataArray.getJsonObject(0);
		//// JsonObject comments = first.getJsonObject("comments");
		//// JsonObject summary = comments.getJsonObject("summary");
		//// int commentsCount = summary.getInt("total_count");
		//// System.out.println("totalcount : " + commentsCount);
		//
		// JsonObject paging = rawConnection.getJsonObject("paging");
		// JsonObject cursors = paging.getJsonObject("cursors");
		// String after = cursors.getString("after");
		// System.out.println("after : " + after);
		//
		// JsonObject nextPage =
		//// FB.getInstance().continueGenericConnection("DragonsKeepUtah",
		//// after);
		// System.out.println("nextPage : " + nextPage);
		// JsonMapper jsonMapper = new DefaultJsonMapper();
		//// Photo photo = jsonMapper.toJavaObject(first.toString(),
		//// Photo.class);
		//// System.out.println("photo id : " + photo.getId());

	}

	public static void dateUpdate() throws IOException, InterruptedException, ParseException {
		System.out.println("running experiment");
		String query = "from FBPost p";
		int count = 5000;
		int offset = 0;
		List<FBPost> fbPosts;
		DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
		do {
			fbPosts = JPA.em().createQuery(query, FBPost.class).setMaxResults(count).setFirstResult(offset)
					.getResultList();
			// Thu Apr 28 17:00:00 MDT 2016
			for (FBPost fbPost : fbPosts) {
				Date createdDate = format.parse(fbPost.getCreatedTime());
				Date updatedTime = format.parse(fbPost.getUpdatedTime());
				// System.out.println("date: " + createdDate);
				// System.out.println("updatedTime : " + updatedTime);
				fbPost.setRealCreatedDate(createdDate);
				fbPost.setRealLastUpdated(updatedTime);
			}

			offset += count;
			System.out.println("processed : " + offset);
			JPA.em().getTransaction().commit();
			JPA.em().getTransaction().begin();
			JPA.em().clear();
			System.gc();
		} while (fbPosts.size() > 0);

	}

	public static void seleniumExperiment() throws InterruptedException {
		System.out.println("content : " + LI
				.getFullDocument("https://www.linkedin.com/company/kimley-horn-and-associates-inc-?trk=top_nav_home"));
		Thread.sleep(1000);
		System.out.println("content : " + LI.getFullDocument(
				"https://www.linkedin.com/company/2928?trk=tyah&trkInfo=clickedVertical%3Acompany%2CclickedEntityId%3A2928%2Cidx%3A2-1-4%2CtarId%3A1445916217440%2Ctas%3ADiscovery%20Communications"));
	}

	public static void liExperiment() throws Exception {
		System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();

		// Proxy proxy = new Proxy();
		// String proxyString = Global.getProxyUrl() + ":" +
		// Global.getProxyPort();
		// proxy.setFtpProxy(proxyString);
		// proxy.setHttpProxy(proxyString);
		// proxy.setSslProxy(proxyString);
		// capabilities.setCapability(CapabilityType.PROXY, proxy);
		// capabilities.setCapability("chrome.switches",
		// Arrays.asList("--disable-javascript"));
		// WebDriver driver = new ChromeDriver(capabilities);
		ChromeOptions opt = new ChromeOptions();
		opt.setBinary(CHROME_EXE);
		opt.addArguments(COOKIES);
		WebDriver driver = new ChromeDriver(opt);

		String seed = "https://www.linkedin.com/";
		System.out.println("Performing faux  crawl : " + seed);

		/***********************
		 * Perform crawl with small windowed normal browser
		 ************************************/
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(seed);
		try {
			WebElement bodyElement = driver.findElement(By.cssSelector("body"));
			Thread.sleep(10);
		} catch (Exception e) {
			System.out.println("caught an exception ");
			throw e;
		}
	}

	public static void fetchTwitter() throws Exception {

		// TwitterUser twitterUser = JPA.em().find(TwitterUser.class, 28L);
		// System.out.println("tweets : " + twitterUser.getTweets().size());

		TwitterMaster.readAndFetchPages();
		TwitterMaster.fetchTimelines(TweetType.STATUS);
//		TwitterMaster.fetchTimelines(TweetType.MENTION);

	}

	public static void twitterExperiments() throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("4PZxEOm6whMFjBc7Bi9XDA")
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

		// List<Status> statuses = twitter.getUserTimeline("TlkngMchns");

		// System.out.println("statuses : " + statuses.size());
		//
		// for(Status status : statuses) {
		// System.out.println("status : " + status.getId() + " " +
		// status.getCreatedAt());
		// }
		//
		// Paging paging = new Paging(1,400).maxId(647712548406591488L);
		//
		// statuses = twitter.getUserTimeline("TlkngMchns", paging);
		// System.out.println("second page statuses : " + statuses.size());
		// for(Status status : statuses) {
		// System.out.println("status : " + status.getId() + " " +
		// status.getCreatedAt());
		// }
	}

	public static void batchRequests() {
		System.out.println("getting access token");
		AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(APP_ID, APP_SECRET);
		System.out.println("creating client");
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);

		BatchRequest pageRequest = new BatchRequestBuilder("kengarffauto")
				.parameters(Parameter.with("summary", true),
						Parameter.with("fields",
								"about,affiliation,app_id,attire,best_page,built,can_checkin,category,category_list,"
										+ "checkins,company_overview,contact_address,culinary_team,current_location,description,display_subtext,"
										+ "emails,fan_count,features,food_styles,founded,general_info,general_manager,global_brand_root_id,"
										+ "hours,impressum, is_always_open,is_community_page,is_permanently_closed,is_unclaimed,is_verified,link,"
										+ "location,members,mission,mpg,name, parent_page,parking,payment_options, personal_info, personal_interests,"
										+ "pharma_safety_info,phone,place_type,price_range, products,public_transit,restaurant_services,"
										+ "restaurant_specialties,single_line_address,start_info,store_location_descriptor,store_number,"
										+ "talking_about_count,username,verification_status,voip_info,website,were_here_count"))
				.build();

		BatchRequest feedRequest = new BatchRequestBuilder("kengarffauto/feed").parameters(
				Parameter.with("include_hidden", false),
				Parameter.with("fields",
						"application, created_time, caption, feed_targeting,from{id}, is_hidden, link, message, message_tags, object_id, parent_id,"
								+ "place, privacy, shares, source, status_type, story, targeting,to{id},type, updated_time,"
								+ "with_tags, likes.limit(0).summary(true), " + "comments.limit(0).summary(true)"))
				.build();

		List<BatchResponse> batchResponses = facebookClient.executeBatch(pageRequest, feedRequest);

		// Responses are ordered to match up with their corresponding requests.

		BatchResponse pageResponse = batchResponses.get(0);
		BatchResponse feedResponse = batchResponses.get(1);

		if (pageResponse.getCode() != 200)
			System.out.println("Batch request failed: " + pageResponse);
		else {
			JsonMapper jsonMapper = new DefaultJsonMapper();
			Page page = jsonMapper.toJavaObject(pageResponse.getBody(), Page.class);
			System.out.println("page : " + page);

			Connection<Post> feed = new Connection<Post>(facebookClient, feedResponse.getBody(), Post.class);
			System.out.println("feed : " + feed.getData().size());
		}
	}

	public static void fetchPosts() {
		FBPage fbPage = fetchPage();

		System.out.println("getting access token");
		AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(APP_ID, APP_SECRET);
		System.out.println("creating client");
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
		System.out.println("creating connection");
		String url = fbPage.getFbId() + "/feed";
		System.out.println("url : " + url);
		Connection<Post> feed = facebookClient.fetchConnection(url, Post.class, Parameter.with("include_hidden", false),
				Parameter.with("fields",
						"application, created_time, caption, feed_targeting,from{id}, is_hidden, link, message, message_tags, object_id, parent_id,"
								+ "place, privacy, shares, source, status_type, story, targeting,to{id},type, updated_time,"
								+ "with_tags, likes.limit(0).summary(true), " + "comments.limit(0).summary(true)"));

		int index = 0;
		System.out.println("getting data");
		List<Post> posts = feed.getData();
		System.out.println("Got data");
		System.out.println("posts : " + posts.size());
		for (Post post : posts) {
			Comments commentsObject;
			if ((commentsObject = post.getComments()) == null) {
				System.out.println("no comments");
			} else {
				System.out.println("num comments : " + post.getCommentsCount());
				List<Comment> comments = commentsObject.getData();
				int commentIndex = 0;
				for (Comment comment : comments) {
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
			fbPost.setFeedTargeting(post.getFeedTargeting() + "");
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
		Page page = facebookClient.fetchObject("kengarffauto", Page.class, Parameter.with("summary", true), Parameter
				.with("fields", "about,affiliation,app_id,attire,best_page,built,can_checkin,category,category_list,"
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
		if (page.getBestPage() != null) {
			fbPage.setBestPage(page.getBestPage().getId());
		}
		fbPage.setBuilt(page.getBuilt());
		fbPage.setCanCheckin(page.getCanCheckin());
		fbPage.setCategory(page.getCategory());
		if (page.getCategoryList() != null) {
			List<String> categories = new ArrayList<String>();
			for (Category category : page.getCategoryList()) {
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
		if (page.getEmails() != null) {
			fbPage.setEmails(page.getEmails());
		}
		fbPage.setLikes(page.getFanCount());
		fbPage.setFeatures(page.getFeatures());
		if (page.getFoodStyles() != null) {
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
		if (page.getStartInfo() != null) {
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
		System.out.println("page likes : " + page.getDisplaySubtext());
		return fbPage;
	}

	public static void fbClientTesting() throws Exception {
		try {
			System.out.println("Running experiment");
			ScopeBuilder scopeBuilder = new ScopeBuilder();
			scopeBuilder.addPermission(UserDataPermissions.USER_POSTS);

			AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(APP_ID, APP_SECRET);
			FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
			// Page page = facebookClient.fetchObject("kengarffauto",
			// Page.class, Parameter.with("summary",true),
			// Parameter.with("fields", "phone,fan_count"));
			// System.out.println("page : " + page.getPhone());
			// System.out.println("page likes : " + page.getFanCount());

			Connection<Post> feed = facebookClient.fetchConnection("kengarffauto/feed", Post.class,
					Parameter.with("include_hidden", false),
					Parameter.with("fields", "created_time,from, to, likes.limit(0).summary(true), comments"));
			int index = 0;
			List<Post> posts = feed.getData();
			for (Post post : posts) {
				System.out.println("post : " + ++index + post.getMessage());
				System.out.println("likes : " + post.getLikesCount());
				System.out.println("date : " + post.getCreatedTime());
				System.out.println("from : " + post.getFrom());
				// Comments commentsObject;
				// if((commentsObject = post.getComments()) == null){
				// System.out.println("no comments");
				// }
				// else {
				// List<Comment> comments = commentsObject.getData();
				// int commentIndex = 0;
				// for(Comment comment : comments) {
				// System.out.println("comment : " + ++commentIndex +
				// comment.getMessage());
				// }
				// }
			}

		} catch (Exception e) {
			System.out.println("type : " + e.getClass().getSimpleName());
			System.out.println("message : " + e.getMessage());

		}
	}
}
