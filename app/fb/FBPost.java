package fb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;

import utils.Utils;

@Entity
public class FBPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long fbPostId;
	
	private String id;
	private String callToAction;
	private String app;
	private String createdTime;
	private String caption;
	private String feedTargeting;
	private String fromId;
	private Boolean isHidden;
	private String link;
	@Column(nullable = true, columnDefinition="varchar(2000)")
	private String message;
	private String messageTags;
	private String objectId;
	private String parentId;
	private String place;
	private String privacy;
	private Long shares;
	private String source;
	private String statusType;
	private String story;
	private String targeting;
	private String toId;
	private String type;
	private String updatedTime;
	private String withTags;
	
	private Long likesCount;
	private Long commentsCount;
	private Long reactionsCount;
	
	private Date realCreatedDate;
	private Date realLastUpdated;
	
	private String likesText;
	private String sharesText;
	private String fromText;
	private String commentsText;
	private String addedText;
	
	private String rawHtml;
	
	
	@ManyToOne
	private FBPage fbPage;
	
	
	public long getFbPostId() {
		return fbPostId;
	}
	public void setFbPostId(long fbPostId) {
		this.fbPostId = fbPostId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = Utils.sanitize(id, 255);
	}
	public String getCallToAction() {
		return callToAction;
	}
	public void setCallToAction(String callToAction) {
		this.callToAction = Utils.sanitize(callToAction, 255);
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = Utils.sanitize(app, 255);
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = Utils.sanitize(createdTime, 255);
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = Utils.sanitize(caption, 255);
	}
	public String getFeedTargeting() {
		return feedTargeting;
	}
	public void setFeedTargeting(String feedTargeting) {
		this.feedTargeting = Utils.sanitize(feedTargeting, 255);
	}
	public Boolean getIsHidden() {
		return isHidden;
	}
	
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = Utils.sanitize(fromId, 255);
	}
	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = Utils.sanitize(link, 255);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = Utils.sanitize(message, 2000);
	}
	public String getMessageTags() {
		return messageTags;
	}
	public void setMessageTags(String messageTags) {
		this.messageTags = Utils.sanitize(messageTags, 255);
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = Utils.sanitize(objectId, 255);
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = Utils.sanitize(parentId, 255);
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = Utils.sanitize(place, 255);
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = Utils.sanitize(privacy, 255);
	}
	public Long getShares() {
		return shares;
	}
	public void setShares(Long shares) {
		this.shares = shares;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = Utils.sanitize(source, 255);
	}
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = Utils.sanitize(statusType, 255);
	}
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = Utils.sanitize(story, 255);
	}
	public String getTargeting() {
		return targeting;
	}
	public void setTargeting(String targeting) {
		this.targeting = Utils.sanitize(targeting, 255);
	}
	
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = Utils.sanitize(toId, 255);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = Utils.sanitize(type, 255); 
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = Utils.sanitize(updatedTime, 255);
	}
	public String getWithTags() {
		return withTags;
	}
	public void setWithTags(String withTags) {
		this.withTags = Utils.sanitize(withTags, 255);
	}
	public Long getLikesCount() {
		return likesCount;
	}
	public void setLikesCount(Long likesCount) {
		this.likesCount = likesCount;
	}
	public Long getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(Long commentsCount) {
		this.commentsCount = commentsCount;
	}
	public Long getReactionsCount() {
		return reactionsCount;
	}
	public void setReactionsCount(Long reactionsCount) {
		this.reactionsCount = reactionsCount;
	}
	public FBPage getFbPage() {
		return fbPage;
	}
	public void setFbPage(FBPage fbPage) {
		this.fbPage = fbPage;
	}
	public Date getRealCreatedDate() {
		return realCreatedDate;
	}
	public void setRealCreatedDate(Date realCreatedDate) {
		this.realCreatedDate = realCreatedDate;
	}
	public Date getRealLastUpdated() {
		return realLastUpdated;
	}
	public void setRealLastUpdated(Date realLastUpdated) {
		this.realLastUpdated = realLastUpdated;
	}
	public String getLikesText() {
		return likesText;
	}
	public void setLikesText(String likesText) {
		this.likesText = likesText;
	}
	public String getSharesText() {
		return sharesText;
	}
	public void setSharesText(String sharesText) {
		this.sharesText = sharesText;
	}
	public String getFromText() {
		return fromText;
	}
	public void setFromText(String fromText) {
		this.fromText = fromText;
	}
	public String getCommentsText() {
		return commentsText;
	}
	public void setCommentsText(String commentsText) {
		this.commentsText = commentsText;
	}
	public String getAddedText() {
		return addedText;
	}
	public void setAddedText(String addedText) {
		this.addedText = addedText;
	}
	
	
}
