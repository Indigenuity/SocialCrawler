package fb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
@Entity
public class DatedFeedFetch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long datedFeedFetchId;
	
//	@ManyToOne
//	private FBPage fbPage;
	
	private DateGranularity dateGranularity;
	
	private Date firstDate;
	private Date lastDate;
	private Date currentDate;
	
	private Boolean pastToPresent = true;
	
	@Column(nullable = true, columnDefinition="varchar(4000)")
	private String errorMessage;
	
	@OneToMany
	private List<FeedPage> feedPages = new ArrayList<FeedPage>();
	
	public DatedFeedFetch() {
		Calendar c = Calendar.getInstance();
		this.lastDate = c.getTime();
		c.set(2005, 1, 1);
		this.firstDate = c.getTime();
		this.currentDate = this.firstDate;
	}
	
	
	public long getDatedFeedFetchId() {
		return datedFeedFetchId;
	}


	public void setDatedFeedFetchId(long datedFeedFetchId) {
		this.datedFeedFetchId = datedFeedFetchId;
	}


//	public FBPage getFbPage() {
//		return fbPage;
//	}
//
//
//	public void setFbPage(FBPage fbPage) {
//		this.fbPage = fbPage;
//	}


	public DateGranularity getDateGranularity() {
		return dateGranularity;
	}


	public void setDateGranularity(DateGranularity dateGranularity) {
		this.dateGranularity = dateGranularity;
	}


	public Date getFirstDate() {
		return firstDate;
	}


	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}


	public Date getLastDate() {
		return lastDate;
	}


	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}


	public Date getCurrentDate() {
		if(currentDate == null){
			return firstDate;
		}
		return currentDate;
	}


	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public List<FeedPage> getFeedPages() {
		return feedPages;
	}


	public void setFeedPages(List<FeedPage> feedPages) {
		this.feedPages = feedPages;
	}


	public Boolean getPastToPresent() {
		return pastToPresent;
	}


	public void setPastToPresent(Boolean pastToPresent) {
		this.pastToPresent = pastToPresent;
	}


	public enum DateGranularity {
		YEAR(365), MONTH(30), WEEK(7), DAY(1);
		
		private int numDays;
		
		private DateGranularity(int numDays) {
			this.numDays = numDays;
		}
		
		public int getNumDays(){
			return numDays;
		}
	}
}
