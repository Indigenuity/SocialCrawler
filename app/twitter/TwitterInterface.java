package twitter;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import persistence.TwitterUser;
import play.db.jpa.JPA;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import utils.ThrottledLimiter;

public class TwitterInterface {

	private static final ConfigurationBuilder cb;
	private static final TwitterFactory tf;
	private static final Twitter twitter;
	private static final ThrottledLimiter rateLimiter;
	static{
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("4PZxEOm6whMFjBc7Bi9XDA")
		  .setOAuthConsumerSecret("TJntYfhSNjDAN997BWtVYbw74AXwIxc6x9EYD7VcMok")
		  .setOAuthAccessToken("381118490-zArlTcjR1JHlgrhOMUH6RowcaUXwOKF6cuAVGyA3")
		  .setOAuthAccessTokenSecret("lgzUKc6IDtt6afR1dmDzlm7l1Rpp1RUf2froBfrzw");
		tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		rateLimiter = new ThrottledLimiter(.17, 60, TimeUnit.SECONDS);
	}
	
	public static List<Status> startTimeline(String identifier) throws TwitterException{
		List<Status> statuses = twitter.getUserTimeline(identifier);
		return statuses;
	}
	
	public static User fetchUser(String identifier) throws TwitterException{
		rateLimiter.acquire();
		List<User> users = twitter.lookupUsers(identifier);
		if(users.size() == 1){
			return users.get(0);
		} else if(users.size() == 0){
			return null;
		} else {
			throw new IllegalStateException("Found more than one twitter user");
		}
	}
	
	public static List<Status> fetchTimelineStart(String identifier) throws TwitterException{
		rateLimiter.acquire();
		List<Status> statuses = twitter.getUserTimeline(identifier);
		return statuses;
	}
	
	public static List<Status> fetchTimelinePage(String identifier, Long maxId) throws TwitterException{
		rateLimiter.acquire();
		Paging paging = new Paging(1,200).maxId(maxId);
		List<Status> statuses = twitter.getUserTimeline(identifier, paging);
		return statuses;
	}
	
	public static List<Status> fetchTimelinePage(Long id, Long maxId) throws TwitterException{
		rateLimiter.acquire();
		if(maxId == null){
			return twitter.getUserTimeline(id);
		}
		Paging paging = new Paging(1,200).maxId(maxId);
		return twitter.getUserTimeline(id, paging);
	}
	
	public static List<Status> fetchSearch(String search, Long maxId) throws TwitterException {
		rateLimiter.acquire();
		Query query = new Query(search).count(200);
		if(maxId != null){
			query.setMaxId(maxId);
		} 
		return twitter.search(query).getTweets();
	}
	
	public static void holdUp(){
		System.out.println("This is a holdup");
		rateLimiter.rateLimitNotify();
	}
}
