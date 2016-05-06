package linkedin;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.DSFormatter;

public class LIParser {

	public static String trimUrl(String givenUrl){
		return DSFormatter.removeQueryString(givenUrl);
	}
	
	public static Boolean isGroupUrl(String givenUrl){
		return givenUrl.contains("/groups");
	}
	
	public static LIPage parseDocument(LIPage liPage, Document doc){
		String description = getSingle(".basic-info-description", doc);
		String specialties = getSingle(".specialties p", doc);
		String website = getSingle(".website p", doc);
		String industry = getSingle(".industry p", doc);
		String type = getSingle(".type p", doc);
		String headquarters = getSingle(".hq p", doc);
		String title = getSingle("h1", doc);
		String companySize = getSingle(".company-size p", doc);
		String founded = getSingle(".founded p", doc);
		String careers = getSingle("#nav-careers", doc);
		String stats = getSingle(".stats", doc);
		String followers = getSingle(".followers-count", doc);
		String jobs = getSingle(".careers-promo-description .jobs", doc);
		
		String parentCompanyLink = getSingleLink(".parent-company-info .body a", doc);
		String affiliatedCompaniesNames = getMultiple(".affiliated-company-name", doc);
		String affiliatedCompaniesLinks = getMultipleLinks(".affiliated-company-name a", doc);
		String associatedGroupsNames = getMultiple(".associated-groups h4", doc);
		String associatedGroupsLinks = getMultipleLinks(".associated-groups h4 a", doc);
		
		liPage.setDescription(description);
		liPage.setSpecialties(specialties);
		liPage.setWebsite(website);
		liPage.setIndustry(industry);
		liPage.setType(type);
		liPage.setHq(headquarters);
		liPage.setCompanySize(companySize);
		liPage.setFounded(founded);
		liPage.setTitle(title);
		liPage.setCareers(careers);
		liPage.setStats(stats);
		liPage.setFollowers(followers);
		liPage.setJobs(jobs);
		liPage.setParentCompanyLink(parentCompanyLink);
		liPage.setAffiliatedCompaniesNames(affiliatedCompaniesNames);
		liPage.setAffiliatedCompaniesLinks(affiliatedCompaniesLinks);
		liPage.setAssociatedGroupsNames(associatedGroupsNames);
		liPage.setAssociatedGroupsLinks(associatedGroupsLinks);
		
		
//		System.out.println("description : " + description);
//		System.out.println("specialties : " + specialties);
//		System.out.println("website : " + website);
//		System.out.println("industry : " + industry);
//		System.out.println("type : " + type);
//		System.out.println("headquarters : " + headquarters);
//		System.out.println("companySize : " + companySize);
//		System.out.println("founded : " + founded);
//		System.out.println("careers : " + careers);
//		System.out.println("stats : " + stats);
//		System.out.println("followers : " + followers);
//		System.out.println("jobs : " + jobs);
//		System.out.println("affiliatedCompaniesNames : " + affiliatedCompaniesNames);
//		System.out.println("affiliatedCompaniesLinks : " + affiliatedCompaniesLinks);
//		System.out.println("associatedGroupsNames : " + associatedGroupsNames);
//		System.out.println("associatedGroupsLinks : " + associatedGroupsLinks);
		
		return liPage;
	}
	
	public static List<LIUpdate> parseUpdates(Document doc){
			Elements feedItems = doc.select("#my-feed-post .feed-item");
			List<LIUpdate> updates = new ArrayList<LIUpdate>();
			for(Element feedItem : feedItems) {
				LIUpdate liUpdate = parseUpdate(feedItem);
				updates.add(liUpdate);
			}
		return updates;
	}
	
	public static LIUpdate parseUpdate(Element update) {
		String liId = update.attr("data-li-update-id");
		String liUrl = update.attr("data-li-single-update-url");
		String likes = getSingle(".feed-like .like span", update);
		String comments = getSingle(".feed-comment span", update);
		String timeAgo = getSingle(".nus-timestamp", update);
		String body = getSingle(".share-body", update);
		String shareTitle = getSingle(".share-title", update);
		String shareTitleLink = getSingleLink(".share-title", update);
		String shareImageLink = getSingleLink(".share-object a.image", update);
		
		LIUpdate liUpdate = new LIUpdate();
		
		liUpdate.setLiId(liId);
		liUpdate.setLiUrl(liUrl);
		liUpdate.setLikes(likes);
		liUpdate.setComments(comments);
		liUpdate.setTimeAgo(timeAgo);
		liUpdate.setBody(body);
		liUpdate.setShareTitle(shareTitle);
		liUpdate.setShareTitleLink(shareTitleLink);
		liUpdate.setShareImageLink(shareImageLink);
			
		
//		System.out.println("likes : " + likes);
//		System.out.println("comments : " + comments);
//		System.out.println("timeAgo : " + timeAgo);
//		System.out.println("body : " + body);
//		System.out.println("shareTitle : " + shareTitle);
//		System.out.println("shareTitleLink : " + shareTitleLink);
//		System.out.println("shareImageLink : " + shareImageLink);
		
		
		
		return liUpdate;
	}
	
	private static String getSingle(String cssSelector, Element doc){
		try{
			return doc.select(cssSelector).get(0).text();
		} catch(Exception e) {
			return null;
		}
	}
	
	private static String getSingleLink(String cssSelector, Element doc){
		try{
			return doc.select(cssSelector).get(0).attr("href");
		} catch(Exception e) {
			return null;
		}
	}
	
	private static String getMultiple(String cssSelector, Document doc){
		try{
			Elements elements = doc.select(cssSelector);
			String text = "";
			String delim = "";
			for(Element element : elements){
				text += delim + element.text();
				delim = ",";
			}
			return text;
		} catch(Exception e) {
			return null;
		}
	}
	
	private static String getMultipleLinks(String cssSelector, Document doc){
		try{
			Elements elements = doc.select(cssSelector);
			String text = "";
			String delim = "";
			for(Element element : elements){
				text += delim + element.attr("href");
				delim = ",";
			}
			return text;
		} catch(Exception e) {
			return null;
		}
	}
}
