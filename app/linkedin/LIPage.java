package linkedin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import utils.Utils;

@Entity
public class LIPage { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long liPageId; 
	
	@Column(nullable = true, columnDefinition="varchar(4000)")
	private String givenUrl;
	private String companyString;
	private Boolean job;
	private Boolean liTab;
	private String geoSpecial;
	
	private Boolean isGroup;
	private String trimmedUrl;
	
	
	private String description;
	private String specialties;
	private String website;
	private String industry;
	private String type;
	private String hq;
	private String title;
	private String companySize;
	private String founded;
	private String careers;
	private String stats;
	private String followers;
	private String jobs;
	private String affiliatedCompaniesNames;
	@Column(columnDefinition="varchar(4000)")
	private String affiliatedCompaniesLinks;
	private String associatedGroupsNames;
	@Column(columnDefinition="varchar(4000)")
	private String associatedGroupsLinks;
	
	private String parentCompanyLink;
	
	private Boolean fetchDone = false;
	private String fetchErrorMessage;
	
	@OneToMany(mappedBy="liPage")
	private List<LIUpdate> updates = new ArrayList<LIUpdate>();

	public long getLiPageId() {
		return liPageId;
	}

	public void setLiPageId(long liPageId) {
		this.liPageId = liPageId;
	}

	public String getGivenUrl() {
		return givenUrl;
	}

	public void setGivenUrl(String givenUrl) {
		this.givenUrl = Utils.sanitize(givenUrl, 255);
	}

	public String getCompanyString() {
		return companyString;
	}

	public void setCompanyString(String companyString) {
		this.companyString = Utils.sanitize(companyString, 255);
	}

	public Boolean getJob() {
		return job;
	}

	public void setJob(Boolean job) {
		this.job = job;
	}

	public Boolean getLiTab() {
		return liTab;
	}

	public void setLiTab(Boolean liTab) {
		this.liTab = liTab;
	}

	public String getGeoSpecial() {
		return geoSpecial;
	}

	public void setGeoSpecial(String geoSpecial) {
		this.geoSpecial = Utils.sanitize(geoSpecial, 255);
	}

	public String getTrimmedUrl() {
		return trimmedUrl;
	}

	public void setTrimmedUrl(String trimmedUrl) {
		this.trimmedUrl = Utils.sanitize(trimmedUrl, 255);
	}

	public Boolean getGroup() {
		return isGroup;
	}

	public void setGroup(Boolean group) {
		this.isGroup = group;
	}

	public Boolean getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = Utils.sanitize(description, 255);
	}

	public String getSpecialties() {
		return specialties;
	}

	public void setSpecialties(String specialties) {
		this.specialties = Utils.sanitize(specialties, 255);
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = Utils.sanitize(website, 255);
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = Utils.sanitize(industry, 255);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = Utils.sanitize(type, 255);
	}

	public String getHq() {
		return hq;
	}

	public void setHq(String hq) {
		this.hq = Utils.sanitize(hq, 255);
	}

	public String getCompanySize() {
		return companySize;
	}

	public void setCompanySize(String companySize) {
		this.companySize = Utils.sanitize(companySize, 255);
	}

	public String getFounded() {
		return founded;
	}

	public void setFounded(String founded) {
		this.founded = Utils.sanitize(founded, 255);
	}

	public String getCareers() {
		return careers;
	}

	public void setCareers(String careers) {
		this.careers = Utils.sanitize(careers, 255);
	}

	public String getStats() {
		return stats;
	}

	public void setStats(String stats) {
		this.stats = Utils.sanitize(stats, 255);
	}

	public String getFollowers() {
		return followers;
	}

	public void setFollowers(String followers) {
		this.followers = Utils.sanitize(followers, 255);
	}

	public String getJobs() {
		return jobs;
	}

	public void setJobs(String jobs) {
		this.jobs = Utils.sanitize(jobs, 255);
	}

	public String getAffiliatedCompaniesNames() {
		return affiliatedCompaniesNames;
	}

	public void setAffiliatedCompaniesNames(String affiliatedCompaniesNames) {
		this.affiliatedCompaniesNames = Utils.sanitize(affiliatedCompaniesNames, 255);
	}

	public String getAffiliatedCompaniesLinks() {
		return affiliatedCompaniesLinks;
	}

	public void setAffiliatedCompaniesLinks(String affiliatedCompaniesLinks) {
		this.affiliatedCompaniesLinks = Utils.sanitize(affiliatedCompaniesLinks, 4000);
	}

	public String getAssociatedGroupsNames() {
		return associatedGroupsNames;
	}

	public void setAssociatedGroupsNames(String associatedGroupsNames) {
		this.associatedGroupsNames = Utils.sanitize(associatedGroupsNames, 255);
	}

	public String getAssociatedGroupsLinks() {
		return associatedGroupsLinks;
	}

	public void setAssociatedGroupsLinks(String associatedGroupsLinks) {
		this.associatedGroupsLinks = Utils.sanitize(associatedGroupsLinks, 4000);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = Utils.sanitize(title, 255);
	}

	public List<LIUpdate> getUpdates() {
		return updates;
	}
	
	public void addUpdate(LIUpdate liUpdate) {
		this.updates.add(liUpdate);
	}

	public Boolean getFetchDone() {
		return fetchDone;
	}

	public void setFetchDone(Boolean fetchDone) {
		this.fetchDone = fetchDone;
	}

	public String getFetchErrorMessage() {
		return fetchErrorMessage;
	}

	public void setFetchErrorMessage(String fetchErrorMessage) {
		this.fetchErrorMessage = Utils.sanitize(fetchErrorMessage, 255);
	}

	public String getParentCompanyLink() {
		return parentCompanyLink;
	}

	public void setParentCompanyLink(String parentCompanyLink) {
		this.parentCompanyLink = parentCompanyLink;
	}
	
	
}
