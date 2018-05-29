package main.java.com.mishpahug.back.app.constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {
	public static Calendar calendar = Calendar.getInstance();
	
	public static final SimpleDateFormat formatForDate = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat formatForTime = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat formatForDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Integer getYear () {
		return calendar.get(Calendar.YEAR);
	}
	
	public static Integer calculateAge(Date birthday){
	    Calendar dob = Calendar.getInstance();
	    Calendar today = Calendar.getInstance();
	    dob.setTime(birthday);
	    dob.add(Calendar.DAY_OF_MONTH, -1);
	    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
	    if (today.get(Calendar.DAY_OF_YEAR) <= dob.get(Calendar.DAY_OF_YEAR)) {
	        age--;
	    }
	    return age;
	}
	
	public static boolean isDateValid(String date){
	    try {
			formatForDate.setLenient(false);
			formatForDate.parse(date);
			return true;
			} catch (ParseException e) {
				return false;
			}  
	}
	
	public static String addMinutes (Date date, Integer minutes) {
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		String newTime = formatForDateAndTime.format(calendar.getTime());
		return newTime;
	}
}
