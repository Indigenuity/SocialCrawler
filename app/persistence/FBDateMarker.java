package persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FBDateMarker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long fbDateMarkerId;
	
	private String fbId;
	private String timestamp;
	private Date creationDate;
	private Long fbIdLong;
	private Long scaledId;
	
	public long getFbDateMarkerId() {
		return fbDateMarkerId;
	}
	public void setFbDateMarkerId(long fbDateMarkerId) {
		this.fbDateMarkerId = fbDateMarkerId;
	}
	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Long getFbIdLong() {
		return fbIdLong;
	}
	public void setFbIdLong(Long fbIdLong) {
		this.fbIdLong = fbIdLong;
	}
	public Long getScaledId() {
		return scaledId;
	}
	public void setScaledId(Long scaledId) {
		this.scaledId = scaledId;
	}
	
	
}
