package fb;

import com.restfb.types.NamedFacebookType;
import com.restfb.types.Photo;
import com.restfb.types.Post;

public enum FeedType {

	FEED(Post.class), 
	MANUAL(Post.class),
	PHOTOS(Photo.class);
	
	private Class<? extends NamedFacebookType> feedClass;
	
	private FeedType(Class<?extends NamedFacebookType> feedClass){
		this.feedClass = feedClass;
	}
	
	public Class<? extends NamedFacebookType> getFeedClass() {
		return this.feedClass;
	}
	
}
