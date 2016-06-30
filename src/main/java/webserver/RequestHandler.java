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
			String fileName = getFileName(in);
			body = getBody(fileName);
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
	private String getFileName(InputStream in) {
		String rtnStr = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String line;
			while ( (line = br.readLine()) != null) {
				System.out.println(line);
				if (line.indexOf("HTTP/1.1") > -1) {
					String[] tokens = line.split(" ");
					rtnStr = tokens[1];
					System.out.println(rtnStr);
				}
				if (line.isEmpty()) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(rtnStr);
		return rtnStr;
	}

	/**
	 * @return
	 * @throws IOException 
	 */
	private byte[] getBody(String fileName) throws IOException {
/*		
		FileInputStream fis = new FileInputStream("./" + fileName);
		InputStreamReader r = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(r);
		while (br.readLine() != null) {
			String line = br.readLine();
			System.out.println(line);
		}
*/		
		return Files.readAllBytes(new File("./webapp" + fileName).toPath());
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
