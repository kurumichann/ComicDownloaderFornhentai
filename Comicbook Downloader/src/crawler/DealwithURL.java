package crawler;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DealwithURL implements DealwithURLUtils{
	BlockingQueue<Page> queue;
	Document parser;

	final Pattern JPGPATTERNER = Pattern.compile("//t.*jpg");
	final Pattern PNGPATTERNER = Pattern.compile("//t.*png");
	final Pattern GIFPATTERNER = Pattern.compile("//t.*gif");
	Matcher match;
	private int retryCnt;
	private int MAXCNT = 5;
	
	/*  
	 *nhentai
	 *param  String url
	 * http://t.nhentai.net/galleries/891585/1t.jpg
	 * http://i.nhentai.net/galleries/891585/1.jpg
	 * http://t.nhentai.net/galleries/891585/27t.jpg
	 * http://i.nhentai.net/galleries/891585/27.jpg   
	 * https//t.nhentai.net/galleries/959435/1t.jpg
	 * https//i.nhentai.net/galleries/959435/1.jpg
	 */
	public DealwithURL(BlockingQueue<Page> queue){
		this.queue = queue;
	}
	
	public Book getSource(String url)
	{
		Book book = new Book();
		book.setUrl(url);
		System.out.println(url);
	     try {
			parser = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36").get();
		} catch (IOException e) {
			if(++retryCnt != MAXCNT){
				System.out.println("retry......");
				getSource(url);
			}
			e.printStackTrace();
		}
	    /*
	     * title
	     * 
	     */
	    String title = parser.getElementById("info").child(1).tagName();
	    if(title.equals("h2")){
	    	title = parser.getElementById("info").child(1).ownText();
	    }
	    else{
	    	title = parser.getElementById("info").child(0).ownText();
	    }
	    
	    book.setTitle(title.replaceFirst(":","").replaceFirst("\\+","").replaceFirst("/","").
	    		replaceAll("\\?", "").replaceAll("\\|", "").replaceAll("/", "").replaceAll(":", "").replace("*", ""));
	    //get pages in a book
	    Iterator<Element> iterator =  parser.getElementsByAttributeValue("class", "lazyload").iterator();
	    while(iterator.hasNext())
	    {
	    	String temp  = iterator.next().toString();
	    	if(temp.contains("thumb")||temp.contains("cover")){
	    		continue;
	    	}
	    	match = JPGPATTERNER.matcher(temp);
	    	if(match.find())
	    	{
	    		book.getUrls().add("https:"+match.group().replaceAll("//t", "//i").replaceAll("t.jpg", ".jpg"));	    
	    		continue;
	    	}
	    	
	    	match = PNGPATTERNER.matcher(temp);
	    	if(match.find())
	    	{
	    		book.getUrls().add("https:"+match.group().replaceAll("//t", "//i").replaceAll("t.png", ".png"));	 
	    		continue;
	    	}
	    	
	    	match = GIFPATTERNER.matcher(temp);	    			
	    	if(match.find()){
	    		book.getUrls().add("https:"+match.group().replaceAll("//t", "//i").replaceAll("t.gif", ".gif"));
	    	}
	    		 
	    }
	    
	    for(String pageUrl : book.getUrls()){
	    	Page page = new Page();
	    	page.setUrl(pageUrl);
	    	page.setTitle(book.getTitle());
	    	queue.add(page);
	    }
	    
	    return book;
	}	
	
}
