package crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedInputStream;

import javax.swing.JTextArea;

public class DownloadComickbook implements DownloadFileUtils,Runnable{
		
	static int count = 1;
	String path = "D:/Users/KURUMI-PANNTSU/Desktop/" ;
	String distribute;
	Pattern pattern = Pattern.compile("\\d{1,3}.[A-Za-z]{3}");
	File sf;
	Properties properties;
	JTextArea showArea;
	JTextArea progress;
	URLConnection connetcion;
	String title = null;
	
	public DownloadComickbook(){};
	public DownloadComickbook(Properties properties) {
		this.properties = properties;
		// TODO Auto-generated constructor stub
	}
	public DownloadComickbook(String distribute,Properties properties,JTextArea showArea,String title,JTextArea progress) {
		this.distribute = distribute;
		this.properties = properties;
		this.showArea = showArea;
		this.title = title;
		this.progress = progress;
		// TODO Auto-generated constructor stub
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
		path = properties.getLocation();
		sf = new File(path+"\\"+bookname);
		if (!sf.exists()) {
			System.out.println("创建文件夹"+sf.getPath());
			sf.mkdir();
			return false;
		} else {
			return true;
		}
	}
	@Override
	public void downloadNHENTAI(String bookurl) 
	{
		
		// create the dircotry if it doesnot exist
		try {
			InputStream in;
			OutputStream out;
			URL url = new URL(bookurl);
			int stringlength = bookurl.length();
			URLConnection connetcion = url.openConnection();
			connetcion.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36");
			connetcion.connect();
			in = connetcion.getInputStream();
			Matcher match = pattern.matcher(bookurl);
			match.find();
		    path = properties.getLocation();
			OutputStream os = new FileOutputStream(path +"\\"+ title
					+ "/" + match.group());
			// 开始读取
			int len;
			while ((len = in.read(cache)) != -1) {
				os.write(cache, 0, len);
			}
			// 完毕，关闭所有链接
			showArea.append(match.group() + " has done  " + IncreaseCount()
					+ "/" + (properties.getPages() - 1) + "\n");
			progress.setText("    "+count+"/"+properties.getPages());
			os.close();
			in.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (count == properties.getPages()) {
			showArea.append("this book is done,THX");
			resetCount();
		}
	}

	@Override
	public void run() {		
		downloadNHENTAI(distribute);
		
	}

}
