package linkedin;

import java.util.List;

import fb.FeedFetch;
import persistence.FBPage;
import play.db.jpa.JPA;

public class LIdao {
	
	public static boolean alreadyPresent(String givenUrl){
		
		List<LIPage> results = JPA.em().createQuery("from LIPage p where p.givenUrl = :givenUrl", LIPage.class).setParameter("givenUrl", givenUrl).getResultList();
		if(results.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public static void persistPage(LIPage liPage) {
		JPA.em().persist(liPage);
		JPA.em().getTransaction().commit();
		JPA.em().getTransaction().begin();
	}
}
