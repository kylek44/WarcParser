package edu.uafs.cis;

import helpers.WarcHTMLResponseRecord;
import helpers.WarcRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    private static final char NL = '\n';
    private static final String saveString = "%06duafs.html";
    private static Pattern httpStartPattern = Pattern.compile("^[hH][tT][tT][pP][sS]?:?//?.*");

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("InputFile OutputDirectoryName StartIndex");
        } else {
            try {
                String inputWarcFile = args[0];
                String directoryName = fixDirectoryName(args[1]);
                String mappingFileName = "urls.csv";
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(inputWarcFile));
                WarcRecord warcRecord;
                WarcHTMLResponseRecord htmlRecord;
                String targetUri;
                String recordContent;
                int index = Integer.parseInt(args[2]);

                new File(directoryName).mkdirs();

                BufferedWriter out = new BufferedWriter(new FileWriter(directoryName + mappingFileName, true));

                try {
                    while ((warcRecord = WarcRecord.readNextWarcRecord(dataInputStream)) != null) {
                        if (warcRecord.getHeaderRecordType().equals("response")) {
                            htmlRecord = new WarcHTMLResponseRecord(warcRecord);
                            targetUri = htmlRecord.getTargetURI();
                            recordContent = htmlRecord.getRawRecord().getContentUTF8();
                            saveHTML(recordContent, directoryName, index);
                            out.write(String.format(saveString, index) + "," + targetUri);
                            out.newLine();
                            index++;
                        }
                    }
                } catch(IOException e) {

                }

                out.close();
//                saveFileToURL(fileToUrl, directoryName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveHTML(String record, String directoryName, int fileNumber) throws IOException {
        BufferedReader in = new BufferedReader(new StringReader(record));
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(directoryName + "/" + String.format(saveString, fileNumber)), StandardCharsets.UTF_8);
        String line;
        boolean foundStartOfHttpHeader = false;
        boolean foundEndOfHttpHeader = false;

        while ((line = in.readLine()) != null) {
            Matcher matcher = httpStartPattern.matcher(line);
            if (matcher.find()) {
                foundStartOfHttpHeader = true;
                break;
            }
        }

        while ((line = in.readLine()) != null && !foundEndOfHttpHeader) {
            if (line.length() == 0) {
                foundEndOfHttpHeader = true;
                while ((line = in.readLine()) != null) {
                    if (line.length() > 0) {
                        writer.write(line);
                        writer.write(NL);
                        break;
                    }
                }
            }
        }

        while ((line = in.readLine()) != null) {
            writer.write(line);
            writer.write(NL);
        }


        if (!foundStartOfHttpHeader) {
            writer.write("Error no HTTP header");
        }

        writer.close();
        in.close();
    }

    private static String fixDirectoryName(String directoryName) {
        if (directoryName.endsWith("/")) {
            return directoryName;
        }
        return directoryName + "/";
    }
}
