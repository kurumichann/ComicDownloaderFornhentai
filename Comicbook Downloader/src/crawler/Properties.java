package crawler;

public class Properties {
	private String host;
	private String port;
	private String url;
	private String location;
	private int pages = 0;
	
	
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		if(this.pages == 0){
			this.pages = pages;			
		}else{
			this.pages += pages;
		}
		 
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


}
