package crawler;

import java.util.LinkedList;

public class Book {
	
	private String title;
	private int pagesCnt;
	private LinkedList<String> urls = new LinkedList<>();
	private String url;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPagesCnt() {
		return this.urls.size();
	}
	public LinkedList<String> getUrls() {
		return urls;
	}
	public void setUrls(LinkedList<String> urls) {
		this.urls = urls;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
