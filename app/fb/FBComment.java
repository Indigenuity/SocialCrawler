package fb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import utils.Utils;

@Entity
public class FBComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long fbCommentId;
	
	private String id;
	private Long commentCount;
	private String createdTime;
	private String fromId;
	private Long likeCount;
	@Column(nullable = true, columnDefinition="varchar(2000)")
	private String message;
	
	
	public long getFbCommentId() {
		return fbCommentId;
	}
	public void setFbCommentId(long fbCommentId) {
		this.fbCommentId = fbCommentId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = Utils.sanitize(id, 255);
	}
	public Long getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = Utils.sanitize(createdTime, 255);
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = Utils.sanitize(fromId, 255);
	}
	public Long getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = Utils.sanitize(message, 2000);
	}
	
	
	
}
