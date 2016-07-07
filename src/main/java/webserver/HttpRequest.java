package webserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
	
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private InputStream in;
	
	private String method;
	
	private String path;
	
	private Map<String, String> parameters;
	
	private Map<String, String> headers;
	
	public HttpRequest(InputStream in) {
		this.in = in;
		this.init();
	}
	
	private void init() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line;
			while ( (line = br.readLine()) != null) {
				log.debug("request line : {}", line);
				setMethod(line);
				setPath(line);
				setParameter(line);
				setHeaders(line);
			}
		} catch (Exception e) {
			e.getStackTrace();
			log.error(e.getMessage());
		}
	}

	private void setMethod(String line) {
		if (line.indexOf(": ") == -1) {
			String[] tokens = line.split(" ");
			this.method = tokens[0];
		}
	}
	
	public String getMethod() {
		return this.method;
	}
	
	private void setPath(String line) {
		if (line.indexOf(": ") == -1) {
			String[] tokens = line.split(" ");
			this.path = tokens[1].split("\\?")[0];
		}
	}
	
	public String getPath() {
		return this.path;
	}
	
	private void setParameter(String line) {
		if (line.indexOf(": ") == -1) {
			String[] tokens = line.split(" ");
			String[] parameters = tokens[1].split("\\?")[1].split("&");
			for (String keyVal : parameters) {
				String[] keyVals = keyVal.split("="); 
				this.getParameters().put(keyVals[0], keyVals[1]);
			}
		}
	}
	
	public Map<String, String> getParameters() {
		if (parameters == null) {
			this.parameters = new HashMap<String, String>();
		}
		return parameters;
	}
	
	public String getParameter(String key) {
		return this.getParameters().get(key);
	}

	private void setHeaders(String line) {
		if (line.indexOf(": ") > -1) {
			String[] keyVals = line.split(": "); 
			this.getHeaders().put(keyVals[0], keyVals[1]);
		}
	}
	
	public Map<String, String> getHeaders() {
		if (headers == null) {
			this.headers = new HashMap<String, String>();
		}
		return headers;
	}
	
	public String getHeader(String key) {
		return this.getHeaders().get(key);
	}

}
