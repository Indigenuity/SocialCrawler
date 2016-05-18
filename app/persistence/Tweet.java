package persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import utils.Utils;

@Entity
@Table(indexes = {@Index(name = "reply_index",  columnList="inReplyToUserId", unique = false),
           @Index(name = "tweet_id_index", columnList="id",     unique = false)})
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long tweetId;
	
	@ManyToOne
	private TwitterUser twitterUser;
	
	
	private String contributors;
	private String latitude;
	private String longitude;
	private Date createdAt;
	private String hashtagEntities;
	private String mediaEntities;
	private String extendedMediaEntities;
	private String mentionEntities;
	private String urlEntities;
	private String symbolEntities;
	private Integer favoriteCount;
	private String filterLevel;
	private Long id;
	private String inReplyToScreenName;
	private Long inReplyToStatusId;
	private Long inReplyToUserId;
	private String lang;
	private String placeId;
	private Boolean possiblySensitive;
	private Long quotedStatusId;
	private String scopes;
	private Integer retweetCount;
	private Long retweetedStatus;
	private String source;
	private String text;
	private Boolean truncated;
	private Long userId;
	private Boolean withheldCopyright;
	private String withheldInCountries;
	private String withheldScope;
	
	
	public long getTweetId() {
		return tweetId;
	}
	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}
	public TwitterUser getTwitterUser() {
		return twitterUser;
	}
	public void setTwitterUser(TwitterUser twitterUser) {
		this.twitterUser = twitterUser;
	}
	public String getContributors() {
		return contributors;
	}
	public void setContributors(String contributors) {
		this.contributors = Utils.sanitize(contributors, 255);
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = Utils.sanitize(latitude, 255);
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = Utils.sanitize(longitude, 255);
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(Integer favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public String getFilterLevel() {
		return filterLevel;
	}
	public void setFilterLevel(String filterLevel) {
		this.filterLevel = Utils.sanitize(filterLevel, 255);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}
	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = Utils.sanitize(inReplyToScreenName, 255);
	}
	public Long getInReplyToStatusId() {
		return inReplyToStatusId;
	}
	public void setInReplyToStatusId(Long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}
	public Long getInReplyToUserId() {
		return inReplyToUserId;
	}
	public void setInReplyToUserId(Long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = Utils.sanitize(lang, 255);
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = Utils.sanitize(placeId, 255);
	}
	public Boolean getPossiblySensitive() {
		return possiblySensitive;
	}
	public void setPossiblySensitive(Boolean possiblySensitive) {
		this.possiblySensitive = possiblySensitive;
	}
	public Long getQuotedStatusId() {
		return quotedStatusId;
	}
	public void setQuotedStatusId(Long quotedStatusId) {
		this.quotedStatusId = quotedStatusId;
	}
	public String getScopes() {
		return scopes;
	}
	public void setScopes(String scopes) {
		this.scopes = Utils.sanitize(scopes, 255);
	}
	public Integer getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(Integer retweetCount) {
		this.retweetCount = retweetCount;
	}
	public Long getRetweetedStatus() {
		return retweetedStatus;
	}
	public void setRetweetedStatus(Long retweetedStatus) {
		this.retweetedStatus = retweetedStatus;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = Utils.sanitize(source, 255);
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = Utils.sanitize(text, 255);
	}
	public Boolean getTruncated() {
		return truncated;
	}
	public void setTruncated(Boolean truncated) {
		this.truncated = truncated;
	}
	public Boolean getWithheldCopyright() {
		return withheldCopyright;
	}
	public void setWithheldCopyright(Boolean withheldCopyright) {
		this.withheldCopyright = withheldCopyright;
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
	public String getHashtagEntities() {
		return hashtagEntities;
	}
	public void setHashtagEntities(String hashtagEntities) {
		this.hashtagEntities = Utils.sanitize(hashtagEntities, 255);
	}
	public String getMediaEntities() {
		return mediaEntities;
	}
	public void setMediaEntities(String mediaEntities) {
		this.mediaEntities = Utils.sanitize(mediaEntities, 255);
	}
	public String getExtendedMediaEntities() {
		return extendedMediaEntities;
	}
	public void setExtendedMediaEntities(String extendedMediaEntities) {
		this.extendedMediaEntities = Utils.sanitize(extendedMediaEntities, 255);
	}
	public String getMentionEntities() {
		return mentionEntities;
	}
	public void setMentionEntities(String mentionEntities) {
		this.mentionEntities = Utils.sanitize(mentionEntities, 255);
	}
	public String getUrlEntities() {
		return urlEntities;
	}
	public void setUrlEntities(String urlEntities) {
		this.urlEntities = Utils.sanitize(urlEntities, 255);
	}
	public String getSymbolEntities() {
		return symbolEntities;
	}
	public void setSymbolEntities(String symbolEntities) {
		this.symbolEntities = Utils.sanitize(symbolEntities, 255);
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
}
