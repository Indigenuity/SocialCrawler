package twitter;

import java.util.List;

import fb.FBPage;
import fb.FeedFetch;
import persistence.TwitterUser;
import play.db.jpa.JPA;

public class TwitterDao {

	public static boolean alreadyPresent(String givenUrl) {
		String query = "from TwitterUser tu where tu.givenUrl = :givenUrl";
		List<TwitterUser> results = JPA.em().createQuery(query,TwitterUser.class).setParameter("givenUrl", givenUrl).getResultList();
		if(results.size() > 0){
			return true;
		}
		return false;
	}
	
	public static TimelineFetch getTimelineFetch(TwitterUser twitterUser, TweetType tweetType){
		String query = "from TimelineFetch tf where tf.twitterUser = :twitterUser and tf.tweetType = :tweetType";
		List<TimelineFetch> results = JPA.em().createQuery(query, TimelineFetch.class)
				.setParameter("twitterUser", twitterUser)
				.setParameter("tweetType", tweetType)
				.getResultList();
		if(results.size() > 0){
			return results.get(0);
		}
		return null;
	}
}
