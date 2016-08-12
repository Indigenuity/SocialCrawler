package fb;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FeedPage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long feedPageId;
	
	private Date firstDate;
	private Date lastDate;
	
	private int numPosts;
	private int maxPosts;
	
	@Column(nullable = true, columnDefinition="varchar(4000)")
	private String nextPageUrl;
	@Column(nullable = true, columnDefinition="varchar(4000)")
	private String previousPageUrl;
	
	
	public long getFeedPageId() {
		return feedPageId;
	}
	public void setFeedPageId(long feedPageId) {
		this.feedPageId = feedPageId;
	}
	public Date getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public int getNumPosts() {
		return numPosts;
	}
	public void setNumPosts(int numPosts) {
		this.numPosts = numPosts;
	}
	public String getNextPageUrl() {
		return nextPageUrl;
	}
	public void setNextPageUrl(String nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}
	public String getPreviousPageUrl() {
		return previousPageUrl;
	}
	public void setPreviousPageUrl(String previousPageUrl) {
		this.previousPageUrl = previousPageUrl;
	}
	public int getMaxPosts() {
		return maxPosts;
	}
	public void setMaxPosts(int maxPosts) {
		this.maxPosts = maxPosts;
	}
	
	
}
