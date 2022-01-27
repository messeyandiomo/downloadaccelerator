
public class RequestProperties {
	
	private String userAgent = "";
	private String cookies = "";
	private String domain = "";

	public RequestProperties(String domain, String useragent, String cookies) {
		// TODO Auto-generated constructor stub
		this.setCookies(cookies);
		this.setUserAgent(useragent);
		this.setDomain(domain);
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}
