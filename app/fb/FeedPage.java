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
import javax.persistence.OneToOne;

@Entity
public class FeedPage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long feedPageId;
	
	@ManyToOne
	private DatedFeedFetch feedFetch;
	
	private Date firstDate;
	private Date lastDate;
	
	private int numPosts;
	private int maxPosts;
	
	private Boolean zoomed = false;
	
	@Column(nullable = true, columnDefinition="varchar(4000)")
	private String nextPageUrl;
	@Column(nullable = true, columnDefinition="varchar(4000)")
	private String previousPageUrl;
	
	@OneToMany
	private List<DatedFeedFetch> subFetches = new ArrayList<DatedFeedFetch>();
	
	
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
	public Boolean getZoomed() {
		return zoomed;
	}
	public void setZoomed(Boolean zoomed) {
		this.zoomed = zoomed;
	}
	public List<DatedFeedFetch> getSubFetches() {
		return subFetches;
	}
	public void setSubFetches(List<DatedFeedFetch> subFetches) {
		this.subFetches = subFetches;
	}
	public DatedFeedFetch getFeedFetch() {
		return feedFetch;
	}
	public void setFeedFetch(DatedFeedFetch feedFetch) {
		this.feedFetch = feedFetch;
	}
	
	
}
