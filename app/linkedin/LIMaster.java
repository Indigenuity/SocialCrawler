package linkedin;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.nodes.Document;

import com.restfb.types.Page;

import fb.FB;
import fb.FBPage;
import fb.FBParser;
import fb.FBdao;
import play.db.jpa.JPA;

public class LIMaster {
	
	public static void fetchUpdateFeeds() throws InterruptedException {
		String query = "from LIPage lip where lip.fetchDone != true and lip.isGroup != true";
		List<LIPage> liPages = JPA.em().createQuery(query, LIPage.class).getResultList();
		System.out.println("liPages size : " + liPages.size());
		
		for(LIPage liPage : liPages) {
			try{
				Document doc = LI.getFullDocument(liPage.getTrimmedUrl());
				LIParser.parseDocument(liPage, doc);
				List<LIUpdate> liUpdates = LIParser.parseUpdates(doc);
				for(LIUpdate liUpdate : liUpdates){
					JPA.em().persist(liUpdate);
					liUpdate.setLiPage(liPage);
				}
				liPage.setFetchDone(true);
				JPA.em().getTransaction().commit();
				JPA.em().getTransaction().begin();
			} catch (Exception e) {
				String message = e.getClass().getSimpleName() + " exception : " + e.getMessage();
				System.out.println("exception : "+ message);
				liPage.setFetchErrorMessage(e.getClass().getSimpleName() + " exception : " + e.getMessage());
			}
		}
	}
	
	public static void fetchPages() {
		String query = "from LIPage lip where lip.fetchDone != true and lip.isGroup != true";
		List<LIPage> liPages = JPA.em().createQuery(query, LIPage.class).getResultList();
		System.out.println("liPages size : " + liPages.size());
		
		for(LIPage liPage : liPages) {
			try{
				Document doc = LI.getMainDocument(liPage.getTrimmedUrl());
				LIParser.parseDocument(liPage, doc);
				liPage.setFetchDone(true);
				JPA.em().getTransaction().commit();
				JPA.em().getTransaction().begin();
			} catch (Exception e) {
				String message = e.getClass().getSimpleName() + " exception : " + e.getMessage();
				System.out.println("exception : "+ message);
				liPage.setFetchErrorMessage(e.getClass().getSimpleName() + " exception : " + e.getMessage());
			}
		}
	}
	
	public static void readAndFetchPages() throws IOException, InterruptedException { 
		System.out.println("Reading and Fetching LI pages");
		Reader in = new FileReader("./data/in/companies2.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
		
		EntityManager em = JPA.em();
		int total = 0;
		for(CSVRecord record : records) {
			String type = record.get("Site (FB/Tw/LI)");
			if(type.toLowerCase().equals("li")){
				importLIPage(record);
//				if(total > 0){
//					break;
//				}
			} 
			total++;
			
			System.out.println("Processed : " + total);
		}
		
		in.close();
	}
	public static void importLIPage(CSVRecord record) throws InterruptedException{
		String givenUrl = record.get("URL");
		String companyString = record.get("Company");
		Boolean job = Boolean.parseBoolean(record.get("Job/Career (Yes/No)"));
//		Boolean liTab = Boolean.parseBoolean(record.get("LI Tab?"));
//		String geoSpecial = record.get("Geography specific");
		
		if(LIdao.alreadyPresent(givenUrl)){
			return;
		}
		String trimmedUrl = LIParser.trimUrl(givenUrl);
		LIPage liPage = new LIPage();
		liPage.setGivenUrl(givenUrl);
		liPage.setCompanyString(companyString);
		liPage.setJob(job);
//		liPage.setLiTab(liTab);
//		liPage.setGeoSpecial(geoSpecial);
		liPage.setTrimmedUrl(trimmedUrl);
		liPage.setGroup(LIParser.isGroupUrl(givenUrl));
		
		System.out.println("givenUrl : "  + givenUrl);
		
		LIdao.persistPage(liPage);
	}
}
