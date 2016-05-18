package twitter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import persistence.TwitterUser;

@Entity
public class TimelineFetch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long timelineFetchId;
	
	@ManyToOne
	private TwitterUser twitterUser;
	
	@Column(nullable = true, columnDefinition="varchar(4000)")
	private String errorMessage;
	
	private Long earliestTweetId;
	
	private Boolean reachedEnd = false;
	
	@Enumerated(EnumType.STRING)
	private TweetType tweetType;


	public long getTimelineFetchId() {
		return timelineFetchId;
	}

	public void setTimelineFetchId(long timelineFetchId) {
		this.timelineFetchId = timelineFetchId;
	}

	public TwitterUser getTwitterUser() {
		return twitterUser;
	}

	public void setTwitterUser(TwitterUser twitterUser) {
		this.twitterUser = twitterUser;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Long getEarliestTweetId() {
		return earliestTweetId;
	}

	public void setEarliestTweetId(Long earliestTweetId) {
		this.earliestTweetId = earliestTweetId;
	}

	public Boolean getReachedEnd() {
		return reachedEnd;
	}

	public void setReachedEnd(Boolean reachedEnd) {
		this.reachedEnd = reachedEnd;
	}

	public TweetType getTweetType() {
		return tweetType;
	}

	public void setTweetType(TweetType tweetType) {
		this.tweetType = tweetType;
	}
	
	
}
