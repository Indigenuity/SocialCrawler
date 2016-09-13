package fb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonObject;

import utils.ThrottledLimiter;

import com.restfb.FacebookClient.AccessToken;
import com.restfb.types.Comments;
import com.restfb.types.Likes;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Page;
import com.restfb.types.Photo;
import com.restfb.types.Post;
import com.restfb.types.Reactions;

public class FB {

	private static FB instance = new FB();

	private static final String APP_SECRET = "17b49f9d44934c08902965568727117e";
	private static final String APP_ID = "847051758681799";
	private static final String USER_ACCESS_TOKEN = "EAAMCY5suvscBAMR2m6PTVpAtw1uQVxxZCuFiOjfQmmrGoMwSfUfACmbO8I7iUhnpP57ZAup5GSacwGEh4WAu2ZBJ0H07hHZCipxiANguYrRnZBafm5ucepTKsd8aDHpp6XB0QiLMc0ZClkYNfNay2tX3MOyyWWhN9i2H3Ot2xVDQZDZD";
	private static final String REDIRECT = "https://www.facebook.com/connect/login_success.html";
	private static final String TEMP_USER_ACCESS = "AQBhzmEw3hmKQ_ffKvyVxI1GIdTop6-epXaeGIwRQzkR54VTfSJkJ8cNBwWn_jKqAbIi00bSQGyaGoC84dT4Nl2DXM6B04X6Ypt_ifeuenuf0UPVse4lqbFBKD86ZQ6dmvnL_T3Pvf4iTL9OytEJGQklKhR3lkv28EdYpdhJOBHszq4FM8qc28BRrjCUpnO-i3dk6kZyFVt9-k1kHsmxg-g1blfYWOmZShlCKvXYk-bOqbqJiu6D0Q_qroof8_yStP1IWa5HFnIswBKdhC6FmimmvrCGxGNUlv6qRVczTnhIkaUBN5vrh4vCmeUF1Cr2VcY";
	
	public static final int POST_LIMIT = 100;

	private static final String PAGE_FIELDS_PARAMETER = "about,affiliation,app_id,attire,best_page,built,can_checkin,category,category_list,"
			+ "checkins,company_overview,contact_address,culinary_team,current_location,description,display_subtext,"
			+ "emails,fan_count,features,food_styles,founded,general_info,general_manager,global_brand_root_id,"
			+ "hours,impressum, is_always_open,is_community_page,is_permanently_closed,is_unclaimed,is_verified,link,"
			+ "location,members,mission,mpg,name, parent_page,parking,payment_options, personal_info, personal_interests,"
			+ "pharma_safety_info,phone,place_type,price_range, products,public_transit,restaurant_services,"
			+ "restaurant_specialties,single_line_address,start_info,store_location_descriptor,store_number,"
			+ "talking_about_count,username,verification_status,voip_info,website,were_here_count";
	
	private static final String FEED_FIELDS_PARAMETER = "application, created_time, caption, feed_targeting,from{id}, is_hidden, link, message, message_tags, object_id, parent_id,"
			+ "place, privacy, shares, source, status_type, story, targeting,to{id},type, updated_time,"
			+ "with_tags, likes.limit(0).summary(true), reactions.limit(0).summary(true), "
			+ "comments.limit(0).summary(true)";
	
	private static final String PHOTOS_FIELDS_PARAMETER = "album, "
			+ "backdated_time, backdated_time_granularity, can_tag, created_time, event, from, height, icon, "
			+ "last_used_time, link, name, name_tags, page_story_id, picture, place, updated_time, width,"
			+ " likes.limit(0).summary(true), comments.limit(0).summary(true), tags.limit(0).summary(true), "
			+ "reactions.limit(0).summary(true), sharedposts.limit(0).summary(true)";
	
	private AccessToken accessToken;
	private FacebookClient client;
	private ThrottledLimiter rateLimiter;
	
	private Boolean initialized = false;
	
	private FB() {
		
	}
	
	private void init(){
		System.out.println("getting access token");
		accessToken = new DefaultFacebookClient(Version.LATEST).obtainAppAccessToken(APP_ID, APP_SECRET);
		System.out.println("creating client");
		client = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_2_6);
		rateLimiter = new ThrottledLimiter(2, 30, TimeUnit.SECONDS);
		initialized = true;
	}
	
	public static FB getInstance() {
		synchronized(instance) {
			if (!instance.initialized) {
				instance.init();
			}
			return instance;
		}
	}
	
	public void holdUp(){
		System.out.println("This is a holdup");
		rateLimiter.rateLimitNotify();
	}
	
	public Page fetchPage(String identifier) {
		rateLimiter.acquire();
		Page page = client.fetchObject(identifier,  Page.class, Parameter.with("summary",true), 
				Parameter.with("fields", PAGE_FIELDS_PARAMETER));
		
		return page;
	}
	
	public void feedExperiment(){
		JsonObject photosConnection = client.fetchObject("kiewit/photos", JsonObject.class, Parameter.with("fields", 
				PHOTOS_FIELDS_PARAMETER));
		System.out.println("photos Connection : " + photosConnection);
		
		
	}
	
	public JsonObject getGenericConnection(String identifier, String after) {
		rateLimiter.acquire();
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(Parameter.with("type", "uploaded"));
		parameters.add(Parameter.with("fields", PHOTOS_FIELDS_PARAMETER));
		if(after != null){
			parameters.add(Parameter.with("after", after));
		}
		return client.fetchObject(identifier + "/photos",JsonObject.class, parameters.toArray(new Parameter[parameters.size()]));
	}
	
	public <T extends NamedFacebookType> Connection<T> getFeedStart(String identifier, Class<T> namedType) {
		rateLimiter.acquire();
		if(namedType == Photo.class){
			return client.fetchConnection(identifier + "/photos", namedType, 
					Parameter.with("type", "uploaded"), Parameter.with("include_hidden", true), Parameter.with("fields", 
					PHOTOS_FIELDS_PARAMETER));
		}
		else {
			return client.fetchConnection(identifier + "/feed", namedType, 
					Parameter.with("limit",  POST_LIMIT));
		}
	}
	
	public <T extends NamedFacebookType> Connection<T> continueFeed(String nextPageUrl, Class<T> namedType) {
		rateLimiter.acquire();
		return client.fetchConnectionPage(nextPageUrl, namedType);
	}
	
	public Connection<Post> getTimedConnection(String identifier, Date since, Date until) {
		rateLimiter.acquire();
		return client.fetchConnection(identifier + "/feed", Post.class, 
				Parameter.with("until", until), Parameter.with("since", since),
				Parameter.with("fields", FEED_FIELDS_PARAMETER),
				Parameter.with("limit",  POST_LIMIT));
		
	}
	
	public Integer getPhotoLikes(String fbId){
		rateLimiter.acquire();
		Connection<Likes> likes = client.fetchConnection("/" + fbId + "/likes", Likes.class);
		Integer count = 0;
		for(List<Likes> likePage : likes){
			count += likePage.size();
			System.out.println("likes count : " + count);
			rateLimiter.acquire();
		}
		System.out.println("likes : " + count);
		return count;
	}
	
	public Integer getPhotoComments(String fbId){
		rateLimiter.acquire();
		Connection<Comments> items = client.fetchConnection("/" + fbId + "/comments", Comments.class);
		Integer count = 0;
		for(List<Comments> itemPage : items){
			count += itemPage.size();
			System.out.println("comments count : " + count);
			rateLimiter.acquire();
		}
		System.out.println("comments : " + count);
		return count;
	}
	
	public Integer getReactions(String fbId){
		rateLimiter.acquire();
		Connection<Reactions> items = client.fetchConnection("/" + fbId + "/reactions", Reactions.class);
		Integer count = 0;
		for(List<Reactions> itemPage : items){
			count += itemPage.size();
			System.out.println("reactions count : " + count);
			rateLimiter.acquire();
		}
		System.out.println("reactions : " + count);
		return count;
	}
	
	
}
