package persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import twitter.TweetType;
import utils.Utils;

@Entity
@Table(indexes = {@Index(name = "given_url_index",  columnList="givenUrl", unique = false),
           @Index(name = "twitter_id_index", columnList="id",     unique = false)})
public class TwitterUser {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long twitterUserId;
	
	private String givenUrl;
	private String companyString;
	private Boolean job;
	private Boolean liTab;
	private String geoSpecial;
	
	private Date createdAt;
	private Boolean defaultProfile;
	private Boolean defaultProfileImage;
	private String description;
	private Integer favouritesCount;
	private Integer followersCount;
	private Boolean geoEnabled;
	private Long id;
	private String idString;
	private Boolean isTranslator;
	private String lang;
	private String location;
	private String name;
	private String profileBanner;
	private String profileImage;
	private Boolean protectedProfile;
	private String screenName;
	private Integer statusCount;
	private String timezone;
	private String url;
	private Integer utcOffset;
	private String withheldInCountries;
	private String withheldScope;
	
	
	
	
	
	@OneToMany(mappedBy="twitterUser")
	private List<Tweet> tweets = new ArrayList<Tweet>();
	
	@OneToMany
	private List<Tweet> mentions = new ArrayList<Tweet>();
	
	
	public void addTweet(Tweet tweet, TweetType tweetType){
		if(tweetType == TweetType.MENTION){
			mentions.add(tweet);
		}
		else {
			tweets.add(tweet);
		}
	}
	
	public long getTwitterUserId() {
		return twitterUserId;
	}
	public void setTwitterUserId(long twitterUserId) {
		this.twitterUserId = twitterUserId;
	}
	public String getGivenUrl() {
		return givenUrl;
	}
	public void setGivenUrl(String givenUrl) {
		this.givenUrl = givenUrl;
	}
	public String getCompanyString() {
		return companyString;
	}
	public void setCompanyString(String companyString) {
		this.companyString = companyString;
	}
	public Boolean getJob() {
		return job;
	}
	public void setJob(Boolean job) {
		this.job = job;
	}
	public Boolean getLiTab() {
		return liTab;
	}
	public void setLiTab(Boolean liTab) {
		this.liTab = liTab;
	}
	public String getGeoSpecial() {
		return geoSpecial;
	}
	public void setGeoSpecial(String geoSpecial) {
		this.geoSpecial = Utils.sanitize(geoSpecial, 255);
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Boolean getDefaultProfile() {
		return defaultProfile;
	}
	public void setDefaultProfile(Boolean defaultProfile) {
		this.defaultProfile = defaultProfile;
	}
	public Boolean getDefaultProfileImage() {
		return defaultProfileImage;
	}
	public void setDefaultProfileImage(Boolean defaultProfileImage) {
		this.defaultProfileImage = defaultProfileImage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = Utils.sanitize(description, 255);
	}
	public Integer getFavouritesCount() {
		return favouritesCount;
	}
	public void setFavouritesCount(Integer favouritesCount) {
		this.favouritesCount = favouritesCount;
	}
	public Integer getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(Integer followersCount) {
		this.followersCount = followersCount;
	}
	public Boolean getGeoEnabled() {
		return geoEnabled;
	}
	public void setGeoEnabled(Boolean geoEnabled) {
		this.geoEnabled = geoEnabled;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = Utils.sanitize(idString, 255);
	}
	public Boolean getIsTranslator() {
		return isTranslator;
	}
	public void setIsTranslator(Boolean isTranslator) {
		this.isTranslator = isTranslator;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = Utils.sanitize(lang, 255);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = Utils.sanitize(location, 255);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = Utils.sanitize(name, 255);
	}
	public String getProfileBanner() {
		return profileBanner;
	}
	public void setProfileBanner(String profileBanner) {
		this.profileBanner = Utils.sanitize(profileBanner, 255);
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = Utils.sanitize(profileImage, 255);
	}
	public Boolean getProtectedProfile() {
		return protectedProfile;
	}
	public void setProtectedProfile(Boolean protectedProfile) {
		this.protectedProfile = protectedProfile;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = Utils.sanitize(screenName, 255);
	}
	public Integer getStatusCount() {
		return statusCount;
	}
	public void setStatusCount(Integer statusCount) {
		this.statusCount = statusCount;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = Utils.sanitize(timezone, 255);
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = Utils.sanitize(url, 255);
	}
	public Integer getUtcOffset() {
		return utcOffset;
	}
	public void setUtcOffset(Integer utcOffset) {
		this.utcOffset = utcOffset;
	}
	public String getWithheldInCountries() {
		return withheldInCountries;
	}
	public void setWithheldInCountries(String withheldInCountries) {
		this.withheldInCountries = Utils.sanitize(withheldInCountries, 255);
	}
	public String getWithheldScope() {
		return withheldScope;
	}
	public void setWithheldScope(String withheldScope) {
		this.withheldScope = Utils.sanitize(withheldScope, 255);
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public List<Tweet> getMentions() {
		return mentions;
	}
	
	
	
	
}
