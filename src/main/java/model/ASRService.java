package model;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import utils.ParseUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class ASRService {

    private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    private static Logger logger = lc.getLogger("HIV Service");

    private HIVListener listener;

    private final StringBuffer sb = new StringBuffer();

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");

    SimpleDateFormat nf = new SimpleDateFormat("yyyyMMddHHmm");

    public void setListener(HIVListener listener){
        this.listener = listener;
    }

    public void getSentResults(){
        try {
            _getSentResults();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getSentResultsDir(){

    }

    private void _getSentResults() throws ParseException {
        File [] dirs = new File("H://JohnShen/test/asr/spectraHLAB_External_Interface_Results/nj_doh/prod").listFiles();
        File [] months = null;
        int count = 0;

        if(listener != null){
            listener.starting("Ready to begin");
        }

        //System.out.println(nf.parse("20191026"));


        for(File dir : dirs){
            if(dir.isDirectory()) continue;
            if(dir.getName().endsWith(".hl7")){

                        //logger.info(file.getParent() + ":" + file.getName());
                        try {
                            _parseFile(dir);
                        } catch (FileNotFoundException | ParseException fnfe){
                            if(listener != null){
                                listener.errorMessage(fnfe.getMessage());
                            }

                        }
                }




        }




    }


    private void _parseFile(File file) throws FileNotFoundException, ParseException {
        Scanner scan = null;
        List<String> obrline = null;
        List<String> pidline = null;
        List<String> obxline = null;
        List<String> mshline = null;

        List<String> result = new ArrayList<String>();
        List<String> fld = null;
        Calendar cal = Calendar.getInstance();
        int obxcount = 0;
        String msh = null;

        String newline = null;
        int count = 0;


            scan = new Scanner(file);
            System.out.println(file);
            while(scan.hasNextLine()){
                newline = scan.nextLine();
                if(newline.startsWith("MSH")){
                    mshline = ParseUtil.splitLine(newline);
                    System.out.println(mshline.get(2));
                    mshline = null;
                }
                if(newline.startsWith("PID")) {
                    obxcount = 0;
                    pidline = null;
                    pidline = ParseUtil.splitLine(newline);
                } else if (newline.startsWith("OBR")) {
                    obrline = null;
                    obrline = ParseUtil.splitLine(newline);

                } else if(newline.startsWith("OBX")
                    && obxcount == 0){
                    obxcount++;
                    obxline = null;
                    obxline = ParseUtil.splitLine(newline);


                        count = 0;
                        for(String s : pidline){
                            //System.out.println(count++ + " - " + s);
                        }
                        count = 0;
                        for(String s : obrline){
                            //System.out.println(count++ + " - " + s);
                        }
                        count = 0;
                        //for(String s : obxline){
                            //System.out.println(count++ + " - " + s);
                    //System.out.println(obxline);
                            System.out.println(obrline.get(2) + " " + obxline.get(2) + " " + obxline.get(4) + " " + obxline.get(5) + " " + obxline.get(6) + " " + obxline.get(7));
                       // }

                        //logger.info(pidline.toString());
                        //logger.info(obrline.toString());
                        //result.add(pidline.get(3));
                        //result.add(obxline.get(5));


                        /*result.add(sdf.format(nf.parse(obrline.get(7))));
                        //result.add(Integer.toString(cal.get(Calendar.MONTH)));


                        fld = ParseUtil.splitField(obrline.get(4));

                        result.add(fld.get(0));
                        result.add(fld.get(1));
                        result.add("NY");

                        fld = null;
                        fld = ParseUtil.splitField(pidline.get(5));
                        result.add(fld.get(1));
                        result.add(fld.get(0));
                        try {
                            result.add(fld.get(2).substring(0, 1));
                        } catch (IndexOutOfBoundsException iobe){
                            result.add("");
                        }

                        fld = null;
                        fld = ParseUtil.splitField(pidline.get(11));
                        result.add(fld.get(0));
                        result.add(fld.get(1));
                        result.add(fld.get(2));
                        result.add(fld.get(3));
                        result.add(fld.get(4));


                        count = result.size();
                        for(String res : result){

                           sb.append(res);
                           count--;
                           if(count != 0)
                                 sb.append("|");
                        }

                        sb.append("\r\n");*/

                        //System.out.println(sb.length());




                }






            }



            scan.close();
            scan = null;



    }
}
