package fb;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;

import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

@SuppressWarnings("deprecation")
public class FBdao {
	
//	private static final String PERSISTENCE_UNIT_NAME = "defaultPersistenceUnit";
//	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//	private static EntityManager em = factory.createEntityManager();
	
	
	
	public static boolean alreadyPresent(String givenUrl){
			
		List<FBPage> results = JPA.em().createQuery("from FBPage p where p.givenUrl = :givenUrl", FBPage.class).setParameter("givenUrl", givenUrl).getResultList();
		if(results.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static FeedFetch getFeedFetch(FBPage fbPage, FeedType feedType){
		String query = "from FeedFetch ff where ff.fbPage = :fbPage and ff.feedType = :feedType";
		List<FeedFetch> results = JPA.em().createQuery(query, FeedFetch.class).setParameter("fbPage", fbPage)
				.setParameter("feedType", feedType).getResultList();
		if(results.size() > 0){
			return results.get(0);
		}
		return null;
	}
	
	
	public static void persistPage(FBPage fbPage) {
		
		JPA.em().persist(fbPage);
		
		JPA.em().getTransaction().commit();
		JPA.em().getTransaction().begin();
	}
	
}
