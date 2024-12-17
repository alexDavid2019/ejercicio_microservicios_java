package ar.example.registration.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class ConvertUtil {
	
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public static Date convertStringToDate(String date, String timezone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.parse(date);
    }

    public static String convertDateToString(Date date, String timezone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.format(date);
    }

    public static Boolean tryConvertStringToUUID(String uuid) {
    	Pattern UUID_REGEX =
    			  Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    	return  UUID_REGEX.matcher(uuid).matches();
    }
    
}
