package webserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class HttpRequest {
	
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private InputStream in;
	
	private Map<String, String> headers;
	
	private Map<String, String> parameters;
	
	public HttpRequest(InputStream in) {
		this.in = in;
	}
	
	public Map<String, String> getHeaders() {
		if (headers == null) {
			this.headers = new HashMap<String, String>();
		}
		return headers;
	}
	
	public Map<String, String> getParameters() {
		if (parameters == null) {
			this.parameters = new HashMap<String, String>();
		}
		return parameters;
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
			
			String method = tokens[0];
//			String path = getPath();
			
			return method;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	public String getPath() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = br.readLine();
			if (line == null) {
				return null;
			}
			
			log.debug("request line : {}", line);
			String[] tokens = line.split(" ");
			
//			String method = tokens[0];
//			String path = tokens[1].split("?")[0];
			String path = tokens[1].split("\\?")[0];
			
			return path;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	public String getHeader(String key) {
		
		try {
			int cnt = 0;
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line;
			log.debug(String.valueOf(cnt++));
			while ( (line = br.readLine()) != null) {
				log.debug(String.valueOf(cnt++));
				log.debug("request line : {}", line);
				log.debug("line.indexOf(\": \") : {}", line.indexOf(": "));
//				if (!line.contains("HTTP")) {
//				if (line.contains(": ")) {
				if (line.indexOf(": ") > -1) {
					log.debug(String.valueOf(cnt++));
					String[] keyVal = line.split(": ");
					log.debug("keyVal.length : {}", keyVal.length);
//					this.headers.put(keyVal[0], keyVal[1]);
//					log.debug("this.getHeaders: {}", this.getHeaders().toString());
					this.getHeaders().put(keyVal[0], keyVal[1]);
				}
//				log.debug("this.getHeaders: {}", this.getHeaders().toString());
				log.debug(String.valueOf(cnt++));
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			log.error(e.getMessage());
		}
		
		return this.getHeaders().get(key);
		
	}
	
	public String getParameter(String key) {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = br.readLine();
			if (line == null) {
				return null;
			}
			
			log.debug("request line : {}", line);
			String[] tokens = line.split(" ");
			log.debug("tokens: {}", tokens[1]);
			
//			String[] parameters = tokens[1].split("?")[1].split("&");
			String[] parameters = tokens[1].split("\\?")[1].split("&");
			log.debug("parameters: {}", parameters);
			for (String keyVal : parameters) {
				String[] keyVals = keyVal.split("="); 
				this.getParameters().put(keyVals[0], keyVals[1]);
				log.debug("getParameters: {}" , this.getParameters().toString());
			}
			
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		return this.getParameters().get(key);
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
