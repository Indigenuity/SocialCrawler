package twitter;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.restfb.Connection;
import com.restfb.types.Page;
import com.restfb.types.Post;

import fb.FB;
import fb.FBPage;
import fb.FBParser;
import fb.FBPost;
import fb.FBdao;
import fb.FeedFetch;
import persistence.Tweet;
import persistence.TwitterUser;
import play.db.jpa.JPA;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

public class TwitterMaster {

	public static void readAndFetchPages() throws IOException, TwitterException { 
		
		Reader in = new FileReader("./data/in/companies.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
		
		EntityManager em = JPA.em();
		int total = 0;
		for(CSVRecord record : records) {
			String type = record.get("Site (FB/Tw/LI)");
			if(type.toLowerCase().equals("tw")){
				importTwitterPage(record);
			} 
			total++;
			
			System.out.println("Processed : " + total);
		}
		
		in.close();
	}
	
	public static void fetchTimelines(TweetType tweetType){
		
		String query = "from TwitterUser tu";
		
		List<TwitterUser> twitterUsers= JPA.em().createQuery(query, TwitterUser.class).getResultList();
		System.out.println("number of twitterUsers : " + twitterUsers.size());
		for(TwitterUser twitterUser : twitterUsers){
			
			TimelineFetch timelineFetch = TwitterDao.getTimelineFetch(twitterUser, tweetType);
			if(timelineFetch == null){
				System.out.println("was null");
				timelineFetch = new TimelineFetch();
				timelineFetch.setTwitterUser(twitterUser);
				timelineFetch.setTweetType(tweetType);
				JPA.em().persist(timelineFetch);
				JPA.em().getTransaction().commit();
				JPA.em().getTransaction().begin();
			}
			try {
				processTimelineFetch(timelineFetch);
				timelineFetch.setErrorMessage(null);
			}
			catch(Exception e) {
				timelineFetch.setErrorMessage(e.getClass().getSimpleName() + " exception while processing timeline fetch : " + e.getMessage());
				TwitterInterface.holdUp();
				System.out.println("exception : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public static void processTimelineFetch(TimelineFetch timelineFetch) throws TwitterException {
		TwitterUser twitterUser = timelineFetch.getTwitterUser();
		
		if(timelineFetch.getReachedEnd()){
			System.out.println("This timeline fetch has already reached its end");
			return;
		}
		
		do{
			List<Status> statuses = null;
			if(timelineFetch.getTweetType() == TweetType.MENTION){
				statuses = TwitterInterface.fetchSearch("@" + twitterUser.getScreenName(), timelineFetch.getEarliestTweetId());
			} else {
				statuses = TwitterInterface.fetchTimelinePage(twitterUser.getId(), timelineFetch.getEarliestTweetId());
			}
			System.out.println("statuses (" + twitterUser.getScreenName() + ") (" + timelineFetch.getTweetType() + ") : " + statuses.size());
			List<Tweet> tweets = TwitterParser.parseTweets(statuses);
			if(tweets.size() > 1){
				tweets.remove(0);		//Get rid of the first tweet, since we already have it.
				timelineFetch.setEarliestTweetId(tweets.get(tweets.size() - 1).getId());
				System.out.println("earliest (" + twitterUser.getScreenName() + ") (" + timelineFetch.getTweetType() + ") : " + timelineFetch.getEarliestTweetId());
				for(Tweet tweet : tweets){
					if(timelineFetch.getTweetType() == TweetType.MENTION){
						JPA.em().persist(tweet);
						twitterUser.addTweet(tweet, timelineFetch.getTweetType());
					} else {
						tweet.setTwitterUser(twitterUser);
						JPA.em().persist(tweet);
					}
					
					
				}
			} else {
				timelineFetch.setReachedEnd(true);
			}
			
			JPA.em().getTransaction().commit();
			JPA.em().getTransaction().begin();
			
		}while(!timelineFetch.getReachedEnd());
	}
	
	public static void importTwitterPage(CSVRecord record) throws TwitterException{
		String givenUrl = record.get("URL");
		String companyString = record.get("Company");
		Boolean job = Boolean.parseBoolean(record.get("Job/Career (Yes/No)"));
		Boolean liTab = Boolean.parseBoolean(record.get("LI Tab?"));
		String geoSpecial = record.get("Geography specific");
		
		if(TwitterDao.alreadyPresent(givenUrl)){
			return;
		}
		
		String identifier = TwitterParser.getIdentifier(givenUrl);
		System.out.println("identifier : " + identifier);
		if(identifier == null){
			throw new IllegalStateException("null identifier for givenUrl : " + givenUrl);
		}
		User user = TwitterInterface.fetchUser(identifier);
		TwitterUser twitterUser = TwitterParser.parseUser(user);
		twitterUser.setGivenUrl(givenUrl);
		twitterUser.setCompanyString(companyString);
		twitterUser.setJob(job);
		twitterUser.setLiTab(liTab);
		twitterUser.setGeoSpecial(geoSpecial);
		JPA.em().persist(twitterUser);
		JPA.em().getTransaction().commit();
		JPA.em().getTransaction().begin();
	}
}
