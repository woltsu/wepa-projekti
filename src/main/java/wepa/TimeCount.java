package wepa;

import java.util.Calendar;
import java.util.Date;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("default")
public class TimeCount {

    public String howLongAgo(Date date) {
        Calendar oldCalendar = Calendar.getInstance();
        oldCalendar.setTime(date);
        Calendar newCalendar = Calendar.getInstance();

        long oldTime = oldCalendar.getTimeInMillis();
        long newTime = newCalendar.getTimeInMillis();
        long differenceInMinutes = ((newTime - oldTime) / 1000) / 60;
        
        
        if (differenceInMinutes < 1) {
            return ((newCalendar.getTimeInMillis() - oldCalendar.getTimeInMillis()) / 1000) + " seconds ago";
        
        } else if (differenceInMinutes < 60) {
            if (differenceInMinutes == 1) {
                return "1 minute ago";
            } else {
                return differenceInMinutes + " minutes ago";
            }
            
        }

        int differenceInHours = (int) (differenceInMinutes / 60);
        if (differenceInHours < 24) {
            if (differenceInHours == 1) {
                return "1 hour ago";
            } else {
                return differenceInHours + " hours ago";
            }
        }
        
        int differenceInDays = (int) (differenceInHours / 24);
        if (differenceInDays < 30) {
            if (differenceInDays == 1) {
                return "1 day ago";
            } else {
                return differenceInDays + " days ago";
            }
        }
        
        int differenceInMonths = (int) (differenceInDays / 365);
        if (differenceInMonths < 12) {
            if (differenceInMonths == 1) {
                return "1 month ago";
            } else {
                return differenceInMonths + " months ago";
            }
        }
        
        int differenceInYears = differenceInMonths / 12;
        if (differenceInYears == 1) {
            return "1 year ago";
        } 
        
        return differenceInYears + " years ago";
    }

}
