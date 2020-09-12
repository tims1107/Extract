package utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProcessCalendar {
    private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    private static Logger logger = lc.getLogger("ProcessCalendar");
    public static void getDateString() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        List<Calendar> calList = CalendarArithmetic.getCurrentPrevious();

        for (int i = 0; i < 9; i++){
            calList.remove(0);
        }
        for (Calendar c : calList) {
            Date d = c.getTime();
            logger.info("loadFactResultsExtract(): " + (d == null ? "NULL" : d.toString()));

        }
    }
}
