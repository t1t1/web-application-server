package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			response(in, out);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * @param dos
	 */
	private void response(InputStream in, OutputStream out) {
		DataOutputStream dos = new DataOutputStream(out);
		byte[] body;
		try {
			String url = getUrl(in);
			body = getBody(url);
			response200Header(dos, body.length);
			responseBody(dos, body);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param in
	 * @return
	 */
	private String getUrl(InputStream in) {
		String url = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String line;
			while ( (line = br.readLine()) != null) {
				// TODO 하드코딩 같음
				log.debug(line);
				if (line.indexOf("HTTP/1.1") > -1) {
					// TODO 하드코딩 같음
					url = getRealUrl(line);
				}
				if (line.isEmpty()) break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * @param line
	 * @return
	 */
	private String getRealUrl(String line) {
		log.debug(line);
		String url;
		url = line.split(" ")[1];
		String[] params = (url.substring(url.indexOf("?")+1)).split("&");
		
		String password;
		String name;
		String email;
		
		Map<String, String> map = new HashMap<String, String>();
		
		for (String param : params) {
			String[] keyVal = param.split("=");
			map.put(keyVal[0], keyVal[1]);
		}
		
		User user = new User(map.get("userId"), map.get("userId"), map.get("userId"), map.get("userId"));
		log.debug(user.toString());
		
		return url;
	}

	/**
	 * @return
	 * @throws IOException 
	 */
	private byte[] getBody(String url) throws IOException {
		return Files.readAllBytes(getFile(url));
	}

	/**
	 * @param url
	 * @return
	 */
	private Path getFile(String url) {
		File file = new File("./webapp" + url);
		Path path = null;
		// TODO 파일 없을 땐 어떻게 response?
		if (file.isFile()) {
			path = file.toPath();
		}
		return path;
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
