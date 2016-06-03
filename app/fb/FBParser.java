package fb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.restfb.DefaultJsonMapper;
import com.restfb.JsonMapper;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Category;
import com.restfb.types.Comment;
import com.restfb.types.Comments;
import com.restfb.types.Page;
import com.restfb.types.Page.PageStartDate;
import com.restfb.types.Photo;
import com.restfb.types.Post;

import play.db.jpa.JPA;
import utils.DSFormatter;
import utils.Utils;


public class FBParser {
	
	public static final String NOT_A_PAGE = "Not a Page";
	
	public static final String SHARER = "sharer.php";
	public static final String FACEBOOK_LEGAL = "legal/terms";
	public static final String FACEBOOK_PRINCIPLES = "principles.php";
	public static final String FACEBOOK_DIRECTORY = "directory/";
	public static final String LOGIN = "login/";
	public static final String PHP = ".php";
	
	public static final Pattern SIMPLE_URL = Pattern.compile("(?<=facebook.com/)[^/]+$");
	
	public static final DateFormat format = new SimpleDateFormat("EEE MMM FF HH:mm:ss zzz YYYY", Locale.ENGLISH);
	public static final DateFormat markerFormat = new SimpleDateFormat("YYYY-MM-FF", Locale.ENGLISH);
	public static final DateFormat rawFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
	
	
	public static final String PAGES = "pages/";
	private static final JsonMapper jsonMapper = new DefaultJsonMapper();
	
	private static final String ofComment = "[0-9]+ of ([0-9]+)";
	private static final Pattern ofCommentPattern = Pattern.compile(ofComment);
	
	private static final String moreComment = "View ([0-9]+) more comments";
	private static final Pattern moreCommentPattern = Pattern.compile(moreComment);
	
	
	private static final String othersLikes = "[^0-9]+ and ([0-9\\.]+) others";
	private static final Pattern othersLikesPattern = Pattern.compile(othersLikes);
	
	public static Date convertMarkerDate(String dateString) throws ParseException{
		return markerFormat.parse(dateString);
	}
	
	public static void parseTextPost(FBPost fbPost) {
//		Matcher matcher = othersLikesPattern.matcher("Angelina Castellano-O'Leary and 40 others");
//		System.out.println("mmatcher: " + matcher.find());
//		System.out.println("parsing fbPost : " + fbPost);
		parseCommentText(fbPost);
		parseLikeText(fbPost);
		parseSharesText(fbPost);
		parseDateText(fbPost);
	}
	
	public static void parseCommentText(FBPost fbPost) {
		Integer numComments = fbPost.getNumShowingComments();
		if(numComments == null) {
			numComments = 0;
		}
		String commentText = fbPost.getCommentsText();
		
		if(!StringUtils.isEmpty(commentText)){
			Matcher matcher = ofCommentPattern.matcher(commentText);
			if(matcher.find()){
				numComments += Integer.parseInt(matcher.group(1));
			} else {
				matcher = moreCommentPattern.matcher(commentText);
				if(matcher.find()){
					numComments += Integer.parseInt(matcher.group(1));
				}
			}
		}
		fbPost.setCommentsCount(numComments + 0L);
		
	}
	
	public static void parseLikeText(FBPost fbPost) {
		double numLikes = 1;
		String likesText = fbPost.getLikesText();
		if(!StringUtils.isEmpty(likesText)){
			if(likesText.contains("K")){
				numLikes = 1000;
				likesText = likesText.replace("K", "");
			}
			
			Matcher matcher = othersLikesPattern.matcher(likesText);
//			System.out.println("likesText : " + likesText);
			if(matcher.find()){
//				System.out.println("found it");
				numLikes *= Double.parseDouble(matcher.group(1));
			} else {
				numLikes *= Double.parseDouble(likesText);
			}
//			System.out.println("numLikes : " + numLikes);
		}
		fbPost.setLikesCount((long)numLikes);
	}
	
	public static void parseSharesText(FBPost fbPost) {
		long numShares = 0;
		if(!StringUtils.isEmpty(fbPost.getSharesText())){
			numShares= Long.parseLong(fbPost.getSharesText().replace(" shares", "").replace(" share", "").replace(",", ""));
		}
		fbPost.setShares(numShares);
	}
	
	public static void parseDateText(FBPost fbPost) {
		try{
			Date rawDate = rawFormat.parse(fbPost.getCreatedTime());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rawDate);
			calendar.set(Calendar.MINUTE, 1);
			fbPost.setRealCreatedDate(calendar.getTime());
			
		}catch( Exception e){
			System.out.println("bad date");
//			JPA.em().remove(fbPost);
		}
	}
	
	public static List<FBPhoto> parsePhotos(List<Photo> photos){
		System.out.println("photos : " + photos.size());
		List<FBPhoto> fbPhotos = new ArrayList<FBPhoto>();
		for(Photo photo : photos) {
			FBPhoto fbPhoto = parsePhoto(photo);
			fbPhotos.add(fbPhoto); 
		}
		return fbPhotos;
	}
	
	public static List<FBPhoto> parseRawPhotos(JsonArray dataArray){
		List<FBPhoto> fbPhotos = new ArrayList<FBPhoto>();
		for(int i = 0; i < dataArray.length(); i++){
			fbPhotos.add(parseRawPhoto(dataArray.getJsonObject(i)));
		}
		return fbPhotos;
	}
	
	public static FBPhoto parseRawPhoto(JsonObject rawPhoto) {
		Photo photo = jsonMapper.toJavaObject(rawPhoto.toString(), Photo.class);
		FBPhoto fbPhoto = parsePhoto(photo);
		fbPhoto.setLikesCount(rawPhoto.getJsonObject("likes").getJsonObject("summary").getInt("total_count"));
		fbPhoto.setCommentsCount(rawPhoto.getJsonObject("comments").getJsonObject("summary").getInt("total_count"));
		fbPhoto.setReactionsCount(rawPhoto.getJsonObject("reactions").getJsonObject("summary").getInt("total_count"));
		
		return fbPhoto;
	}
	
	public static FBPhoto parsePhoto(Photo photo){
		FBPhoto fbPhoto = new FBPhoto();
		fbPhoto.setId(photo.getId());
		if(photo.getAlbum() != null){ 
			fbPhoto.setAlbumId(photo.getAlbum().getId());
			fbPhoto.setAlbumName(photo.getAlbum().getName());
		}
		fbPhoto.setBackdatedTime(photo.getBackdatedTime());
		fbPhoto.setBackdatedTimeGranularity(photo.getBackdatedTimeGranularity());
		fbPhoto.setCreatedTime(photo.getCreatedTime());
		if(photo.getEvent() != null) {
			fbPhoto.setEventId(photo.getEvent().getId());
		}
		fbPhoto.setFromId(photo.getFrom().getId());
		fbPhoto.setIcon(photo.getIcon());
		fbPhoto.setLink(photo.getLink());
		fbPhoto.setName(photo.getName());
		fbPhoto.setNameTags(photo.getNameTags() + "");
		fbPhoto.setPageStoryId(photo.getPageStoryId());
		fbPhoto.setPicture(photo.getPicture());
		if(photo.getPlace() != null ) {
			fbPhoto.setPlaceId(photo.getPlace().getId());
		}
		fbPhoto.setUpdatedTime(photo.getUpdatedTime());
		fbPhoto.setWidth(photo.getWidth());
		fbPhoto.setHeight(photo.getHeight());
		fbPhoto.setCanTag(photo.getCanTag());
		fbPhoto.setLikesCount(photo.getLikes().size());
		fbPhoto.setCommentsCount(photo.getComments().size());
		fbPhoto.setTagsCount(photo.getTags().size());
		fbPhoto.setReactionsCount(photo.getReactions().getData().size());
		return fbPhoto;
	}
	
	public static List<FBPost> parsePosts(List<Post> posts) {
		System.out.println("posts : " + posts.size());
		List<FBPost> fbPosts = new ArrayList<FBPost>();
		for(Post post : posts) {
			FBPost fbPost = new FBPost();
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
			
			try {
				Date createdDate = format.parse(fbPost.getCreatedTime());
				Date updatedTime = format.parse(fbPost.getUpdatedTime());
				
				fbPost.setRealCreatedDate(createdDate);
				fbPost.setRealLastUpdated(updatedTime);
			} catch (ParseException e) {
				System.out.println("error while parsing date ");
			}
			
			fbPosts.add(fbPost);
		}
		return fbPosts;
	}
	
	public static List<FBPost> parseDomPosts(List<WebElement> elements) {
		List<FBPost> fbPosts = new ArrayList<FBPost>();
		for(WebElement element : elements) {
			FBPost fbPost = new FBPost();
			
			fbPost.setCreatedTime(element.findElement(By.cssSelector(".timestampContent")).getText());
			fbPost.setFromText(element.findElement(By.cssSelector("h5")).getText());
			fbPost.setId(element.findElement(By.name("ft_ent_identifier")).getAttribute("value"));
			try{
				fbPost.setLikesText(element.findElement(By.cssSelector("._4arz")).getText());
			}catch(Exception e){
//				System.out.println("Exception while getting likes");
			}
			
			try{
				List<WebElement> pagerRows = element.findElements(By.cssSelector(".UFIPagerRow"));
				String commentsText = "";
				for(WebElement pagerRow : pagerRows){
					commentsText += pagerRow.getText() + " ";
				}
				fbPost.setCommentsText(commentsText); 
			}catch(Exception e){
//				System.out.println("Exception while getting comments text");
			}
			
			try{
				List<WebElement> visibleComments = element.findElements(By.cssSelector(".UFIComment"));
				fbPost.setNumShowingComments(visibleComments.size());
			}catch(Exception e){
//				System.out.println("Exception while getting comments count");
			}
			
			try{
				fbPost.setSharesText(element.findElement(By.cssSelector(".UFIShareRow")).getText());
			}catch(Exception e){
//				System.out.println("Exception while getting shares");
			}
			
			try{
				fbPost.setMessage(element.findElement(By.cssSelector(".userContent")).getText());
			}catch(Exception e){
//				System.out.println("Exception while getting message");
			}
			
			try{
				WebElement worldIcon = element.findElement(By.cssSelector("._5qla"));
//				System.out.println("found world icon");
				WebElement hoverTextElement = worldIcon.findElement(By.xpath(".."));
//				System.out.println("got parent");
				fbPost.setAddedText(hoverTextElement.getAttribute("data-tooltip-content"));
			} catch(Exception e) {
//				System.out.println("Exception while getting added hover");
			}
			
			System.out.println("createdTime : " + fbPost.getCreatedTime());
//			System.out.println("id : " + fbPost.getId());
//			System.out.println("fromText : " + fbPost.getFromText());
//			System.out.println("likesText : " + fbPost.getLikesText());
//			System.out.println("commentsText : " + fbPost.getCommentsText());
//			System.out.println("sharesText : " + fbPost.getSharesText());
			System.out.println("messageText : " + fbPost.getMessage());
//			System.out.println("hoverText : " + fbPost.getAddedText());
			
//			fbPost.setCaption(post.getCaption());
//			fbPost.setFeedTargeting(post.getFeedTargeting()+"");
//			fbPost.setFromId(post.getFrom().getId());
//			fbPost.setIsHidden(post.getIsHidden());
//			fbPost.setLink(post.getLink());
//			fbPost.setMessage(post.getMessage());
//			fbPost.setMessageTags(post.getMessageTags() + "");
//			fbPost.setObjectId(post.getObjectId());
//			fbPost.setParentId(post.getParentId());
//			fbPost.setPlace(post.getPlace() + "");
//			fbPost.setPrivacy(post.getPrivacy() + "");
//			fbPost.setShares(post.getSharesCount());
//			fbPost.setSource(post.getSource());
//			fbPost.setStatusType(post.getStatusType());
//			fbPost.setStory(post.getStory());
//			fbPost.setTargeting(post.getTargeting() + "");
//			fbPost.setToId(Utils.listToIdCSV(post.getTo()));
//			fbPost.setType(post.getType());
//			fbPost.setUpdatedTime(post.getUpdatedTime() + "");
//			fbPost.setWithTags(post.getWithTags() + "");
//			fbPost.setLikesCount(post.getLikesCount());
//			fbPost.setCommentsCount(post.getCommentsCount());
//			fbPost.setReactionsCount(post.getReactionsCount());
			
//			try {
//				Date createdDate = format.parse(fbPost.getCreatedTime());
//				Date updatedTime = format.parse(fbPost.getUpdatedTime());
//				
//				fbPost.setRealCreatedDate(createdDate);
//				fbPost.setRealLastUpdated(updatedTime);
//			} catch (ParseException e) {
//				System.out.println("error while parsing date ");
//			}
//			
			fbPosts.add(fbPost);
		}
		return fbPosts;
	}
	
	public static FBPage parsePage(Page page) {
		FBPage fbPage = new FBPage();
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
			fbPage.setRealStartDate(parseStartDate(page.getStartInfo().getDate()));
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
	
	public static Date parseStartDate(PageStartDate startDate){ 
		Calendar c = Calendar.getInstance();
		int year = 0;
		int month = 0;
		int day = 0;
		if(startDate == null){
			return null;
		}
		if(startDate.getYear() != null){
			year = startDate.getYear();
		}
		if(startDate.getMonth() != null) {
			month = startDate.getMonth();
		}
		if(startDate.getDay() != null) {
			day = startDate.getDay();
		}
		c.set(year, month, day);
		return c.getTime();
	}
	
	public static String getIdentifier(String url) {
		String identifier = NOT_A_PAGE;
//		url = url.toLowerCase();
		if(url.contains(SHARER) || url.contains(FACEBOOK_LEGAL) || url.contains(FACEBOOK_PRINCIPLES) ||
				url.contains(FACEBOOK_DIRECTORY) || url.contains(LOGIN) || url.contains(PHP)){
			return NOT_A_PAGE;
		}
		
		url = DSFormatter.removeQueryString(url);
		
		
		int lastIndex = url.lastIndexOf("/");
		if(lastIndex >= url.length()){
			url = url.substring(0,lastIndex);
			System.out.println("replaced : " + url);
		}
//		System.out.println("last index : " + lastIndex);
		if(url.contains(PAGES)){
			identifier = DSFormatter.getLastSegment(url);
		}
		else{
			Matcher matcher = SIMPLE_URL.matcher(url);
			if(matcher.find()){
				identifier = matcher.group(0);
			}
		}
		identifier = identifier.trim();
		if(DSFormatter.isEmpty(identifier)){
			return NOT_A_PAGE;
		}
		return identifier;
	}
	
}
