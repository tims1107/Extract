package app;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;

import controller.Controller;


import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Main {

    private final static ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);



    private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    private static Logger logger = lc.getLogger("Extract SQL main()");

    private Controller controller;
    private int runProc = 0;

    final ScheduledFuture<?> exec;



    public Main(String args[])  {


        runProc = Integer.parseInt(args[3]);
        exec = scheduler.scheduleAtFixedRate(sched, Integer.parseInt(args[1]),
                Integer.valueOf(args[1]), TimeUnit.SECONDS);

        if (args.length != 4 || args == null) return;

        AddShutDownHook hook = new AddShutDownHook();
        hook.attachShutdownHook();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            configurator.doConfigure(Main.class.getClassLoader().getResourceAsStream(args[2]));
            controller = new Controller(args);

        } catch (JoranException e ) {
            logger.error(e.getMessage());
        }


    }

    /*     0 - url3.json database connection setup json format
           1 - 10 schedule wait seconds
           2 - logger.xml log file configuration file
           3 - [runProc] - controller.runProcess(runProc);
    */
    public static void main(String[] args) {


        try {

            new Main(args);
            logger.debug("Start main()");
        } catch (NumberFormatException e) {
            logger.error("Listener not started " + e.getMessage());
        }

    }

    final Runnable sched = new Runnable() {
        int count = 0;
        Scanner scan = null;
        String next;



        public void run() {
            try
            {
                Thread.currentThread().setName("Scheduler");

                scan = new Scanner(System.in);
                try
                {
                runProc = Integer.parseInt(scan.nextLine().trim());
                }catch (NumberFormatException nfe){
                    runProc = 0;
                }


                if (runProc == 0 ) {
                    System.exit(0);
                }


                controller.runProcess(runProc);

                logger.debug(Integer.toString(count++));


            } catch (Throwable t) {
                t.printStackTrace();
                try {
                    throw t;
                } catch (Throwable e) {

                }


            }

        }

    };





}
