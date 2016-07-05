package webserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private InputStream in;
	
	public HttpRequest(InputStream in) {
		this.in = in;
	}

	public String getMethod() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = br.readLine();
			if (line == null) {
				return null;
			}
			
			log.debug("request line : {}", line);
			String[] tokens = line.split(" ");
			return tokens[1];
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	public void getPath() {
	}
	
	public void getHeader(String str) {
	}
	
	public void getParameter(String str) {
	}

/*	
	public void execute(InputStream in, BufferedReader br, String[] tokens, int contentLength, boolean logined) {
		
		try {
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = br.readLine();
			if (line == null) {
				return;
			}

			log.debug("request line : {}", line);
			tokens = line.split(" ");

			contentLength = 0;
			logined = false;
			while (!line.equals("")) {
				line = br.readLine();
				log.debug("header : {}", line);
				
				if (line.contains("Content-Length")) {
					contentLength = getContentLength(line);
				}
				
				if (line.contains("Cookie")) {
					logined = isLogin(line);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
	}
*/	
	private boolean isLogin(String line) {
		String[] headerTokens = line.split(":");
		Map<String, String> cookies = HttpRequestUtils.parseCookies(headerTokens[1].trim());
		String value = cookies.get("logined");
		if (value == null) {
			return false;
		}
		return Boolean.parseBoolean(value);
	}

	private int getContentLength(String line) {
		String[] headerTokens = line.split(":");
		return Integer.parseInt(headerTokens[1].trim());
	}

}
