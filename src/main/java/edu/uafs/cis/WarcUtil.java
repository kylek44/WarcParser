package edu.uafs.cis;

import helpers.WarcHTMLResponseRecord;
import helpers.WarcRecord;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class WarcUtil {
    private static Pattern httpStartPattern = Pattern.compile("^[hH][tT][tT][pP][sS]?:?//?.*");
    private static int fileNumber = 0;
    private static int directoryNumber = 0;
    private static boolean directoryIsMade = false;

    public static boolean process(String filename, String fullPath, String directoryName) {
        try {
            DataInputStream dataStream = new DataInputStream(new FileInputStream(filename));
            WarcRecord currentWarcRecord;

            while ((currentWarcRecord = WarcRecord.readNextWarcRecord(dataStream)) != null) {
                if (currentWarcRecord.getHeaderRecordType().equals("response")) {
                    WarcHTMLResponseRecord htmlResponseRecord = new WarcHTMLResponseRecord(currentWarcRecord);
                    String recordContent = htmlResponseRecord.getRawRecord().getContentUTF8();
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    private static void saveHtml(String record, String path, String name) throws IOException {
        if (fileNumber > 9999) {
            fileNumber = 0;
            directoryNumber++;
            directoryIsMade = false;
        }
        String directory = String.format(path + "%d" + name, directoryNumber);
        if (!directoryIsMade) {
            new File(directory).mkdirs();
        }

    }

    private static void checkPath(String path) {

    }
}
