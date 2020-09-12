package utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.apache.commons.lang.Validate;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ConditionChecker extends Validate {
    private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    private static Logger logger = lc.getLogger("ConditionChecker");
    
    public static boolean checkMatch(String str, String str1){
        if((checkValidString(str)) && (checkValidString(str1))){
            return str.matches(str1);
        }
        return false;
    }
    
    public static boolean checkValidString(String str){
        try{
            notEmpty(str);
        }catch(IllegalArgumentException iae){
            return false;
        }
        return true;
    }
    
    public static boolean checkInvalidString(String str){
        return (!checkValidString(str));
    }
    
    public static boolean checkNotNull(Object o){
        try{
            notNull(o);
        }catch(IllegalArgumentException iae){
            return false;
        }
        return true;
    }
    
    public static boolean checkNull(Object o){
        return (!checkNotNull(o));
    }
    
    public static boolean checkValidArray(Object[] array){
        try{
            notEmpty(array);
        }catch(IllegalArgumentException iae){
            return false;
        }
        return true;
    }
    
    public static boolean checkInvalidArray(Object[] array){
        return (!checkValidArray(array));
    }
    
    public static boolean checkValidByteArray(byte[] array){
        return ((array != null) && (array.length > 0));
    }
    
    public static boolean checkInvalidByteArray(byte[] array){
        return (!checkValidByteArray(array));
    }
    
    public static boolean checkEmpty(Object o){
        boolean isEmpty = false;
        try{
            notNull(o);
        }catch(IllegalArgumentException iae){
            //log.info("checkEmpty(): thrown IllegalArgumentException, " + (o == null ? "NULL" : o.getClass().getName()) + " is empty.");
            return true;
        }
        if(!isEmpty){
            if(o instanceof Collection){
                isEmpty = ((Collection)o).isEmpty();
            }else if(o instanceof Map){
                isEmpty = ((Map)o).isEmpty();
            }else if(o instanceof String){
                isEmpty = checkInvalidString((String)o);
                //log.info("checkEmpty(): checkInvalidString(), " + (o == null ? "NULL" : (String)o) + " isEmpty = " + String.valueOf(isEmpty));
            }else if(o instanceof Iterator){
                isEmpty = !((Iterator)o).hasNext();
            }
        }
        return isEmpty;
    }
    
    public static boolean checkNotEmpty(Object o){
        return (!checkEmpty(o));
    }
}

