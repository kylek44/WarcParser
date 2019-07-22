package helpers;

/**
 * author: Mark Watson
 */

/**
 * a sample callback class for handling WARC record data by implementing IProcessWarcRecord interface
 */
public class SampleProcessWarcRecord implements IProcessWarcRecord {
    @Override
    public void process(String url, String content) {
        System.out.println("url: " + url);
//        System.out.println("content: " + url + "\n\n" + content + "\n");
//        System.out.println("content: " + "\n\n" + content + "\n\n\n\n\n");
    }

    @Override
    public void done() {
        // place any code hear to save data, etc.
    }
}