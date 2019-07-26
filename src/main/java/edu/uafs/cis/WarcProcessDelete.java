package edu.uafs.cis;

import helpers.WarcHTMLResponseRecord;
import helpers.WarcRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WarcProcessDelete {

    private static final String SAVE_STRING = "%04d-%08d.html";
    private static final String DIRECTORY_STRING = "%04d-warc";
    private static final String FILE_INDEX_STRING = "%08d.html";
    private static Pattern httpStartPattern = Pattern.compile("^[hH][tT][tT][pP][sS]?:?//?.*");

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("First arg: Directory containing WARC files to process");
            System.out.println("Second arg: Directory name to place processed files");
            System.out.println("Third arg: Index number for file start");
        } else {
            try {
                File[] files = new File(args[0]).listFiles();
                String outDirectory = checkDirectoryName(args[1]);
                String mapFile;
                DataInputStream dataInputStream;
                WarcRecord warcRecord;
                WarcHTMLResponseRecord htmlResponseRecord;
                String targetUri;
                String recordContent;
                int directoryIndex;
                long fileIndex = Long.parseLong(args[2]);

                new File(outDirectory).mkdirs();
                PrintWriter logs = new PrintWriter(new FileWriter(outDirectory + "errors.log"));
                BufferedWriter urls = new BufferedWriter(new FileWriter(outDirectory + "urls.txt", true));

                if (files == null) {
                    System.out.println("Input directory not found");
                    System.exit(1);
                }

                for (File file : files) {
                    try {
                        dataInputStream = new DataInputStream(new FileInputStream(file));
                        directoryIndex = getDirectoryIndex(file.getName());
                        new File(outDirectory + String.format(DIRECTORY_STRING, directoryIndex)).mkdirs();

                        while ((warcRecord = WarcRecord.readNextWarcRecord(dataInputStream)) != null) {
                            if (warcRecord.getHeaderRecordType().toLowerCase().equals("response")) {
                                htmlResponseRecord = new WarcHTMLResponseRecord(warcRecord);
                                targetUri = htmlResponseRecord.getTargetURI();
                                recordContent = htmlResponseRecord.getRawRecord().getContentUTF8();
                                saveHtml(recordContent, outDirectory, directoryIndex, fileIndex);
                                urls.write(String.format("%s,%s,%s\n", targetUri, String.format(DIRECTORY_STRING, directoryIndex), String.format(SAVE_STRING, directoryIndex, fileIndex)));
                                fileIndex++;
                            }
                        }
                    } catch (IOException e) {
                        logs.printf("Error processing file: %s\n", file.getAbsolutePath());
                        e.printStackTrace();
                    }
                }

                urls.close();
                logs.close();

            } catch (IOException | NullPointerException | SecurityException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println("Index starts must be integers");
            }
        }
    }

    private static int getDirectoryIndex(String fileName) {
        StringBuilder numbers = new StringBuilder();

        for (int i = 15; i < fileName.length(); i++) {
            if (Character.isDigit(fileName.charAt(i))) {
                numbers.append(fileName.charAt(i));
            } else {
                break;
            }
        }

        return Integer.parseInt(numbers.toString());
    }

    private static void saveHtml(String content, String outDirectory, int directoryIndex, long fileIndex) throws IOException {
        BufferedReader in = new BufferedReader(new StringReader(content));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outDirectory + String.format(DIRECTORY_STRING + "/", directoryIndex) + String.format(SAVE_STRING, directoryIndex, fileIndex)), StandardCharsets.UTF_8));
        boolean foundStartHttpHeader = false;
        boolean foundEndHttpHeader = false;
        boolean foundStartHtml = false;
        String line;
//        String fullPath = new File().getAbsolutePath()

        while ((line = in.readLine()) != null && !foundStartHttpHeader) {
            Matcher matcher = httpStartPattern.matcher(line);
            if (matcher.find()) {
                foundStartHttpHeader = true;
            }
        }

        while ((line = in.readLine()) != null && !foundEndHttpHeader) {
            if (line.isEmpty()) {
                foundEndHttpHeader = true;
                while ((line = in.readLine()) != null && !foundStartHtml) {
                    if (!line.isEmpty()) {
                        foundStartHtml = true;
                    }
                }
            }
        }

        if (line != null) {
            out.write(line);
            out.newLine();

            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }
        } else {
            out.write("Error finding HTML");
            out.newLine();
        }



        in.close();
        out.close();
    }

    private static String checkDirectoryName(String directoryName) {
        if (directoryName.endsWith("/")) {
            return directoryName;
        }

        return directoryName + "/";
    }

}
