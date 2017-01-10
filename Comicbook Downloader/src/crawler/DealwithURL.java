package crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DealwithURL implements DealwithURLUtils{
	visitQuaue Quaue = new visitQuaue();
	Document parser;

    LinkedList<String> result;
	Pattern addrEndWithJpg = Pattern.compile("//t.*jpg");
	Pattern addrEndWithPng = Pattern.compile("//t.*png");
	Pattern addrEndWithgif = Pattern.compile("//t.*gif");
	Matcher match;
	
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
	public LinkedList<String> getSource(String url)
	{
		
		System.out.println(url);
	     try {
			parser = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getSource(url);
		}
	    result = new LinkedList<String>();
	    /*
	     * add title as the fisrst one
	     * remove special symbol
	     */
	    String title = parser.getElementById("info").child(1).tagName();
	    if(title.equals("h2")){
	    	title = parser.getElementById("info").child(1).ownText();
	    }
	    else{
	    	title = parser.getElementById("info").child(0).ownText();
	    }
	    //去除特殊符号
	    result.add(title.replaceFirst(":","").replaceFirst("\\+","").replaceFirst("/","").
	    		replaceAll("\\?", "").replaceAll("\\|", "").replaceAll("/", "").replaceAll(":", "").replace("*", ""));
	    //iterator in jsoup
	    Iterator<Element> iterator =  parser.getElementsByAttributeValue("class", "lazyload").iterator();
	    /*
	     * 处理jpg，gif，png格式，若还有其他格式再加
	     */
	    while(iterator.hasNext())
	    {
	    	String temp  = iterator.next().toString();
	    	match = addrEndWithJpg.matcher(temp);
	    	if(match.find())
	    	{
	    		result.add("https:"+match.group().replaceAll("//t", "//i").replaceAll("t.jpg", ".jpg"));	    
	    		continue;
	    	}else{
	    		match = addrEndWithPng.matcher(temp);
	    	}
	    	if(match.find())
	    	{
	    		result.add("https:"+match.group().replaceAll("//t", "//i").replaceAll("t.png", ".png"));	 
	    		continue;
	    	}else{
	    		match = addrEndWithgif.matcher(temp);	    			    		
	        }
	    	match.find();
	    	result.add("https:"+match.group().replaceAll("//t", "//i").replaceAll("t.gif", ".gif"));	 
	    }

	    			      	   	   
	    return result;
	}	
	
}
