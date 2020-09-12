package model;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import utils.ParseUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HIVService {

    private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    private static Logger logger = lc.getLogger("HIV Service");

    private HIVListener listener;

    private final StringBuffer sb = new StringBuffer();

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");

    SimpleDateFormat nf = new SimpleDateFormat("yyyyMMddHHmm");

    private final Map<String,String> test = new HashMap<String,String>();

    public void setListener(HIVListener listener){
        this.listener = listener;
    }

    public void getSentResults(int run,String filename){
        try {
            switch (run) {
                case 1:
                    _getSentResults();
                    break;
                case 2:
                    _getNYSentResults();
                    break;
                case 3:
                    //_hl7Format();
                    String teststr = "T:\\asr\\spectraHLAB_External_Interface_Results\\NJ\\202003/NJ.HL7.20200312130533.hl7";
                    String prodstr = "X:/nj_doh/archive/NJ.HL7.20200103030142.hl7_2020-01-06_11.04.08.686";
                    _hl7FormatNJ(teststr);

                    break;
                case 4 :
                    _getArchiveFiles("X:/nj_doh/archive");
                    break;

                case 5:
//                    _getHL7Format(new File("C:/Projects/dev/Sql/TestHL7_CA_251.txt"));
//                   --_getHL7Format(new File("C:/Projects/dev/Sql/CA.HL7.20200508150355.hl7");
                   _getHL7Format(new File(filename));
                    break;
                case 6:
                    _getHL7Format2(new File(filename));
                    break;
                case 7:
                    File [] files = new File(filename).listFiles();
                    for(File f : files){
                        _getHL7Format2(f);
                    }
                    break;
                default:


            }

        } catch (Exception e){
            e.printStackTrace();
        }



//        for (Map.Entry<String,String> m : test.entrySet()){
//            System.out.println(m.getValue());
//        }
    }
    // Process HL7 format
    private void _hl7Format() {

        File [] dirs = new File("U:/HLAB Results Archive/NYS/").listFiles();
        File files [] = null;
        for(File ldir : dirs){
            if(ldir.isDirectory() && ldir.getName().startsWith("2019")) {

                files = ldir.listFiles();
                for(File f : files){
                    if(f.isFile() && f.getName().endsWith("txt")){
                        _getHL7Format(f);
                    }
                }
                files = null;
            }
        }
    }

    public void getNPI(String filename){
        Scanner scan = null;
        int count = 0;

        try {
            scan = new Scanner(new File(filename));
            String [] flds = null;
            String line = null;
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                flds = line.split(",");
                //System.out.println(count++);
                if(ParseUtil.removeQuotes(flds[0]).equals("1932114733")) {
                    System.out.println(line);
                    System.exit(0);
                }

                flds = null;
                line = null;
            }
            scan.close();
            scan = null;
        } catch (IOException ioe){

        } finally {
            if(scan != null) try {scan.close();} catch (Exception e){}
        }

    }


    private void _hl7FormatNJ(String filestr) {

        //File [] files = new File("X:\\nj_doh\\archive").listFiles();
        File f = new File(filestr);
       //for(File f : files){
            //if(f.getName().equals("X:\\nj_doh\\archive\\NJ.HL7.20200103030142.hl7_2020-01-06_11.04.08.686")) {
           //System.out.println(f);
                //_getHL7Format(f);
                _getHL7StringFormat(f);
           // }


        //}
    }

    private void _getArchiveFiles(String dir){
        System.out.println(dir);
        File dirfile = new File(dir);
        if(dirfile.exists()){
            for(File f : dirfile.listFiles()){
                _parseLines(f);
            }
        }
    }

    private void _parseLines(File f){
        _getLines(f,"MSH");
    }

    private void _getHL7Format2(File file){
        Scanner scan = null;
        List<String> fmt = null;
        List<String> flds = null;
        int segcnt = 0;
        int fldcnt = 1;


        StringBuffer sb = new StringBuffer();
        StringBuffer getline = new StringBuffer();

        try {
            scan = new Scanner(file);

            while(scan.hasNextLine()){
                sb.setLength(0);
                sb.append(scan.nextLine());
                fmt = null;
                fmt = ParseUtil.splitLine(sb.toString());


                    segcnt = 1;
                    flds = null;

                    switch (fmt.get(0)) {
                        case "MSH":
                            //System.out.print(file.getName() + "\t" + fmt.get(6) + "\t");
                            getline.append(fmt.get(6) + "\t");
                            break;
                        case "OBR":

                            flds = ParseUtil.splitField(fmt.get(4));
                            //System.out.print(fmt.get(22) + "\t" + fmt.get(3) + "\t" + flds.get(0) + "\t" + flds.get(1) );
                            break;
                        case "OBX":

                            flds = ParseUtil.splitField(fmt.get(3));
                            getline.append(flds.get(3));

                                //System.out.print("\t" + flds.get(3));
                                //System.out.println("\t" + fmt.get(5));
                            if(flds.get(3).startsWith("329") || flds.get(3).startsWith("329")) {
                                System.out.println(getline.toString());
                            }

                            flds = null;
                            break;



                    }
                //System.out.println(getline.toString());
                    getline.setLength(0);
                    sb.setLength(0);

                }





            //System.exit(0);
            scan.close();
            scan = null;
        } catch (IOException ioe){

        } finally {
            if(scan != null){
                scan.close();
                scan = null;
            }
        }


    }


    private void _getHL7Format(File file){
        Scanner scan = null;
        List<String> fmt = null;
        List<String> flds = null;
        int segcnt = 0;
        int fldcnt = 1;

        StringBuffer sb = new StringBuffer();

       try {
           scan = new Scanner(file);

           while(scan.hasNextLine()){
              fmt = ParseUtil.splitLine(scan.nextLine());
               if(fmt.get(0).equals("MSH"))
                   fldcnt = 1 ;
               else
                   fldcnt = 0;

              for (String s : fmt) {

                  segcnt = 1;

                  flds =  ParseUtil.splitField(s);
                      switch (fmt.get(0)) {
                          case "MSH":

                              for(String seq : flds) {
                                  System.out.println(fldcnt + "." + segcnt++ + "\t" + "'" + seq + "'");
                              }
                              fldcnt++;
                              break;
                          case "SFT":
                              for(String seq : flds) {
                                  System.out.println(fldcnt + "." + segcnt++ + "\t" + "'" + seq + "'");
                              }
                              fldcnt++;
                              break;
                          case "PID":
                              for(String seq : flds) {
                                  System.out.println(fldcnt + "." + segcnt++ + "\t" + "'" + seq + "'");
                              }
                              fldcnt++;
                              break;
                          case "ORC":
                              for(String seq : flds) {
                                  System.out.println(fldcnt + "." + segcnt++ + "\t" + "'" + seq + "'");
                              }
                              fldcnt++;
                              break;
                          case "OBR":
                              for(String seq : flds) {
                                  System.out.println(fldcnt + "." + segcnt++ + "\t" + "'" + seq + "'");
                              }
                              fldcnt++;
                              break;
                          case "OBX":
                              for(String seq : flds) {
                                  System.out.println(fldcnt + "." + segcnt++ + "\t" + "'" + seq + "'");
                              }
                              fldcnt++;
                              break;
                          case "SPM":
                              for(String seq : flds) {
                                  System.out.println(fldcnt + "." + segcnt++ + "\t" + "'" + seq + "'");
                              }
                              fldcnt++;
                              break;



                      }



              }



           }

           //System.exit(0);
           scan.close();
           scan = null;
       } catch (IOException ioe){

       } finally {
           if(scan != null){
               scan.close();
               scan = null;
           }
        }


    }

    private void fileChannelWrite(ByteBuffer byteBuffer, String updateFile)
            throws IOException {

        Set options = new HashSet();
        options.add(StandardOpenOption.CREATE);
        options.add(StandardOpenOption.APPEND);



        Path path = Paths.get("./" + updateFile);

        FileChannel fileChannel = FileChannel.open(path, options);
        fileChannel.write(byteBuffer);

        fileChannel.close();
    }

    public static Charset charset = Charset.forName("UTF-8");
    public static CharsetEncoder encoder = charset.newEncoder();
    public static CharsetDecoder decoder = charset.newDecoder();

    public ByteBuffer str_to_bb(String msg, Charset charset){
        return ByteBuffer.wrap(msg.getBytes(charset));
    }

    public String bb_to_str(ByteBuffer buffer, Charset charset){
        byte[] bytes;
        if(buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return new String(bytes, charset);
    }

    private void _getHL7StringFormat(File file){
        Scanner scan = null;
        List<String> fmt = null;
        List<String> flds = null;
        int segcnt = 0;
        int fldcnt = 1;
        StringBuffer [] sb = {new StringBuffer(),new StringBuffer(),new StringBuffer()};
        List<String> buf = new LinkedList<>();

        Map<String,List<StringBuffer>> excelmap = new HashMap<>();

        //StringBuffer sb = new StringBuffer();

        try {
            scan = new Scanner(file);

            while(scan.hasNextLine()){
                fmt = ParseUtil.splitLine(scan.nextLine());

                for(String s : fmt){
//                  if(s.startsWith("Methicillin")){
//                      System.out.println(s);
//                      System.out.println(file);
//                      System.exit(0);
//                  }
                    flds = ParseUtil.splitField(s);
                    if(flds.size() > 1 && !s.equals("^~\\&")){
                        for(String fld : flds) {
                            sb[0].append(fmt.get(0) + "|");
                            sb[1].append(segcnt + "." + fldcnt++ + "|");
                            sb[2].append(fld + "|");
                            //sb.append(fmt.get(0)+ "|" + segcnt + "." + fldcnt++ + "|" + fld + "\n");
                        }

                        segcnt++;
                        fldcnt = 1;
                    } else {
                        sb[0].append(fmt.get(0) + "|");
                        sb[1].append(segcnt++ + "." + fldcnt + "|");
                        sb[2].append(s + "|");
                       // sb.append(fmt.get(0)+ "|" + segcnt++ + "." + fldcnt + "|" + s + "\n");
                    }
                }


                for(int i = 0; i < 3; i++) {

                    sb[i].append("\n");
                    buf.add(sb[i].toString());

                    sb[i].setLength(0);
                }

                segcnt = 0;
                fldcnt = 1;

            }

            //System.exit(0);
            scan.close();
            scan = null;

            for(String s : buf){
                System.out.println(s);
                fileChannelWrite(str_to_bb(s,charset),"testhl7file");

            }


        } catch (Exception ioe){
            ioe.printStackTrace();
        } finally {
            if(scan != null){
                scan.close();
                scan = null;
            }
        }


    }

    private void _getSentResults() throws ParseException {
        File [] dirs = new File("U:/HLAB Results Archive/NYS/").listFiles();
        File [] months = null;
        int count = 0;

        if(listener != null){
            listener.starting("Ready to begin");
        }

        //System.out.println(nf.parse("20191026"));


        for(File dir : dirs){
            if(dir.getName().startsWith("2019")){
                months = new File(dir.getAbsolutePath()).listFiles();
                for(File file : months){
                    if(!file.isDirectory()) {
                        //logger.info(file.getParent() + ":" + file.getName());
                        try {
                            _parseFile(file);
                        } catch (FileNotFoundException | ParseException fnfe){
                            if(listener != null){
                                listener.errorMessage(fnfe.getMessage());
                            }
                        }
                        count++;
                    }
                }
            }



        }
        System.out.println("Testing");
        System.out.println(sb.toString());

        logger.info(Integer.toString(count));


    }

    private void _getNYSentResults() throws ParseException {
        File [] dirs = new File("U:/HLAB Results Archive/NYS/").listFiles();
        File [] months = null;
        int count = 0;

        if(listener != null){
            listener.starting("Ready to begin");
        }

        //System.out.println(nf.parse("20191026"));


        for(File dir : dirs){
            if(dir.getName().startsWith("2019")){
                months = new File(dir.getAbsolutePath()).listFiles();
                for(File file : months){
                    if(!file.isDirectory()) {
                        //logger.info(file.getParent() + ":" + file.getName());
                        try {
                            _parseNYFile(file);
                        } catch (FileNotFoundException | ParseException fnfe){
                            if(listener != null){
                                listener.errorMessage(fnfe.getMessage());
                            }
                        }
                        count++;
                    }
                }
            }



        }

        System.out.println(sb.toString());

        logger.info(Integer.toString(count));


    }

    private void _parseNYFile(File file) throws FileNotFoundException, ParseException {
        Scanner scan = null;
        List<String> obrline = null;
        List<String> pidline = null;
        List<String> obxline = null;
        List<String> result = new ArrayList<String>();
        List<String> fld = null;
        Calendar cal = Calendar.getInstance();
        int obxcount = 0;

        String newline = null;
        int count = 0;


        scan = new Scanner(file);

        while(scan.hasNextLine()){
            newline = scan.nextLine();

            if(newline.startsWith("PID")) {
                obxcount = 0;
                pidline = null;
                pidline = ParseUtil.splitLine(newline);
            } else if (newline.startsWith("OBR")) {
                obrline = null;
                obrline = ParseUtil.splitLine(newline);

                sb.append(ParseUtil.splitField(obrline.get(4)).get(0));
                sb.append("|");
                sb.append(ParseUtil.splitField(obrline.get(4)).get(1));

                test.put(ParseUtil.splitField(obrline.get(4)).get(0),sb.toString());

                //System.out.println(sb.toString());
                sb.setLength(0);



            } else if(newline.startsWith("OBX") ){
                obxcount++;
                obxline = null;
                obxline = ParseUtil.splitLine(newline);

            }






        }



        scan.close();
        scan = null;



    }

    private static void _getLines(File file, String seg){
        Scanner scan = null;
        String line;
        List<String> segs = null;


        try
        {
            scan = new Scanner(file);
            while(scan.hasNextLine()){
               line = scan.nextLine();
               segs = ParseUtil.splitLine(line);
               if(segs.get(0).equals(seg)){

                       System.out.println(line);

               }
               line = null;
               segs = null;
            }
            scan.close();
        } catch (IOException ioe){

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

        String newline = null;
        int count = 0;


            scan = new Scanner(file);

            while(scan.hasNextLine()){
                newline = scan.nextLine();

                if(newline.startsWith("PID")) {
                    obxcount = 0;
                    pidline = null;
                    pidline = ParseUtil.splitLine(newline);
                } else if (newline.startsWith("MSH")){
                    mshline = ParseUtil.splitLine(newline);
                } else if (newline.startsWith("OBR")) {
                    obrline = null;
                    obrline = ParseUtil.splitLine(newline);

                } else if(newline.startsWith("OBX")
                    && obxcount == 0){
                    obxcount++;
                    obxline = null;
                    obxline = ParseUtil.splitLine(newline);

                    if(obrline.get(4).startsWith("335")) {
                        count = 0;
                        for(String s : pidline){
                            System.out.println(count++ + " - " + s);
                        }
                        count = 0;
                        for(String s : obrline){
                            System.out.println(count++ + " - " + s);
                        }
                        count = 0;
                        for(String s : obxline){
                            System.out.println(count++ + " - " + s);
                        }

                        logger.info(pidline.toString());
                        logger.info(obrline.toString());
                        result.add(mshline.get(2));
                        result.add(pidline.get(3));
                        result.add(obxline.get(5));


                        result.add(sdf.format(nf.parse(obrline.get(7))));
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

                        sb.append("\r\n");

                        System.out.println(sb.length());



                    }



                }






            }



            scan.close();
            scan = null;



    }
}
