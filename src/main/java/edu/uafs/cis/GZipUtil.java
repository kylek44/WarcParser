package edu.uafs.cis;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

public class GZipUtil {

    public static boolean isGZipped(File file) {
        int magic = 0;

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            magic = raf.read() & 0xff | ((raf.read() << 8) & 0xff00);
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return magic == GZIPInputStream.GZIP_MAGIC;
    }

}
