package crawler;

public interface DownloadFileUtils {	
	byte[] cache = new byte[2048];  
	public boolean isDownloaded(String bookname);
	public void downloadNHENTAI();

}
