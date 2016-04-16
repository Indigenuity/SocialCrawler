package experiment;

import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.scope.ScopeBuilder;
import com.restfb.scope.UserDataPermissions;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Comment;
import com.restfb.types.Comments;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;

import persistence.Test;
import play.db.jpa.JPA;

public class Experiment {
	
	private static final String APP_SECRET = "17b49f9d44934c08902965568727117e";
	private static final String APP_ID = "847051758681799";
	private static final String REDIRECT = "https://www.facebook.com/connect/login_success.html";
	private static final String TEMP_USER_ACCESS = "AQBhzmEw3hmKQ_ffKvyVxI1GIdTop6-epXaeGIwRQzkR54VTfSJkJ8cNBwWn_jKqAbIi00bSQGyaGoC84dT4Nl2DXM6B04X6Ypt_ifeuenuf0UPVse4lqbFBKD86ZQ6dmvnL_T3Pvf4iTL9OytEJGQklKhR3lkv28EdYpdhJOBHszq4FM8qc28BRrjCUpnO-i3dk6kZyFVt9-k1kHsmxg-g1blfYWOmZShlCKvXYk-bOqbqJiu6D0Q_qroof8_yStP1IWa5HFnIswBKdhC6FmimmvrCGxGNUlv6qRVczTnhIkaUBN5vrh4vCmeUF1Cr2VcY";

	public static void runExperiment() {
		Test test = new Test();
		JPA.em().persist(test);
		System.out.println("id : " + test.testId);
	}
	
	public static void fbClientTesting() throws Exception {
		try{
			System.out.println("Running experiment");
			ScopeBuilder scopeBuilder = new ScopeBuilder();
			scopeBuilder.addPermission(UserDataPermissions.USER_POSTS);
			
			
			AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(APP_ID, APP_SECRET);
			FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
			//Page page = facebookClient.fetchObject("kengarffauto",  Page.class, Parameter.with("summary",true), Parameter.with("fields", "phone,fan_count"));
//			System.out.println("page : " + page.getPhone());
//			System.out.println("page likes : "  + page.getFanCount());
			
			
			Connection<Post> feed = facebookClient.fetchConnection("kengarffauto/feed", Post.class, Parameter.with("include_hidden", true), Parameter.with("fields",  "created_time,from, to, likes.limit(0).summary(true), comments"));
			int index = 0;
			List<Post> posts = feed.getData();
			for(Post post : posts) {
				System.out.println("post : "  + ++index + post.getMessage());
				System.out.println("likes : " + post.getLikesCount());
				System.out.println("date : " + post.getCreatedTime()); 
				System.out.println("from : " + post.getFrom());
//				Comments commentsObject;
//				if((commentsObject = post.getComments()) == null){
//					System.out.println("no comments");
//				}
//				else {
//					List<Comment> comments = commentsObject.getData();
//					int commentIndex = 0;
//					for(Comment comment : comments) {
//						System.out.println("comment : " + ++commentIndex + comment.getMessage());
//					}
//				}
			}
			
			
		}
		catch(Exception e){
			System.out.println("type : " + e.getClass().getSimpleName());
			System.out.println("message : " + e.getMessage());
			
		}
	}
} 
