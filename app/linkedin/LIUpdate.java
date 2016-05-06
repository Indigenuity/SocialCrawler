package linkedin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import utils.Utils;

@Entity
public class LIUpdate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long liPageId; 
	
	@ManyToOne
	LIPage liPage;
	
	private String liId;
	private String liUrl;
	private String likes;
	private String comments;
	private String timeAgo;
	private String body;
	private String shareTitle;
	@Column(columnDefinition="varchar(4000)")
	private String shareTitleLink;
	@Column(columnDefinition="varchar(4000)")
	private String shareImageLink;
	
	
	
	
	public long getLiPageId() {
		return liPageId;
	}
	public void setLiPageId(long liPageId) {
		this.liPageId = liPageId;
	}
	public LIPage getLiPage() {
		return liPage;
	}
	public void setLiPage(LIPage liPage) {
		this.liPage = liPage;
	}
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = Utils.sanitize(likes, 255);
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = Utils.sanitize(comments, 255);
	}
	public String getTimeAgo() {
		return timeAgo;
	}
	public void setTimeAgo(String timeAgo) {
		this.timeAgo = Utils.sanitize(timeAgo, 255);
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = Utils.sanitize(body, 255);
	}
	public String getShareTitle() {
		return shareTitle;
	}
	public void setShareTitle(String shareTitle) {
		this.shareTitle = Utils.sanitize(shareTitle, 255);
	}
	public String getShareTitleLink() {
		return shareTitleLink;
	}
	public void setShareTitleLink(String shareTitleLink) {
		this.shareTitleLink = Utils.sanitize(shareTitleLink, 4000);
	}
	public String getShareImageLink() {
		return shareImageLink;
	}
	public void setShareImageLink(String shareImageLink) {
		this.shareImageLink = Utils.sanitize(shareImageLink, 4000);
	}
	public String getLiId() {
		return liId;
	}
	public void setLiId(String liId) {
		this.liId = Utils.sanitize(liId, 255);
	}
	public String getLiUrl() {
		return liUrl;
	}
	public void setLiUrl(String liUrl) {
		this.liUrl = Utils.sanitize(liUrl, 255);
	}
	
	
}
