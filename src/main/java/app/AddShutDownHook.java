package app;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class AddShutDownHook {

    private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    private static Logger logger = lc.getLogger("Inbound Validation Shutdown Hook");

    AddShutDownHook(){
        logger.setLevel(Level.ERROR);
        logger.debug("Shutdown Hook Attached");
    }

    public void attachShutdownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){

                logger.error("Inside Hook");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        });

    }
}

