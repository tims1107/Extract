package controller;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.github.jonathanlink.PDFLayoutTextStripper;
import model.*;

import org.apache.pdfbox.examples.pdmodel.*;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.LoggerFactory;
import utils.FileUtil;
import utils.ParseUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static utils.FileUtil.*;


public class Controller {
    private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    private static Logger logger = lc.getLogger("Controller");
    private final HIVService hivService = new HIVService();
    private final ASRService asrService = new ASRService();

    public Controller (String [] args){

    }



    public void runProcess(int runProc) {
        hivService.setListener(new HIVListener() {
            @Override
            public void starting(String message) {
                logger.info(message);
            }

            @Override
            public void errorMessage(String message) {
                logger.info(message);
            }
        });

        asrService.setListener(new HIVListener() {
            @Override
            public void starting(String message) {

            }

            @Override
            public void errorMessage(String message) {

            }
        });

        System.out.println("11 - Test control number ");

        switch (runProc) {
            case 1:

                    //createPDF();
                    //testDrawTextLineText();
                    sendFiles("S:/Reports/daily/");


                break;

            case 2:
                logger.info("1: HIV Report");

                hivService.getSentResults(1,"");

                break;

            case 3:
                new LoadArray().testArray();
                System.out.println(ParseUtil.TranType.ADD.value);

                break;

            case 4:

                asrService.getSentResults();
                break;


            case 91:
                hivService.getNPI("c:/physicians/npidata_pfile_20050523-20200607.csv");
                break;
            case 5:
                //logger.info("1: NY Report");

//                hivService.getSentResults(5,"C:/test_hl7_files/or.hl7_20200608");
//                hivService.getSentResults(5,"C:/test_hl7_files/asr_or.hl7");
                File files [] = new File("C:/test_hl7_files/CA").listFiles();
                hivService.getSentResults(5,files[0].getAbsolutePath());
                files = null;
                break;
            case 15:
//                hivService.getSentResults(6,"C:/test_hl7_files/or_test_east.hl7");
                hivService.getSentResults(6,"C:/test_hl7_files/PA.HL7.20200716093911.hl7");
                break;

            case 16: // Results Sent parser
                hivService.getSentResults(7,"c:/sentresults/");
                break;

            case 17: // get from extract all

            case 6:
                // HL7 Format for NY
                hivService.getSentResults(3,"");
                break;
            case 7:
                try {
                    makeLandscape();
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 8: // Get message from archive files
                hivService.getSentResults(4,"");
                break;

            case 9: //Test file write from StringBuffer
                StringBuffer testbuffer = new StringBuffer();
                testbuffer.append("This is a test");
                FileUtil.fileChannelWrite(FileUtil.strbuf_to_bb(testbuffer),"testfile");

                break;

            case 11:
                StringBuffer sb = new StringBuffer();

                sb.append(getControlNum("./controlnum"));

                System.out.println(Long.getLong(sb.toString()));
                long newnum = 0;
                newnum = Long.parseLong(sb.toString().trim());
                sb.setLength(0);
               // sb.append(newnum);
                newnum++;

                sb.append(newnum);

                fileChannelWrite(strbuf_to_bb(sb),"./controlnum");

                break;
        }

    }

    private void passLine(String content){
        Scanner scan = null;
        List<String> grpnames = new ArrayList<>();
        List<String> patterns = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        Map<String,String> map = new HashMap<>();
        Map<String,String> fillmap = new HashMap<>();
        int chksum = 0;
        List<HlabReport> hlabreports = new ArrayList<>();

        //System.out.println(content);
        try {
            Collections.addAll(grpnames, new String [] {"reqline","requisitionid","pcity","pstate","pzip","fcity","fstate","fzip","lname","fname","eid"});
            Collections.addAll(patterns,new String [] {"(^\\s{10})(?<pcity>[A-Z]+)(\\s+.)(?<pstate>[A-Z]{2})(\\s+)(?<pzip>\\d{5})",
                    "(?<reqline>^.+ACCT:)(.+)(REQ#.{6})(?<requisitionid>[0-9A-Z]{7})",
                    "(^\\s{39})(?<fcity>[0-9A-Z]{1}.+)(?<fstate>[A-Z]{2})(\\s+)(?<fzip>\\d{5})",
                    "(?<lname>^.+)(,)(?<fname>.+)(MR#)(\\s+)(?<eid>\\d+)"});
            scan = new Scanner(content);
            while (scan.hasNextLine()){
                sb.append(scan.nextLine());
                for(String p : patterns) {
                    if(fillmap.containsKey("fstate")) {

                        hlabreports.add(new HlabReport(fillmap.get("fname"),fillmap.get("lname"),fillmap.get("pstate"),
                                fillmap.get("requisitionid"),fillmap.get("pcity"),fillmap.get("pzip"),
                                fillmap.get("fstate"),fillmap.get("fcity"),fillmap.get("fzip")));

                        if(fillmap.containsKey("pstate")) {
                            //System.out.println("requisitionid" + fillmap.get());
                        }

                        fillmap.clear();
                    }

                    map = ParseUtil.getMap(sb.toString(), p, grpnames);
                    if(map.size() > 0) {
                        //System.out.println(map);
                        fillmap.putAll(map);
                        //System.out.println(fillmap);
                    }

                    //map.clear();

                    if(fillmap.containsKey("reqline")){
                        //System.out.println(fillmap.get("requisitionid"));
                        chksum = chksum + 1;
                    } else if (fillmap.containsKey("pcity")){
                        //System.out.println("Patient: " + map.get("pcity") + " " + map.get("pstate") + " " + map.get("pzip"));
                        chksum = chksum + 2;
                    } else if (fillmap.containsKey("fcity")){
                        //System.out.println("Facility: " + map.get("fcity") + " " + map.get("fstate") + " " + map.get("fzip"));
                        //System.out.println();

                        chksum = chksum + 4;
                    } else if (fillmap.containsKey("lname")){
                        //System.out.println("Last Name: " + map.get("lname").trim() + ", " + map.get("fname").trim() + " " + map.get("eid"));
                        chksum = chksum + 4;
                    }

                }
                //System.out.println(chksum);
                chksum = 0;
                sb.setLength(0);

            }

            for(HlabReport rpt : hlabreports){
                System.out.println(rpt.getRequisition() + "\t" + rpt.getLname().trim() + "\t" + rpt.getFname().trim() +
                        "\t" + rpt.getPstate() + "\t" + rpt.getFstate());
            }

            scan.close();
            scan = null;

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(scan != null){
                scan.close();
            }
        }

    }

    private void parseLine(String line){
        try {
            Pattern regex = Pattern.compile("(?<reqline>^ACCT:)(.+)(REQ#.{6})(?<requisitionid>[0-9A-Z]{7})");
            Matcher regexMatcher = regex.matcher(line);
            while (regexMatcher.find()) {
                System.out.println(regexMatcher.group());
                // match start: regexMatcher.start()
                // match end: regexMatcher.end()
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }
    }

    private void sendFiles(String dir){
        File dirname = new File(dir);
        if(dirname.exists()) System.out.println(dirname);
        File [] files = new File(dir).listFiles();
        for (File f : files){
            System.out.println(f.getAbsolutePath());
            parseDoc2(f.getAbsolutePath());
        }
    }


    private void parseDoc2(String file){
        String string = null;
        try {
            PDFParser pdfParser = new PDFParser(new RandomAccessBufferedFileInputStream( file),"r");
            pdfParser.parse();
            PDDocument pdDocument = new PDDocument(pdfParser.getDocument());
            PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
            string = pdfTextStripper.getText(pdDocument);
            passLine(string);
            pdDocument.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        };

    }

    private void parseDoc(String file){

        try {
            PDDocument document = PDDocument.load(new File(file));
            PDFTextStripper s = new PDFTextStripper();
            String content = s.getText(document);

            System.out.println(content);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createPDF() throws IOException {

        PDDocument doc = new PDDocument();

        PDPage myPage = new PDPage();
        doc.addPage(myPage);

        PDPageContentStream cont = new PDPageContentStream(doc, myPage);

            cont.beginText();

            cont.setFont(PDType1Font.TIMES_ROMAN, 12);
            cont.setLeading(14.5f);

            cont.newLineAtOffset(25, 700);
            String line1 = "World War II (often abbreviated to WWII or WW2), "
                    + "also known as the Second World War,";
            cont.showText(line1);

            cont.newLine();

            String line2 = "was a global war that lasted from 1939 to 1945, "
                    + "although related conflicts began earlier.";
            cont.showText(line2);
            cont.newLine();

            String line3 = "It involved the vast majority of the world's "
                    + "countries—including all of the great powers—";
            cont.showText(line3);
            cont.newLine();

            String line4 = "eventually forming two opposing military "
                    + "alliances: the Allies and the Axis.";
            cont.showText(line4);
            cont.newLine();

            cont.endText();

            cont.close();


        doc.save("C:/PdfBox_Examples/testi.pdf");

        doc.close();

        System.out.println("PDF created");



    }

    public void doIt(String message, String outfile) throws IOException {
        PDDocument doc = null;

        try {
            doc = new PDDocument();
            PDFont font = PDType1Font.HELVETICA;
            PDPage page = new PDPage(PDRectangle.A4);
            page.setRotation(90);
            doc.addPage(page);
            PDRectangle pageSize = page.getMediaBox();
            float pageWidth = pageSize.getWidth();
            float fontSize = 12.0F;
            float stringWidth = font.getStringWidth(message) * fontSize / 1000.0F;
            float startX = 100.0F;
            float startY = 100.0F;
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.OVERWRITE, false);
            contentStream.transform(new Matrix(0.0F, 1.0F, -1.0F, 0.0F, pageWidth, 0.0F));
            contentStream.setFont(font, fontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);
            contentStream.showText(message);
            contentStream.newLineAtOffset(0.0F, 100.0F);
            contentStream.showText(message);
            contentStream.newLineAtOffset(100.0F, 100.0F);
            contentStream.showText(message);
            contentStream.endText();
            contentStream.moveTo(startX - 2.0F, startY - 2.0F);
            contentStream.lineTo(startX - 2.0F, startY + 200.0F + fontSize);
            contentStream.stroke();
            contentStream.moveTo(startX - 2.0F, startY + 200.0F + fontSize);
            contentStream.lineTo(startX + 100.0F + stringWidth + 2.0F, startY + 200.0F + fontSize);
            contentStream.stroke();
            contentStream.moveTo(startX + 100.0F + stringWidth + 2.0F, startY + 200.0F + fontSize);
            contentStream.lineTo(startX + 100.0F + stringWidth + 2.0F, startY - 2.0F);
            contentStream.stroke();
            contentStream.moveTo(startX + 100.0F + stringWidth + 2.0F, startY - 2.0F);
            contentStream.lineTo(startX - 2.0F, startY - 2.0F);
            contentStream.stroke();
            contentStream.close();
            doc.save(outfile);
        } finally {
            if (doc != null) {
                doc.close();
            }

        }

    }

    private void makeLandscape() throws IOException {
        CreateLandscapePDF createLandscapePDF = new CreateLandscapePDF();

        createLandscapePDF.doIt("hello","c:/projects/playground/newpdf");

    }

    public void testDrawTextLineText() throws IOException
    {
        PDFont font = PDType1Font.HELVETICA_OBLIQUE;

        float fontSize = 11;
        float fontHeight = fontSize;
        float leading = 42;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date();

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);

        doc.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        contentStream.setFont(font, fontSize);



        float yCordinate = page.getCropBox().getUpperRightY() - 30;
        float startX = page.getCropBox().getLowerLeftX() + 30;
        float endX = page.getCropBox().getUpperRightX() - 30;
        float endPage = 120;

        contentStream.beginText();
        contentStream.newLineAtOffset(startX, yCordinate);
        contentStream.showText("Entry Form – Header");
        yCordinate -= fontHeight;  //This line is to track the yCordinate
        contentStream.newLineAtOffset(0, -leading);
        yCordinate -= leading;
        contentStream.showText("Date Generated: " + dateFormat.format(date));
        yCordinate -= fontHeight;
        contentStream.endText(); // End of text mode

        contentStream.setLineWidth(.5f);
        contentStream.moveTo(startX, yCordinate);
        contentStream.lineTo(endX, yCordinate );
        contentStream.stroke();

        logger.info("startX " + Float.toString(startX));
        logger.info("endX " + Float.toString(endX));
        logger.info("yCordinate " + Float.toString(yCordinate));

        contentStream.moveTo(startX , yCordinate  );
        contentStream.lineTo(startX  , yCordinate + 20 );
        contentStream.stroke();

        contentStream.moveTo(startX , yCordinate + 20);
        contentStream.lineTo(endX, yCordinate + 20 );
        contentStream.stroke();

        contentStream.moveTo(endX, yCordinate);
        contentStream.lineTo(endX, yCordinate + 20 );
        contentStream.stroke();

        yCordinate -= leading;

        contentStream.beginText();
        contentStream.newLineAtOffset(startX, yCordinate);
        contentStream.showText("Name: XXXXX");
        contentStream.endText();

        logger.info(Float.toString(endPage));

        contentStream.beginText();
        contentStream.newLineAtOffset(startX, endPage);
        contentStream.showText("End Page 1");
        contentStream.endText();

        PDRectangle pageSize = page.getMediaBox();

        logger.info("yCordinate: " + Float.toString(yCordinate));
        logger.info(Float.toString(page.getCropBox().getHeight()));
        contentStream.beginText();
        contentStream.newLineAtOffset(startX, pageSize.getLowerLeftY() + 20);
        contentStream.showText("End Page 2 " + pageSize.getHeight());
        contentStream.endText();

        contentStream.close();
        doc.save(new File("C:/PdfBox_Examples", "textLineText.pdf"));
    }
}
