package twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persistence.Tweet;
import persistence.TwitterUser;
import twitter4j.Status;
import twitter4j.TweetEntity;
import twitter4j.User;

public class TwitterParser {

	private static final Pattern IDENTIFIER_REGEX = Pattern.compile("https://twitter\\.com/([^/]+)");
	
	public static String getIdentifier(String givenUrl){
		Matcher matcher = IDENTIFIER_REGEX.matcher(givenUrl);
		if(matcher.find()){
			return matcher.group(1);
		}
		return null;
	}
	
	public static TwitterUser parseUser(User user){
		TwitterUser twitterUser = new TwitterUser();
		twitterUser.setCreatedAt(user.getCreatedAt());
		twitterUser.setDefaultProfile(user.isDefaultProfile());
		twitterUser.setDefaultProfileImage(user.isDefaultProfileImage());
		twitterUser.setDescription(user.getDescription());
		twitterUser.setFavouritesCount(user.getFavouritesCount());
		twitterUser.setFollowersCount(user.getFollowersCount());
		twitterUser.setGeoEnabled(user.isGeoEnabled());
		twitterUser.setId(user.getId());
		twitterUser.setIsTranslator(user.isTranslator());
		twitterUser.setLang(user.getLang());
		twitterUser.setLocation(user.getLocation());
		twitterUser.setName(user.getName());
		twitterUser.setProfileBanner(user.getProfileBannerURL());
		twitterUser.setProfileImage(user.getProfileImageURL());
		twitterUser.setProtectedProfile(user.isProtected());
		twitterUser.setScreenName(user.getScreenName());
		twitterUser.setStatusCount(user.getStatusesCount());
		twitterUser.setTimezone(user.getTimeZone());
		twitterUser.setUrl(user.getURL());
		twitterUser.setUtcOffset(user.getUtcOffset());
		twitterUser.setWithheldInCountries(user.getWithheldInCountries() + "");
		return twitterUser;
	}
	
	public static List<Tweet> parseTweets(List<Status> statuses){
		List<Tweet> tweets = new ArrayList<Tweet>();
		
		for(Status status : statuses){
			Tweet tweet = new Tweet();
			tweet.setContributors(Arrays.toString(status.getContributors()).replace("[", "").replace("]", ""));
			if(status.getGeoLocation() != null){
				tweet.setLatitude(status.getGeoLocation().getLatitude() + "");
				tweet.setLongitude(status.getGeoLocation().getLongitude() + "");
			}
			tweet.setCreatedAt(status.getCreatedAt());
			tweet.setHashtagEntities(parseEntities(status.getHashtagEntities()));
			tweet.setMediaEntities(parseEntities(status.getMediaEntities()));
			tweet.setMentionEntities(parseEntities(status.getUserMentionEntities()));
			tweet.setExtendedMediaEntities(parseEntities(status.getExtendedMediaEntities()));
			tweet.setUrlEntities(parseEntities(status.getURLEntities()));
			tweet.setSymbolEntities(parseEntities(status.getSymbolEntities()));
			tweet.setFavoriteCount(status.getFavoriteCount());
			tweet.setId(status.getId());
			tweet.setInReplyToScreenName(status.getInReplyToScreenName());
			tweet.setInReplyToStatusId(status.getInReplyToStatusId());
			tweet.setInReplyToUserId(status.getInReplyToUserId());
			tweet.setLang(status.getLang());
			if(status.getPlace() != null){
				tweet.setPlaceId(status.getPlace().getId());
			}
			tweet.setQuotedStatusId(status.getQuotedStatusId());
			tweet.setScopes(status.getScopes() + "");
			tweet.setRetweetCount(status.getRetweetCount());
			if(status.getRetweetedStatus() != null){
				tweet.setRetweetedStatus(status.getRetweetedStatus().getId());
			}
			tweet.setSource(status.getSource());
			tweet.setText(status.getText());
			tweet.setTruncated(status.isTruncated());
			tweet.setUserId(status.getUser().getId());
			tweet.setPossiblySensitive(status.isPossiblySensitive());
			tweet.setWithheldInCountries(status.getWithheldInCountries() + "");
			
			tweets.add(tweet);
		}
		
		return tweets;
	}
	
	public  static <T extends TweetEntity> String parseEntities(T[] entities){
		String result = "";
		String delim = "";
		for(TweetEntity entity : entities){
			result += delim + entity.getText();
			delim = ",";
		}
		return result;
	}
}
