package fb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class FeedFetch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long feedFetchId;
	
	@ManyToOne
	private FBPage fbPage;
	
	@Column(nullable = true, columnDefinition="varchar(4000)")
	@ElementCollection(fetch=FetchType.LAZY)
	private List<String> nextPageUrls = new ArrayList<String>();
	
	@Column(nullable = true, columnDefinition="varchar(4000)")
	private String errorMessage;
	
	private String earliestPost;
	
	private Boolean reachedEnd = false;
	
	@Enumerated(EnumType.STRING)
	private FeedType feedType = FeedType.FEED;
	

	public long getFeedFetchId() {
		return feedFetchId;
	}

	public void setFeedFetchId(long feedFetchId) {
		this.feedFetchId = feedFetchId;
	}

	public FBPage getFbPage() {
		return fbPage;
	}

	public void setFbPage(FBPage fbPage) {
		this.fbPage = fbPage;
	}

	public List<String> getNextPageUrls() {
		return nextPageUrls;
	}

	public void addNextPageUrl(String nextPageUrl) {
		this.nextPageUrls.add(nextPageUrl);
	}
	
	public String getNextPageUrl(){
		if(nextPageUrls.size() == 0)
			return null;
		return nextPageUrls.get(nextPageUrls.size() - 1);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getEarliestPost() {
		return earliestPost;
	}

	public void setEarliestPost(String earliestPost) {
		this.earliestPost = earliestPost;
	}

	public Boolean getReachedEnd() {
		return reachedEnd;
	}

	public void setReachedEnd(Boolean reachedEnd) {
		this.reachedEnd = reachedEnd;
	}

	public FeedType getFeedType() {
		return feedType;
	}

	public void setFeedType(FeedType feedType) {
		this.feedType = feedType;
	}
	
	

}
