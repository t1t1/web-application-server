package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			
/*			
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = br.readLine();
			if (line == null) {
				return;
			}

			log.debug("request line : {}", line);
			String[] tokens = line.split(" ");

			int contentLength = 0;
			boolean logined = false;
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
*/			
			
//			HttpRequest httpRequest = new HttpRequest();
//			httpRequest.execute(in, br, tokens, contentLength, logined);
			
//			HttpResponse httpResponse = new HttpResponse();
//			httpResponse.execute(out, br, tokens, contentLength, logined);
			
			HttpRequest httpRequest = new HttpRequest(in);
			

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

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
