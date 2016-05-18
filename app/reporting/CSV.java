package reporting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import fb.FBPage;
import play.db.jpa.JPA;

public class CSV {
	
	private static String SELF_PHOTOS_BY_MONTH = "SELECT companyString, date_format(createdTime, '%Y-%m') as 'Date', count(*), fbphotoid FROM socialcrawler.fbphoto photo " +
					"join fbpage p on p.fbpageid = photo.fbpage_fbpageid " +
					"where !(hour(createdTime) = 0 and minute(createdTime) = 0 and second(createdTime) = 0) " +
					"AND !(hour(createdTime) = 1 and minute(createdTime) = 0 and second(createdTime) = 0) " +
					"AND !(hour(createdTime) = 13 and minute(createdTime) = 0 and second(createdTime) = 0) " +
					"and photo.fromId = p.fbid " +
					"group by p.fbpageid, date_format(createdTime, '%Y-%m')";
	
	private static String OTHER_PHOTOS_BY_MONTH = "SELECT companyString, date_format(createdTime, '%Y-%m') as 'Date', count(*), fbphotoid FROM socialcrawler.fbphoto photo " +
			"join fbpage p on p.fbpageid = photo.fbpage_fbpageid " +
			"where !(hour(createdTime) = 0 and minute(createdTime) = 0 and second(createdTime) = 0) " +
			"AND !(hour(createdTime) = 1 and minute(createdTime) = 0 and second(createdTime) = 0) " +
			"AND !(hour(createdTime) = 13 and minute(createdTime) = 0 and second(createdTime) = 0) " +
			"and photo.fromId != p.fbid " +
			"group by p.fbpageid, date_format(createdTime, '%Y-%m')";
	
	public static void generateSiteImportReport() throws IOException {
		CSVReport report = new CSVReport("Site Import Report");
		
		List<String> values = new ArrayList<String>();
		values.add("Salesforce Unique ID");
		values.add("Account Name");
		values.add("Current SalesForce Primary Website URL:");
		values.add("Resolved URL");
		values.add("Status Code");
		values.add("Accepted");
		values.add("No change");
		values.add("Changed but accepted");
		//values.add("Manually Changed");
		values.add("Manually Approved");
		values.add("Shared Site");
		values.add("Needs Attention");
		values.add("Shares homepage with");
		values.add("Shares domain with");
		report.setHeaderValues(values);
		
		String query = "from FBPage fb";
		List<FBPage> pages = JPA.em().createQuery(query, FBPage.class).getResultList();
		
		for(FBPage page : pages){
			
		}
		
		int count = 0;
//		for(Task task : taskSet.getTasks()){
//			values = new ArrayList<String>();
//			
//			if(task.getWorkType() != WorkType.SUPERTASK){
//				continue;
//			}
//			Task urlTask = task.getSubtask(WorkType.REDIRECT_RESOLVE);
//			Task siteImportTask = task.getSubtask(WorkType.SITE_IMPORT);
//			if(urlTask == null || siteImportTask == null){
//				continue;
//			}
//			String sfEntryIdString = task.getContextItem("sfEntryId");
//			String urlCheckIdString = urlTask.getContextItem("urlCheckId");
//			String siteIdString = siteImportTask.getContextItem("siteId");
//			if(StringUtils.isEmpty(urlCheckIdString) || StringUtils.isEmpty(sfEntryIdString)){
//				continue;
//			}
//			SFEntry sf =JPA.em().find(SFEntry.class, Long.parseLong(sfEntryIdString));
//			UrlCheck urlCheck = JPA.em().find(UrlCheck.class, Long.parseLong(urlCheckIdString));
//			Site site = null;
//			if(siteIdString != null){
//				site = JPA.em().find(Site.class, Long.parseLong(siteIdString));
//			}
//			
//			values.add(sf.getAccountId());
//			values.add(sf.getName());
//			values.add(sf.getWebsite());
//			values.add(urlCheck.getResolvedSeed());
//			values.add(urlCheck.getStatusCode() + "");
//			values.add(urlCheck.isAccepted() + "");
//			values.add(urlCheck.isNoChange() + "");
//			values.add((!urlCheck.isNoChange() && urlCheck.isAccepted()) + "");
//			values.add(urlCheck.isManuallyApproved() + "");
//			values.add(urlCheck.isSharedSite() + "");
//			if(site == null){
//				values.add("");
//				values.add("");
//			} else {
////				List<Site> dupHomepages = SitesDAO.getList("homepage", site.getHomepage(), 20, 0);
////				for(Site dup : dupHomepages){
////					SFEntry = 
////				}
//			}
//			report.addRow(values);
//			if(++count % 500 == 0) {
//				System.out.println("count : " + count);
//			}
//		}
		writeReport(report);
	}
	
	public static void writeReport(CSVReport report) throws IOException{
		System.out.println("Writing to file ");
		
		String targetFilename = "./data/out/" + report.getName();
		if(report.isAppendDate()){
			targetFilename += System.currentTimeMillis();  
		}
		targetFilename += ".csv";
		File target = new File(targetFilename);
		FileWriter fileOut = new FileWriter(target);
		CSVPrinter printer = new CSVPrinter(fileOut, CSVFormat.EXCEL);
		printer.printRecords(report.getRows());
		printer.close();
		fileOut.close();
	}
}
