package utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.spectra.framework.utils.calendar.ICalendarArithmetic;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public final class CalendarArithmetic implements ICalendarArithmetic {

    private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    private static Logger logger = lc.getLogger("CalendarArithmetic");

    public static Map<Integer, String> MONTH_MAP = null;
    static{
        MONTH_MAP = new HashMap<Integer, String>();
        MONTH_MAP.put(new Integer(Calendar.JANUARY), "January");
        MONTH_MAP.put(new Integer(Calendar.FEBRUARY), "February");
        MONTH_MAP.put(new Integer(Calendar.MARCH), "March");
        MONTH_MAP.put(new Integer(Calendar.APRIL), "April");
        MONTH_MAP.put(new Integer(Calendar.MAY), "May");
        MONTH_MAP.put(new Integer(Calendar.JUNE), "June");
        MONTH_MAP.put(new Integer(Calendar.JULY), "July");
        MONTH_MAP.put(new Integer(Calendar.AUGUST), "August");
        MONTH_MAP.put(new Integer(Calendar.SEPTEMBER), "September");
        MONTH_MAP.put(new Integer(Calendar.OCTOBER), "October");
        MONTH_MAP.put(new Integer(Calendar.NOVEMBER), "November");
        MONTH_MAP.put(new Integer(Calendar.DECEMBER), "December");
    }

    public static Map<String, Integer> MONTH_INT_MAP = null;
    static{
        MONTH_INT_MAP = new HashMap<String, Integer>();
        MONTH_INT_MAP.put("Jan", new Integer(Calendar.JANUARY));
        MONTH_INT_MAP.put("Feb", new Integer(Calendar.FEBRUARY));
        MONTH_INT_MAP.put("Mar", new Integer(Calendar.MARCH));
        MONTH_INT_MAP.put("Apr", new Integer(Calendar.APRIL));
        MONTH_INT_MAP.put("May", new Integer(Calendar.MAY));
        MONTH_INT_MAP.put("Jun", new Integer(Calendar.JUNE));
        MONTH_INT_MAP.put("Jul", new Integer(Calendar.JULY));
        MONTH_INT_MAP.put("Aug", new Integer(Calendar.AUGUST));
        MONTH_INT_MAP.put("Sep", new Integer(Calendar.SEPTEMBER));
        MONTH_INT_MAP.put("Oct", new Integer(Calendar.OCTOBER));
        MONTH_INT_MAP.put("Nov", new Integer(Calendar.NOVEMBER));
        MONTH_INT_MAP.put("Dec", new Integer(Calendar.DECEMBER));
    }

    public static Map<Integer, List<Integer>> QTR_MAP = null;
    static{
        Calendar CAL = Calendar.getInstance();
        QTR_MAP = new HashMap<Integer, List<Integer>>();
        List<Integer> qtrCalList = null;
        Integer keyQtr = null;
        for(int i = 0; i <= Calendar.DECEMBER; i++){
            switch(i){
                case 0:
                case 1:
                case 2:
                    keyQtr = Integer.valueOf(1);
                    if(QTR_MAP.containsKey(keyQtr)){
                        qtrCalList = QTR_MAP.get(keyQtr);
                        qtrCalList.add(new Integer(i));
                    }else{
                        qtrCalList = new ArrayList<Integer>();
                        qtrCalList.add(new Integer(i));
                        QTR_MAP.put(keyQtr, qtrCalList);
                    }
                    break;
                case 3:
                case 4:
                case 5:
                    keyQtr = Integer.valueOf(2);
                    if(QTR_MAP.containsKey(keyQtr)){
                        qtrCalList = QTR_MAP.get(keyQtr);
                        qtrCalList.add(new Integer(i));
                    }else{
                        qtrCalList = new ArrayList<Integer>();
                        qtrCalList.add(new Integer(i));
                        QTR_MAP.put(keyQtr, qtrCalList);
                    }
                    break;
                case 6:
                case 7:
                case 8:
                    keyQtr = Integer.valueOf(3);
                    if(QTR_MAP.containsKey(keyQtr)){
                        qtrCalList = QTR_MAP.get(keyQtr);
                        qtrCalList.add(new Integer(i));
                    }else{
                        qtrCalList = new ArrayList<Integer>();
                        qtrCalList.add(new Integer(i));
                        QTR_MAP.put(keyQtr, qtrCalList);
                    }
                    break;
                case 9:
                case 10:
                case 11:
                    keyQtr = Integer.valueOf(4);
                    if(QTR_MAP.containsKey(keyQtr)){
                        qtrCalList = QTR_MAP.get(keyQtr);
                        qtrCalList.add(new Integer(i));
                    }else{
                        qtrCalList = new ArrayList<Integer>();
                        qtrCalList.add(new Integer(i));
                        QTR_MAP.put(keyQtr, qtrCalList);
                    }
                    break;
            }
        }
    }

    public static Calendar getPrevMonth(Calendar now){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
        //LOG.info("getPrevMonth(): cal: " + (cal == null ? "NULL" : cal.getTime().toString()));
        if(now.get(Calendar.MONTH) == Calendar.JANUARY){
            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            cal.add(Calendar.YEAR, -1);
        }else{
            cal.add(Calendar.MONTH, -1);
        }
        return cal;
    }

    public static Integer getPrevMonthAsIntZeroStart(Calendar now){
        Integer prevMonth = null;
        Calendar cal = getPrevMonth(now);
        if(ConditionChecker.checkNotNull(cal)){
            int mon = cal.get(Calendar.MONTH);
            prevMonth = new Integer(mon);
        }

        return prevMonth;
    }

    public static Integer getPrevMonthAsInt(Calendar now){
        Integer prevMonth = getPrevMonthAsIntZeroStart(now);
        if(ConditionChecker.checkNotNull(prevMonth)){
            int mon = prevMonth.intValue();
            mon += 1;
            prevMonth = new Integer(mon);
        }
        return prevMonth;
    }

    public static Integer getPrevYearAsInt(Calendar now){
        Integer prevYear = null;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
        cal.add(Calendar.YEAR, -1);
        prevYear = new Integer(cal.get(Calendar.YEAR));
        return prevYear;
    }

    public static Integer getPrevMonthYearAsInt(Calendar now){
        Integer prevMonthYear = null;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, now.get(Calendar.YEAR));

        Integer prevMonth = getPrevMonthAsIntZeroStart(cal);
        if(prevMonth.equals(new Integer(Calendar.DECEMBER))){
            cal.add(Calendar.YEAR, -1);
            prevMonthYear = new Integer(cal.get(Calendar.YEAR));
        }else{
            prevMonthYear = new Integer(cal.get(Calendar.YEAR));
        }
        return prevMonthYear;
    }

    public static Integer getCurrentYearAsInt(Calendar now){
        return new Integer(now.get(Calendar.YEAR));
    }

    public static String getFormattedDate(Calendar cal, String format){
        String formattedDate = null;
        if(ConditionChecker.checkNotNull(cal) && ConditionChecker.checkValidString(format)){
            Date date = cal.getTime();
            DateFormat df = new SimpleDateFormat(format);
            //DateFormat df = new SimpleDateFormat(format, Locale.US);
            formattedDate = df.format(date);
        }
        return formattedDate;
    }

    public static List<Calendar> getPrevQtrCalendarList(Calendar cal){
        List<Calendar> qtrCalList = null;
        if(ConditionChecker.checkNotNull(cal)){
            List<Integer>qtrMonList = getPrevQtrMonthList(cal);
            Integer currQtr = getCurrentQtr(cal);
            boolean setYear = (currQtr.equals(Integer.valueOf(1)));
            //LOG.info("getPrevQtrCalendarList(): cal: " + (cal == null ? "NULL" : cal.getTime().toString()));
            //LOG.info("getPrevQtrCalendarList(): qtrMonList: " + (qtrMonList == null ? "NULL" : qtrMonList.toString()));
            //LOG.info("getPrevQtrCalendarList(): currQtr: " + (currQtr == null ? "NULL" : currQtr.toString()));
            //LOG.info("getPrevQtrCalendarList(): setYear: " + (setYear ? String.valueOf(setYear) : String.valueOf(setYear)));
            qtrCalList = new ArrayList<Calendar>();
            for(Integer mon : qtrMonList){
                Calendar c = Calendar.getInstance();
                int dom = c.get(Calendar.DAY_OF_MONTH);
                //LOG.info("getPrevQtrCalendarList(): dom: " + (dom != -1 ? String.valueOf(dom) : String.valueOf(dom)));
                c.setTime(cal.getTime());
                c.set(Calendar.MONTH, mon.intValue());
                c.set(Calendar.DAY_OF_MONTH, (mon.intValue() + 1));
                if(setYear){
                    c.set(Calendar.YEAR, (cal.get(Calendar.YEAR) - 1));
                }
                //LOG.info("getPrevQtrCalendarList(): mon: " + (mon == null ? "NULL" : mon.toString()));
                //LOG.info("getPrevQtrCalendarList(): c: " + (c == null ? "NULL" : c.getTime().toString()));
                qtrCalList.add(c);
            }
        }
        return qtrCalList;
    }

    public static List<Integer> getPrevQtrMonthList(Calendar cal){
        List<Integer> qtrMonList = null;
        if(ConditionChecker.checkNotNull(cal)){
            Integer currQtr = getCurrentQtr(cal);
            if(currQtr.equals(Integer.valueOf(1))){
                currQtr = Integer.valueOf(4);
            }else{
                currQtr = Integer.valueOf(currQtr.intValue() - 1);
            }
            qtrMonList = QTR_MAP.get(currQtr);
        }
        return qtrMonList;
    }

    public static List<Integer> getQtrMonthList(Calendar cal){
        List<Integer> qtrMonList = null;
        Integer qtr = getCurrentQtr(cal);
        if(ConditionChecker.checkNotNull(qtr)){
            qtrMonList = QTR_MAP.get(qtr);
        }
        return qtrMonList;
    }

    public static Integer getCurrentQtr(Calendar cal){
        Integer qtr = null;
        Integer currMonth = new Integer(cal.get(Calendar.MONTH));
        Set<Map.Entry<Integer, List<Integer>>> entrySet = QTR_MAP.entrySet();
        List<Integer> qtrMonList = null;
        for(Iterator<Map.Entry<Integer, List<Integer>>> it = entrySet.iterator(); it.hasNext();){
            Map.Entry<Integer, List<Integer>> entry = it.next();
            qtrMonList = entry.getValue();
            qtr = entry.getKey();
            boolean foundMon = false;
            for(Integer mon : qtrMonList){
                if(mon.equals(currMonth)){
                    foundMon = true;
                    break;
                }
            }
            if(foundMon){
                break;
            }
        }
        return qtr;
    }

    public static List<Integer> getCurrentQtrMonthList(Calendar cal){
        List<Integer> qtrMonList = null;
        Integer currMonth = new Integer(cal.get(Calendar.MONTH));
        Set<Map.Entry<Integer, List<Integer>>> entrySet = QTR_MAP.entrySet();
        for(Iterator<Map.Entry<Integer, List<Integer>>> it = entrySet.iterator(); it.hasNext();){
            Map.Entry<Integer, List<Integer>> entry = it.next();
            qtrMonList = entry.getValue();
            boolean foundMon = false;
            for(Integer mon : qtrMonList){
                if(mon.equals(currMonth)){
                    foundMon = true;
                    break;
                }
            }
            if(foundMon){
                break;
            }
        }
        return qtrMonList;
    }

    public static List<Calendar> getCalendarsByMonth(Integer numberMonth, String direction) {
        List<Calendar> calList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            calList = new ArrayList<Calendar>();
            int monthPrev = numberMonth.intValue();
            //LOG.debug("getCalendarsByMonth(): monthPrev: " + String.valueOf(monthPrev));
            for(int i = monthPrev; i > 0; i--){
                Calendar c = Calendar.getInstance();
                if(ConditionChecker.checkValidString(direction) && BACK.equalsIgnoreCase(direction)){
                    c.add(Calendar.MONTH, -(i));
                }else{
                    c.add(Calendar.MONTH, i);
                }
                calList.add(c);
				/*
				Date d = c.getTime();
				int month = (c.get(Calendar.MONTH) + 1);
				int year = (c.get(Calendar.YEAR));
				System.out.println("i = " + String.valueOf(i) + "\td: " + d.toString() + "\tmonth: " + String.valueOf(month) + "\tyear: " + String.valueOf(year));
				switch(month){
					case 3:
						System.out.println("MEAN Q1");
						break;
					case 6:
						System.out.println("MEAN Q2");
						break;
					case 9:
						System.out.println("MEAN Q3");
						break;
					case 12:
						System.out.println("MEAN Q4");
						break;
				}
				*/
            }
        }
        return calList;
    }

    public static List<Calendar> getCalendarsByMonth(Calendar cal, Integer numberMonth, String direction) {
        List<Calendar> calList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            calList = new ArrayList<Calendar>();
            int monthPrev = numberMonth.intValue();
            //LOG.debug("getCalendarsByMonth(): monthPrev: " + String.valueOf(monthPrev));
            for(int i = monthPrev; i > 0; i--){
                Calendar c = Calendar.getInstance();
                c.setTime(cal.getTime());
                if(ConditionChecker.checkValidString(direction) && BACK.equalsIgnoreCase(direction)){
                    c.add(Calendar.MONTH, -(i));
                }else{
                    c.add(Calendar.MONTH, i);
                }
                calList.add(c);
            }
        }
        return calList;
    }

    public static List<Calendar> getPreviousThreeMonths(Calendar cal){
        List<Calendar> calList = null;
        if(ConditionChecker.checkNotNull(cal)){
            calList = getCalendarsByMonth(cal, new Integer(3), BACK);
        }
        return calList;
    }

    public static List<Calendar> getCurrentPreviousThreeMonths(){
        Calendar now = Calendar.getInstance();
        int mon = now.get(Calendar.MONTH);
        List<Calendar> calList = getCalendarsPrevious(new Integer(3));
        TreeMap<Integer, Calendar> calMap = new TreeMap<Integer, Calendar>();
        for(Calendar cal : calList){
            calMap.put(new Integer(cal.get(Calendar.MONTH)), cal);
        }
        calList = new ArrayList<Calendar>(calMap.values());
        return calList;
    }

    public static List<Calendar> getCurrentPrevious(Calendar cale){
        Calendar now = Calendar.getInstance();
        now.setTime(cale.getTime());
        //now.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), cale.get(Calendar.DATE));
        int mon = now.get(Calendar.MONTH);
        //LOG.debug("getCurrentPrevious(): now = " + now.getTime().toString());
        //LOG.debug("getCurrentPrevious(): mon = " + String.valueOf(mon));
        if(mon == 0){
            mon = (Calendar.DECEMBER + 1);
        }
        //List<Calendar> calList = getCalendarsPrevious(new Integer(mon - 0));
        List<Calendar> calList = getCalendarsPrevious(now, new Integer(mon - 0));
        TreeMap<Integer, Calendar> calMap = new TreeMap<Integer, Calendar>();
        for(Calendar cal : calList){
            calMap.put(new Integer(cal.get(Calendar.MONTH)), cal);
        }
        calList = new ArrayList<Calendar>(calMap.values());
        return calList;
        //return getCalendarsPrevious(new Integer(mon - 0));
    }

    public static List<Calendar> getCurrentPrevious(){
        Calendar now = Calendar.getInstance();
        return getCurrentPrevious(now);
/*
		int mon = now.get(Calendar.MONTH);
		List<Calendar> calList = getCalendarsPrevious(new Integer(mon - 0));
		TreeMap<Integer, Calendar> calMap = new TreeMap<Integer, Calendar>();
		for(Calendar cal : calList){
			calMap.put(new Integer(cal.get(Calendar.MONTH)), cal);
		}
		calList = new ArrayList<Calendar>(calMap.values());
		return calList;
		//return getCalendarsPrevious(new Integer(mon - 0));
*/
    }

    public static List<Calendar> getCurrentRemainderInclusive(Calendar cal){
        Calendar now = Calendar.getInstance();
        now.setTime(cal.getTime());
        //now.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        List<Calendar> calList = new ArrayList<Calendar>();
        calList.add(now);
        calList.addAll(getCurrentRemainder(now));
        return calList;
    }

    public static List<Calendar> getCurrentRemainderInclusive(){
        Calendar now = Calendar.getInstance();
        return getCurrentRemainderInclusive(now);
/*
		List<Calendar> calList = new ArrayList<Calendar>();
		calList.add(now);
		calList.addAll(getCurrentRemainder());
		return calList;
*/
    }


    public static List<Calendar> getCurrentRemainder(Calendar cale){
        Calendar now = Calendar.getInstance();
        now.setTime(cale.getTime());
        //now.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), cale.get(Calendar.DATE));
        int mon = now.get(Calendar.MONTH);
        if(mon == 0){
            mon = (Calendar.DECEMBER + 1);
        }
        //List<Calendar> calList = getCalendarsNext(new Integer(12 - (mon + 1)));
        List<Calendar> calList = getCalendarsNext(now, new Integer(12 - (mon + 1)));
        //LOG.debug("getCurrentRemainder(): calList: " + (calList.toString()));
        //List<Calendar> calList = getCalendarsNext(new Integer(12 - (mon)));
        TreeMap<Integer, Calendar> calMap = new TreeMap<Integer, Calendar>();
        for(Calendar cal : calList){
            calMap.put(new Integer(cal.get(Calendar.MONTH)), cal);
        }
        calList = new ArrayList<Calendar>(calMap.values());
        //LOG.debug("getCurrentRemainder(): calList from map: " + (calList.toString()));
        return calList;
        //return getCalendarsNext(new Integer(12 - (mon + 1)));
    }


    public static List<Calendar> getCurrentRemainder(){
        Calendar now = Calendar.getInstance();
        return getCurrentRemainder(now);
/*
		int mon = now.get(Calendar.MONTH);
		List<Calendar> calList = getCalendarsNext(new Integer(12 - (mon + 1)));
		//List<Calendar> calList = getCalendarsNext(new Integer(12 - (mon)));
		TreeMap<Integer, Calendar> calMap = new TreeMap<Integer, Calendar>();
		for(Calendar cal : calList){
			calMap.put(new Integer(cal.get(Calendar.MONTH)), cal);
		}
		calList = new ArrayList<Calendar>(calMap.values());
		return calList;
		//return getCalendarsNext(new Integer(12 - (mon + 1)));
*/
    }

    public static List<Calendar> getCalendarsPrevious(Calendar cal, Integer numberMonth) {
        List<Calendar> calList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            //LOG.debug("getCalendarsPrevious(): cal = " + cal.getTime().toString());
            //LOG.debug("getCalendarsPrevious(): numberMonth = " + numberMonth.toString());
            calList = getCalendarsByMonth(cal, numberMonth, BACK);
        }
        return calList;
    }

    public static List<Calendar> getCalendarsPrevious(Integer numberMonth) {
        List<Calendar> calList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            calList = getCalendarsByMonth(numberMonth, BACK);
        }
        return calList;
    }

    public static List<Calendar> getCalendarsNext(Calendar cal, Integer numberMonth) {
        List<Calendar> calList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            calList = getCalendarsByMonth(cal, numberMonth, null);
        }
        return calList;
    }

    public static List<Calendar> getCalendarsNext(Integer numberMonth) {
        List<Calendar> calList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            calList = getCalendarsByMonth(numberMonth, null);
        }
        return calList;
    }

    public static List<Date> getDateByMonth(Integer numberMonth, String direction) {
        List<Date> dateList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            List<Calendar> calList = getCalendarsByMonth(numberMonth, direction);
            if(ConditionChecker.checkNotNull(calList)){
                dateList = new ArrayList<Date>();
                for(Calendar c : calList){
                    Date d = c.getTime();
                    dateList.add(d);
                }
            }
        }
        return dateList;
    }

    public static List<Date> getDatePrevious(Integer numberMonth) {
        List<Date> dateList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            dateList = getDateByMonth(numberMonth, BACK);
        }
        return dateList;
    }

    public static List<Date> getDateNext(Integer numberMonth) {
        List<Date> dateList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            dateList = getDateByMonth(numberMonth, null);
        }
        return dateList;
    }

    public static List<Calendar> getRelativeCurrentMonthCalendars(Integer relativeMonth, Integer numberMonth, String direction){
        List<Calendar> calList = null;
        if(ConditionChecker.checkNotNull(numberMonth)){
            calList = new ArrayList<Calendar>();
            int numMonth = numberMonth.intValue();
            for(int i = numMonth; i > 0; i--){
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, relativeMonth.intValue());
                if(ConditionChecker.checkValidString(direction) && BACK.equalsIgnoreCase(direction)){
                    c.add(Calendar.MONTH, -(i));
                }else{
                    c.add(Calendar.MONTH, i);
                }
                calList.add(c);
            }
        }
        return calList;
    }
}

