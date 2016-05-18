package fb;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import utils.Utils;

@Entity
public class FBPhoto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long fbPhotoId;
	
	private String id;
	private String albumId;
	private String albumName;
	private Date backdatedTime;
	private String backdatedTimeGranularity;
	private Date createdTime;
	private String eventId;
	private String fromId;
	private String icon;
	private String link;
	private String name;
	private String nameTags;
	private String pageStoryId;
	private String picture;
	private String placeId;
	private Date updatedTime;
	private Integer width;
	private Integer height;
	private Boolean canTag = false;
	
	private Integer likesCount, commentsCount, tagsCount, reactionsCount, sharedCount;
	
	@ManyToOne
	private FBPage fbPage;

	public long getFbPhotoId() {
		return fbPhotoId;
	}

	public void setFbPhotoId(long fbPhotoId) {
		this.fbPhotoId = fbPhotoId;
	}

	public String getId() {
		return Utils.sanitize(id, 255);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = Utils.sanitize(albumId, 255);
	}
	
	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = Utils.sanitize(albumName, 255);
	}

	public Date getBackdatedTime() {
		return backdatedTime;
	}

	public void setBackdatedTime(Date backdatedTime) {
		this.backdatedTime = backdatedTime;
	}

	public String getBackdatedTimeGranularity() {
		return backdatedTimeGranularity;
	}

	public void setBackdatedTimeGranularity(String backdatedTimeGranularity) {
		this.backdatedTimeGranularity = Utils.sanitize(backdatedTimeGranularity, 255);
	}


	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = Utils.sanitize(eventId, 255);
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = Utils.sanitize(fromId, 255);
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = Utils.sanitize(icon, 255);
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = Utils.sanitize(link, 255);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = Utils.sanitize(name, 255);
	}

	public String getNameTags() {
		return nameTags;
	}

	public void setNameTags(String nameTags) {
		this.nameTags = Utils.sanitize(nameTags, 255);
	}

	public String getPageStoryId() {
		return pageStoryId;
	}

	public void setPageStoryId(String pageStoryId) {
		this.pageStoryId = Utils.sanitize(pageStoryId, 255);
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = Utils.sanitize(picture, 255);
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = Utils.sanitize(placeId, 255);
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Boolean getCanTag() {
		return canTag;
	}

	public void setCanTag(Boolean canTag) {
		this.canTag = canTag;
	}

	public FBPage getFbPage() {
		return fbPage;
	}

	public void setFbPage(FBPage fbPage) {
		this.fbPage = fbPage;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(Integer likesCount) {
		this.likesCount = likesCount;
	}

	public Integer getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Integer commentsCount) {
		this.commentsCount = commentsCount;
	}

	public Integer getTagsCount() {
		return tagsCount;
	}

	public void setTagsCount(Integer tagsCount) {
		this.tagsCount = tagsCount;
	}

	public Integer getReactionsCount() {
		return reactionsCount;
	}

	public void setReactionsCount(Integer reactionsCount) {
		this.reactionsCount = reactionsCount;
	}

	public Integer getSharedCount() {
		return sharedCount;
	}

	public void setSharedCount(Integer sharedCount) {
		this.sharedCount = sharedCount;
	}
	
	

	
}
