package crawler;

import java.io.InputStream;
import java.util.LinkedList;

import javax.swing.JTextArea;

public interface DownloadFileUtils {	
	byte[] cache = new byte[2048];  
	public boolean isDownloaded(String bookname);
	public void downloadNHENTAI(String book);

}
