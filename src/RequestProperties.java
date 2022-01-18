
public class RequestProperties {
	
	private String userAgent = "";
	private String cookies = "";

	public RequestProperties(String useragent, String cookies) {
		// TODO Auto-generated constructor stub
		this.setCookies(cookies);
		this.setUserAgent(useragent);
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

}
