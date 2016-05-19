package reporting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import fb.FBPage;
import play.db.DB;
import play.db.jpa.JPA;

public class CSV {
	
	private static String SELF_PHOTOS_BY_MONTH = "SELECT companyString, date_format(createdTime, '%Y-%m') as 'Date', count(*), fbphotoid FROM socialcrawler.fbphoto photo " +
					"join fbpage p on p.fbpageid = photo.fbpage_fbpageid " +
					"where !(hour(createdTime) = 0 and minute(createdTime) = 0 and second(createdTime) = 0) " +
					"AND !(hour(createdTime) = 1 and minute(createdTime) = 0 and second(createdTime) = 0) " +
					"AND !(hour(createdTime) = 13 and minute(createdTime) = 0 and second(createdTime) = 0) " +
					"and photo.fromId = p.fbid " +
					"group by p.fbpageid, date_format(createdTime, '%Y-%m')";
	
	private static String OTHER_PHOTOS_BY_MONTH = "SELECT companyString, fbId, fbPageId, date_format(createdTime, '%Y-%m') as 'Date', count(*), fbphotoid FROM socialcrawler.fbphoto photo " +
			"join fbpage p on p.fbpageid = photo.fbpage_fbpageid " +
			"where !(hour(createdTime) = 0 and minute(createdTime) = 0 and second(createdTime) = 0) " +
			"AND !(hour(createdTime) = 1 and minute(createdTime) = 0 and second(createdTime) = 0) " +
			"AND !(hour(createdTime) = 13 and minute(createdTime) = 0 and second(createdTime) = 0) " +
			"and photo.fromId != p.fbid " +
			"group by p.fbpageid, date_format(createdTime, '%Y-%m')";
	
	
	public static void fbPhotosReport() throws IOException, SQLException {
		
		Set<String> staticFieldDefinitions = new LinkedHashSet<String>();
		staticFieldDefinitions.add("fbId");
		staticFieldDefinitions.add("fbPhotoId");
		Set<String> dynamicFieldDefinitions = new LinkedHashSet<String>();
		dynamicFieldDefinitions.add("count(*)");
		dynamicFieldDefinitions.add("commentsCount");
		dynamicFieldDefinitions.add("likesCount");
		dynamicFieldDefinitions.add("reactionsCount");
		dynamicFieldDefinitions.add("hasPlace");
		dynamicFieldDefinitions.add("hasCaption");
		dynamicFieldDefinitions.add("hasEvent");
		dynamicFieldDefinitions.add("updatedLater");
		
		String query = "SELECT companyString, date_format(createdTime, '%Y-%m') as 'date', count(*), sum(if(placeId is null, 0, 1)) as 'hasPlace', " +
				"sum(if(createdTime = updatedTime, 1, 0)) as 'updatedLater', sum(likesCount) as likesCount, sum(commentsCount) as commentsCount, " +
				"sum(reactionsCount) as reactionsCount, sum(if(photo.name is null, 0, 1)) as 'hasCaption', sum(if(eventId is null, 0, 1)) as 'hasEvent', " +
				"fbId, fbPhotoId " +
			    "FROM socialcrawler.fbphoto photo " +
				"join fbpage p on p.fbpageid = photo.fbpage_fbpageid " +
				"where !(hour(createdTime) = 0 and minute(createdTime) = 0 and second(createdTime) = 0) " +
				"AND !(hour(createdTime) = 1 and minute(createdTime) = 0 and second(createdTime) = 0) " +
				"AND !(hour(createdTime) = 13 and minute(createdTime) = 0 and second(createdTime) = 0) " +
				"and photo.fromId = p.fbid " +
				"group by p.fbpageid, date_format(createdTime, '%Y-%m') " +
				"order by count(*) desc";
		
		createCompanyReportByDate("FBPhotos Monthly", query, staticFieldDefinitions, dynamicFieldDefinitions);
		query = query.replaceAll("%Y-%m", "%Y");
		createCompanyReportByDate("FBPhotos Yearly", query, staticFieldDefinitions, dynamicFieldDefinitions);
	}
	
public static void fbPostsReport() throws IOException, SQLException {
		
		Set<String> staticFieldDefinitions = new LinkedHashSet<String>();
		staticFieldDefinitions.add("fbId");
		staticFieldDefinitions.add("fbPageId");
		Set<String> dynamicFieldDefinitions = new LinkedHashSet<String>();
		dynamicFieldDefinitions.add("count(*)");
		dynamicFieldDefinitions.add("commentsCount");
		dynamicFieldDefinitions.add("likesCount");
		dynamicFieldDefinitions.add("reactionsCount");
		dynamicFieldDefinitions.add("sharesCount");
		dynamicFieldDefinitions.add("hasCaption");
		dynamicFieldDefinitions.add("blankMessage");
		dynamicFieldDefinitions.add("updatedLater");
		dynamicFieldDefinitions.add("hasParent");
		dynamicFieldDefinitions.add("hasPlace");
		dynamicFieldDefinitions.add("hasMessageTags");
		dynamicFieldDefinitions.add("hasSource");
		dynamicFieldDefinitions.add("hasTo");
		dynamicFieldDefinitions.add("postedFromApp");
		dynamicFieldDefinitions.add("added_photosStory");
		dynamicFieldDefinitions.add("shared_storyStory");
		dynamicFieldDefinitions.add("added_videoStory");
		dynamicFieldDefinitions.add("mobile_status_updateStory");
		dynamicFieldDefinitions.add("published_storyStory");
		dynamicFieldDefinitions.add("created_noteStory");
		dynamicFieldDefinitions.add("wall_postStory");
		dynamicFieldDefinitions.add("created_eventStory");
		dynamicFieldDefinitions.add("noTypeStory");
		
		String query = "SELECT companyString, "
					+ "fbId, " 
					+ "fbPageId, " 
				    + "date_format(realCreatedDate, '%Y-%m') as 'date', " 
				    + "count(*), " 
				    + "sum(shares) as 'sharesCount', " 
				    + "sum(if(caption is null, 0, 1)) as 'hasCaption', "
				    + "sum(if(message is null, 1, 0)) as 'blankMessage', "
					+ "sum(if(realCreatedDate = realLastUpdated, 1, 0)) as 'updatedLater', " 
				    + "sum(likesCount) as likesCount, " 
				    + "sum(commentsCount) as commentsCount, "
				    + "sum(reactionsCount) as reactionsCount, " 
				    + "sum(if(parentId is null, 0, 1)) as 'hasParent', "
				    + "sum(if(place = 'null', 0, 1)) as 'hasPlace', " 
				    + "sum(if(messageTags = '[]', 0, 1)) as 'hasMessageTags', " 
				    + "sum(if(source is null, 0, 1)) as 'hasSource', "
				    + "sum(if(toId = '', 0, 1)) as 'hasTo', "
				    + "sum(if(app = 'null', 0, 1)) as 'postedFromApp', "
				    + "sum(if(statusType = 'added_photos', 1, 0)) as 'added_photosStory', "
				    + "sum(if(statusType = 'shared_story', 1, 0)) as 'shared_storyStory', "
				    + "sum(if(statusType = 'added_video', 1, 0)) as 'added_videoStory', "
				    + "sum(if(statusType = 'mobile_status_update', 1, 0)) as 'mobile_status_updateStory', "
				    + "sum(if(statusType = 'published_story', 1, 0)) as 'published_storyStory', "
				    + "sum(if(statusType = 'created_note', 1, 0)) as 'created_noteStory', "
				    + "sum(if(statusType = 'wall_post', 1, 0)) as 'wall_postStory', "
				    + "sum(if(statusType = 'created_event', 1, 0)) as 'created_eventStory', "
				    + "sum(if(statusType is null, 1, 0)) as 'noTypeStory' "
					+ "FROM socialcrawler.fbpost post "
					+ "join fbpage p on p.fbpageid = post.fbpage_fbpageid "
					+ "where !(hour(realCreatedDate) = 0 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0) "
					+ "AND !(hour(realCreatedDate) = 1 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0) "
					+ "AND !(hour(realCreatedDate) = 13 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0) "
					+ "AND !(hour(realCreatedDate) = 12 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0) "
					+ "AND !(hour(realCreatedDate) = 14 and minute(realCreatedDate) = 0 and second(realCreatedDate) = 0) "
					+ "and post.fromId = p.fbId "
					+ "group by p.fbpageid, date_format(realCreatedDate, '%Y-%m')";
		
		createCompanyReportByDate("FBPosts Self Monthly", query, staticFieldDefinitions, dynamicFieldDefinitions);
		query = query.replace("post.fromId = p.fbId", "post.fromId != p.fbId");
		createCompanyReportByDate("FBPosts Others Monthly", query, staticFieldDefinitions, dynamicFieldDefinitions);
		query = query.replaceAll("%Y-%m", "%Y");
		createCompanyReportByDate("FBPosts Others Yearly", query, staticFieldDefinitions, dynamicFieldDefinitions);
		query = query.replace("post.fromId != p.fbId", "post.fromId = p.fbId");
		createCompanyReportByDate("FBPosts Self Yearly", query, staticFieldDefinitions, dynamicFieldDefinitions);
	}
	
	
	public static void createCompanyReportByDate(String name, String query, Set<String> staticFieldDefinitions, Set<String> dynamicFieldDefinitions) throws SQLException, IOException{
		CSVReport report = new CSVReport(name);
		Map<String, Map<String, String>> companyDynamicFields = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> companyStaticFields = new HashMap<String, Map<String, String>>();
		System.out.println("Fetcing Data");
		Connection connection = DB.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		System.out.println("Exploding data");
		Set<String> allDates = new TreeSet<String>();
		while(rs.next()) {
			String companyString = rs.getString("companyString");
			String date = rs.getString("date");
			Map<String, String> entries = companyDynamicFields.get(companyString);
			Map<String, String> staticEntries = companyStaticFields.get(companyString);
			if(entries == null){
				entries = new HashMap<String, String>();
				companyDynamicFields.put(companyString, entries);
			}
			if(staticEntries == null) {
				staticEntries = new HashMap<String, String>();
				companyStaticFields.put(companyString, staticEntries);
			}
			for(String field : dynamicFieldDefinitions) {
				entries.put(date + " " + field, rs.getString(field));
			}
			for(String field : staticFieldDefinitions) {
				staticEntries.put(field, rs.getString(field));
			}
			allDates.add(date);
		}
		
		System.out.println("Setting up Headers");
		List<String> values = new ArrayList<String>();
		values.add("Company Name");
		for(String field : staticFieldDefinitions) {
			values.add(field);
		}
		for(String field : dynamicFieldDefinitions) {
			for(String date : allDates){
				values.add(date + " " + field);
			}
		}
		report.setHeaderValues(values);
		
		//Fill in Rows
		int count = 0;
		for(Entry<String, Map<String, String>> entry : companyDynamicFields.entrySet()){
			values = new ArrayList<String>();
			values.add(entry.getKey());
			Map<String, String> entries = entry.getValue();
			Map<String, String> staticEntries = companyStaticFields.get(entry.getKey());
			for(String field : staticFieldDefinitions) {
				values.add(staticEntries.get(field));
			}
			for(String field : dynamicFieldDefinitions) {
				for(String date : allDates){
					String value = entries.get(date + " " + field);
					if(value == null){
						value = "0";
					}
					values.add(value);
				}
			}
			report.addRow(values);
			System.out.println("Printing row : " + ++count);
		}
		
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
