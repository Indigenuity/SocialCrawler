package utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.restfb.types.NamedFacebookType;

public class Utils {

	public static String listToIdCSV(List<NamedFacebookType> objects){
		String returned = "";
		String delim = "";
		System.out.println("raw : " + objects);
		for(NamedFacebookType item : objects){
			returned += delim + item.getId();
			delim = ",";
		}
		System.out.println("after : " + returned);
		return returned;
	}
	
	public static String sanitize(String original, int length){
		if(original == null)
			return original;
		String sanitized = original.replaceAll("[^\u0000-\uFFFF]", "");
		if(original.length() > 255){
			length = (length - 6) >=0 ? length - 6 : 0;
			return "ABBREV" + StringUtils.abbreviate(sanitized, length);
		}
		
		return sanitized;
	}
}
