package crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

public class DownloadComickbook implements DownloadFileUtils,Runnable{
		
	static int count = 1;
	String path = "D:/Users/KURUMI-PANNTSU/Desktop/" ;
	String distribute;
	Pattern pattern = Pattern.compile("\\d{1,3}.[A-Za-z]{3}");
	File sf;
	Configuration conf;
	JTextArea showArea;
	JTextArea progress;
	URLConnection connetcion;
	BlockingQueue<Page> queue;
	
	public DownloadComickbook(){};
	public DownloadComickbook(Configuration conf) {
		this.conf = conf;
	}
	public DownloadComickbook(Configuration conf,JTextArea showArea,JTextArea progress,BlockingQueue<Page> queue) {
		this.conf = conf;
		this.showArea = showArea;
		this.progress = progress;
		this.queue = queue;
	}

	public synchronized static int IncreaseCount()
	{
		return count++;
	}
	public  void resetCount()
	{
		count=1;
	}
	@Override
	public boolean isDownloaded(String bookname)
	{
		path = conf.getLocation();
		sf = new File(path+"\\"+bookname);
		if (!sf.exists()) {
			System.out.println("download into"+sf.getPath());
			sf.mkdir();
			return false;
		} else {
			return true;
		}
	}
	@Override
	public void downloadNHENTAI() 
	{
		
		// create the dircotry if it doesnot exist
		try {
			InputStream in = null;
			OutputStream os = null;
			while(!queue.isEmpty()){
				Page page = queue.poll();
				URL url = new URL(page.getUrl());
				URLConnection connetcion = url.openConnection();
				connetcion.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36");
				connetcion.connect();
				in = connetcion.getInputStream();
				Matcher match = pattern.matcher(page.getUrl());
				match.find();
			    path = conf.getLocation();
				os = new FileOutputStream(path +"\\"+ page.getTitle()
						+ "/" + match.group());
				// download
				int len;
				while ((len = in.read(cache)) != -1) {
					os.write(cache, 0, len);
				}
				showArea.append(match.group() + " in <<"+page.getTitle()+">> has done \n");
				progress.setText(++count+"/"+conf.getPages());
			}
			if(os != null){
				os.close();
			}
			if(in != null){
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
		}
		if (count == conf.getPages()) {
			showArea.append("this book is done,THX");
			resetCount();
		}
	}

	@Override
	public void run() {		
		downloadNHENTAI();
		
	}

}
