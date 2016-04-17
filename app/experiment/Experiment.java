package experiment;

import java.util.ArrayList;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.scope.ScopeBuilder;
import com.restfb.scope.UserDataPermissions;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Category;
import com.restfb.types.Comment;
import com.restfb.types.Comments;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;

import persistence.FBPage;
import persistence.Test;
import play.db.jpa.JPA;

public class Experiment {
	
	private static final String APP_SECRET = "17b49f9d44934c08902965568727117e";
	private static final String APP_ID = "847051758681799";
	private static final String REDIRECT = "https://www.facebook.com/connect/login_success.html";
	private static final String TEMP_USER_ACCESS = "AQBhzmEw3hmKQ_ffKvyVxI1GIdTop6-epXaeGIwRQzkR54VTfSJkJ8cNBwWn_jKqAbIi00bSQGyaGoC84dT4Nl2DXM6B04X6Ypt_ifeuenuf0UPVse4lqbFBKD86ZQ6dmvnL_T3Pvf4iTL9OytEJGQklKhR3lkv28EdYpdhJOBHszq4FM8qc28BRrjCUpnO-i3dk6kZyFVt9-k1kHsmxg-g1blfYWOmZShlCKvXYk-bOqbqJiu6D0Q_qroof8_yStP1IWa5HFnIswBKdhC6FmimmvrCGxGNUlv6qRVczTnhIkaUBN5vrh4vCmeUF1Cr2VcY";

	public static void runExperiment(){
		fetchPage();
	}
	
	public static void fetchPage() {
		AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(APP_ID, APP_SECRET);
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
		Page page = facebookClient.fetchObject("PizzaPieCafe",  Page.class, Parameter.with("summary",true), 
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
		
		JPA.em().persist(fbPage);
		System.out.println("page : " + page.getHours());
		System.out.println("page likes : "  + page.getDisplaySubtext());
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
			
			
			Connection<Post> feed = facebookClient.fetchConnection("kengarffauto/feed", Post.class, Parameter.with("include_hidden", true), Parameter.with("fields",  "created_time,from, to, likes.limit(0).summary(true), comments"));
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
