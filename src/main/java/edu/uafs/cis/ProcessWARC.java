//package edu.uafs.cis;
//
//import helpers.WarcHTMLResponseRecord;
//import helpers.WarcRecord;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
////import java.util.zip.GZIPInputStream;
//
//public class ProcessWARC {
//    private static final char NL = '\n';
//    private static final String saveString = "%04duafs.txt";
//    private static Pattern httpStartPattern = Pattern.compile("^[hH][tT][tT][pP][sS]?:?//?.*");
//
//    public static void main(String[] args) throws IOException {
//        String inputWarcFile = "dataset1.little.warc";
////        GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(inputWarcFile));
//        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(inputWarcFile));
//        WarcRecord currentWarcRecord;
//        int fileNumber = 0;
//
//        while ((currentWarcRecord = WarcRecord.readNextWarcRecord(dataInputStream)) != null) {
//            if (currentWarcRecord.getHeaderRecordType().equals("response")) {
//                WarcHTMLResponseRecord htmlResponseRecord = new WarcHTMLResponseRecord(currentWarcRecord);
//                String recordContent = htmlResponseRecord.getRawRecord().getContentUTF8();
//                saveHTML(recordContent, fileNumber++);
////                String htmlRecord = getHTML(recordContent);
////                saveRecord(htmlRecord, fileNumber++);
//            }
//        }
//    }
//
//    private static void saveHTML(String record, int fileNumber) throws IOException {
//        BufferedReader in = new BufferedReader(new StringReader(record));
//        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(String.format(saveString, fileNumber)), StandardCharsets.UTF_8);
//        String line;
//        boolean foundStartOfHttpHeader = false;
//        boolean foundEndOfHttpHeader = false;
//
//        while ((line = in.readLine()) != null) {
//            Matcher matcher = httpStartPattern.matcher(line);
//            if (matcher.find()) {
//                foundStartOfHttpHeader = true;
//                break;
//            }
//        }
//
//        while ((line = in.readLine()) != null && !foundEndOfHttpHeader) {
//            if (line.length() == 0) {
//                foundEndOfHttpHeader = true;
//                while ((line = in.readLine()) != null) {
//                    if (line.length() > 0) {
//                        writer.write(line);
//                        writer.write(NL);
//                        break;
//                    }
//                }
//            }
//        }
//
//        while ((line = in.readLine()) != null) {
//            writer.write(line);
//            writer.write(NL);
//        }
//
//
//        if (!foundStartOfHttpHeader) {
//            writer.write("Error no HTTP header");
//        }
//
//        writer.close();
//        in.close();
//    }
//
////    private static void saveHTML(String record, int fileNumber) throws IOException {
////        BufferedReader in = new BufferedReader(new StringReader(record));
////        PrintWriter out = new PrintWriter(new FileWriter(String.format(saveString, fileNumber)));
////        StringBuffer backup = new StringBuffer();
////        String line;
////        boolean foundStartOfHttpHeader = false;
////        boolean foundEndOfHttpHeader = false;
////
////        while ((line = in.readLine()) != null) {
////            Matcher matcher = httpStartPattern.matcher(line);
////            if (matcher.find()) {
////                foundStartOfHttpHeader = true;
////                break;
////            }
////            backup.append(line).append("\n");
////        }
////
////        while ((line = in.readLine()) != null && !foundEndOfHttpHeader) {
////            if (line.length() == 0) {
////                foundEndOfHttpHeader = true;
////                while ((line = in.readLine()) != null) {
////                    if (line.length() > 0) {
////                        out.println(line);
////                        break;
////                    }
////                }
////            }
////        }
////
////        while ((line = in.readLine()) != null) {
////            out.println(line);
////        }
////
////
////        if (!foundStartOfHttpHeader) {
////            out.println(backup);
////        }
////
////        out.close();
////        in.close();
////    }
//
//    private static String getHTML(String record) throws IOException {
//        BufferedReader in = new BufferedReader(new StringReader(record));
//        StringBuilder result = new StringBuilder();
//        StringBuilder backup = new StringBuilder();
//        String line;
//        boolean foundStartOfHttpHeader = false;
//        boolean foundEndOfHttpHeader = false;
//
//        while ((line = in.readLine()) != null) {
//            Matcher matcher = httpStartPattern.matcher(line);
//            if (matcher.find()) {
//                foundStartOfHttpHeader = true;
//                break;
//            }
//            backup.append(line).append("\n");
//        }
//
//        while ((line = in.readLine()) != null && !foundEndOfHttpHeader) {
//            if (line.length() == 0) {
//                foundEndOfHttpHeader = true;
//                while ((line = in.readLine()) != null) {
//                    if (line.length() > 0) {
//                        result.append(line).append("\n");
//                        break;
//                    }
//                }
//            }
//        }
//
//        while ((line = in.readLine()) != null) {
//            result.append(line).append("\n");
//        }
//
//        in.close();
//
//        if (!foundStartOfHttpHeader) {
//            return backup.toString();
//        }
//
//        return result.toString();
//    }
//
//    private static void saveRecord(String record, int filePosition) throws IOException {
//        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(String.format(saveString, filePosition)), StandardCharsets.UTF_8)) {
//            writer.write(record);
//        }
//    }
//}
