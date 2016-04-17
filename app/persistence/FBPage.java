package persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

@Entity
public class FBPage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long fbPageId;
	
	@Column(unique=true)
	private String givenUrl;
	private String fbId;
	private String about;
	private String affiliation;
	private String appId;
	private String attire;
	private String bestPage;
	private String built;
	private Boolean canCheckin;
	private String category;
	private Integer checkins;
	private String overview;
	private String contactAddress;
	private String culinaryTeam;
	private String currentLocation;
	private String description;
	private String displaySubtext;
	private String features;
	private String founded;
	private String generalInfo;
	private String generalManager;
	private String globalBrandRootId;
	private String hours;
	private String impressum;
	private String link; 
	private String location;
	private String members;
	private String mission;
	private String mpg;
	private String name;
	private String nameWithBrandOrLocation;
	private String parking;
	private String paymentOptions;
	private String parentPageId;
	private String personalInfo;
	private String personalInterests;
	private String pharmaSafetyInfo;
	private String phone;
	private String placeType;
	private String priceRange;
	private String products;
	private String publicTransit;
	private String restaurantServices;
	private String restaurantSpecialties;
	private String storeLocationDescriptor;
	private String storeNumber;
	private String singleLineAddress;
	private String startDate;
	private String startType;
	private Long talkingAbout;
	private String username;
	private String verificationStatus;
	private String voipInfo;
	private Long wereHere;
	
	@Column(nullable = true, columnDefinition="varchar(4000)")
	private String website;
	private Boolean permanentlyClosed;
	private Boolean verified;
	private Boolean unclaimed;
	private Boolean alwaysOpen;
	private Boolean communityPage;
	private Long likes;
	
	@ElementCollection
	@Column(columnDefinition="varchar(255)")
	private List<String> subCategories = new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER)
	@Column(columnDefinition="varchar(255)")
	private List<String> emails = new ArrayList<String>();
	@ElementCollection(fetch=FetchType.EAGER)
	@Column(columnDefinition="varchar(255)")
	private List<String> foodStyles = new ArrayList<String>();
	
	private String errorCode;
	private String errorMessage;
//	
	private long getFbPageId() {
		return fbPageId;
	}
	private void setFbPageId(long fbPageId) {
		this.fbPageId = fbPageId;
	}
	public List<String> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(List<String> subCategories) {
		this.subCategories.clear();
		this.subCategories.addAll(subCategories);
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails.clear();
		this.emails.addAll(emails);
	}
	public String getGivenUrl() {
		return givenUrl;
	}
	public void setGivenUrl(String givenUrl) {
		this.givenUrl = givenUrl;
	}
	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = StringUtils.abbreviate(about, 255);
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = StringUtils.abbreviate(category, 255);
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview =StringUtils.abbreviate(overview, 255); 
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = StringUtils.abbreviate(contactAddress, 255);
	}
	public String getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = StringUtils.abbreviate(currentLocation, 255);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description =StringUtils.abbreviate(description, 255); 
	}
	public String getFounded() {
		return founded;
	}
	public void setFounded(String founded) {
		this.founded = StringUtils.abbreviate(founded, 255);
	}
	public String getGeneralInfo() {
		return generalInfo;
	}
	public void setGeneralInfo(String generalInfo) {
		this.generalInfo = StringUtils.abbreviate(generalInfo, 255);
	}
	public String getGeneralManager() {
		return generalManager;
	}
	public void setGeneralManager(String generalManager) {
		this.generalManager =StringUtils.abbreviate( generalManager, 255);
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getMission() {
		return mission;
	}
	public void setMission(String mission) {
		this.mission = StringUtils.abbreviate(mission, 255);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = StringUtils.abbreviate(name, 255);
	}
	public String getParentPageId() {
		return parentPageId;
	}
	public void setParentPageId(String parentPageId) {
		this.parentPageId = StringUtils.abbreviate(parentPageId, 255);
	}
	public String getPersonalInfo() {
		return personalInfo;
	}
	public void setPersonalInfo(String personalInfo) {
		this.personalInfo = StringUtils.abbreviate(personalInfo, 255);
	}
	public String getPersonalInterests() {
		return personalInterests;
	}
	public void setPersonalInterests(String personalInterests) {
		this.personalInterests = StringUtils.abbreviate(personalInterests, 255);
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = StringUtils.abbreviate(phone, 255);
	}
	public String getProducts() {
		return products;
	}
	public void setProducts(String products) {
		this.products = StringUtils.abbreviate(products, 255);
	}
	public String getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = StringUtils.abbreviate(storeNumber, 255);
	}
	public String getSingleLineAddress() {
		return singleLineAddress;
	}
	public void setSingleLineAddress(String singleLineAddress) {
		this.singleLineAddress = StringUtils.abbreviate(singleLineAddress, 255);
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = StringUtils.abbreviate(website, 4000);
	}
	public boolean isPermanentlyClosed() {
		return permanentlyClosed;
	}
	public void setPermanentlyClosed(boolean permanentlyClosed) {
		this.permanentlyClosed = permanentlyClosed;
	}
	public boolean isUnclaimed() {
		return unclaimed;
	}
	public void setUnclaimed(boolean unclaimed) {
		this.unclaimed = unclaimed;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public long getLikes() {
		return likes;
	}
	public void setLikes(long likes) {
		this.likes = likes;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = StringUtils.abbreviate(errorCode, 255);
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = StringUtils.abbreviate(errorMessage, 255);
	}
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = StringUtils.abbreviate(affiliation, 255);
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = StringUtils.abbreviate(appId, 255);
	}
	public String getAttire() {
		return attire;
	}
	public void setAttire(String attire) {
		this.attire = StringUtils.abbreviate(attire, 255);
	}
	public String getBestPage() {
		return bestPage;
	}
	public void setBestPage(String bestPage) {
		this.bestPage =StringUtils.abbreviate( bestPage, 255);
	}
	public String getBuilt() {
		return built;
	}
	public void setBuilt(String built) {
		this.built = StringUtils.abbreviate(built, 255);
	}
	public boolean isCanCheckin() {
		return canCheckin;
	}
	public void setCanCheckin(boolean canCheckin) {
		this.canCheckin = canCheckin;
	}
	public String getCulinaryTeam() {
		return culinaryTeam;
	}
	public void setCulinaryTeam(String culinaryTeam) {
		this.culinaryTeam = StringUtils.abbreviate(culinaryTeam, 255);
	}
	public String getDisplaySubtext() {
		return displaySubtext;
	}
	public void setDisplaySubtext(String displaySubtext) {
		this.displaySubtext = StringUtils.abbreviate(displaySubtext, 255);
	}
	public List<String> getFoodStyles() {
		return foodStyles;
	}
	public void setFoodStyles(List<String> foodStyles) {
		this.foodStyles.clear();
		this.foodStyles.addAll(foodStyles);
	}
	public int getCheckins() {
		return checkins;
	}
	public void setCheckins(int checkins) {
		this.checkins = checkins;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = StringUtils.abbreviate(features, 255);
	}
	public String getGlobalBrandRootId() {
		return globalBrandRootId;
	}
	public void setGlobalBrandRootId(String globalBrandRootId) {
		this.globalBrandRootId = StringUtils.abbreviate(globalBrandRootId, 255);
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = StringUtils.abbreviate(hours, 255);
	}
	public String getImpressum() {
		return impressum;
	}
	public void setImpressum(String impressum) {
		this.impressum = StringUtils.abbreviate(impressum, 255);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = StringUtils.abbreviate(location, 255);
	}
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = StringUtils.abbreviate(members, 255);
	}
	public String getMpg() {
		return mpg;
	}
	public void setMpg(String mpg) {
		this.mpg =StringUtils.abbreviate( mpg, 255);
	}
	public String getNameWithBrandOrLocation() {
		return nameWithBrandOrLocation;
	}
	public void setNameWithBrandOrLocation(String nameWithBrandOrLocation) {
		this.nameWithBrandOrLocation = StringUtils.abbreviate(nameWithBrandOrLocation, 255);
	}
	public String getParking() {
		return parking;
	}
	public void setParking(String parking) {
		this.parking = StringUtils.abbreviate(parking, 255);
	}
	public String getPaymentOptions() {
		return paymentOptions;
	}
	public void setPaymentOptions(String paymentOptions) {
		this.paymentOptions = StringUtils.abbreviate(paymentOptions, 255);
	}
	public String getPharmaSafetyInfo() {
		return pharmaSafetyInfo;
	}
	public void setPharmaSafetyInfo(String pharmaSafetyInfo) {
		this.pharmaSafetyInfo = StringUtils.abbreviate(pharmaSafetyInfo, 255);
	}
	public String getPlaceType() {
		return placeType;
	}
	public void setPlaceType(String placeType) {
		this.placeType = StringUtils.abbreviate(placeType, 255);
	}
	public String getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(String priceRange) {
		this.priceRange = StringUtils.abbreviate(priceRange, 255);
	}
	public String getPublicTransit() {
		return publicTransit;
	}
	public void setPublicTransit(String publicTransit) {
		this.publicTransit = StringUtils.abbreviate(publicTransit, 255);
	}
	public String getRestaurantServices() {
		return restaurantServices;
	}
	public void setRestaurantServices(String restaurantServices) {
		this.restaurantServices = StringUtils.abbreviate(restaurantServices, 255);
	}
	public String getRestaurantSpecialties() {
		return restaurantSpecialties;
	}
	public void setRestaurantSpecialties(String restaurantSpecialties) {
		this.restaurantSpecialties =StringUtils.abbreviate( restaurantSpecialties, 255);
	}
	public String getStoreLocationDescriptor() {
		return storeLocationDescriptor;
	}
	public void setStoreLocationDescriptor(String storeLocationDescriptor) {
		this.storeLocationDescriptor =StringUtils.abbreviate( storeLocationDescriptor, 255);
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = StringUtils.abbreviate(startDate, 255);
	}
	public String getStartType() {
		return startType;
	}
	public void setStartType(String startType) {
		this.startType = StringUtils.abbreviate(startType, 255);
	}
	public Long getTalkingAbout() {
		return talkingAbout;
	}
	public void setTalkingAbout(Long talkingAbout) {
		this.talkingAbout = talkingAbout;
	}
	public String getVerificationStatus() {
		return verificationStatus;
	}
	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = StringUtils.abbreviate(verificationStatus, 255);
	}
	public String getVoipInfo() {
		return voipInfo;
	}
	public void setVoipInfo(String voipInfo) {
		this.voipInfo = StringUtils.abbreviate(voipInfo, 255);
	}
	public Long getWereHere() {
		return wereHere;
	}
	public void setWereHere(Long wereHere) {
		this.wereHere = wereHere;
	}
	public boolean isAlwaysOpen() {
		return alwaysOpen;
	}
	public void setAlwaysOpen(boolean alwaysOpen) {
		this.alwaysOpen = alwaysOpen;
	}
	public boolean isCommunityPage() {
		return communityPage;
	}
	public void setCommunityPage(boolean communityPage) {
		this.communityPage = communityPage;
	}
	
	
}
