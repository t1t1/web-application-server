package webserver;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class HttpRequestTest{
	private String testDirectory = "./src/test/resources/";
	
	private InputStream in;
	
	private HttpRequest request;
	
	@Before
	public void before() throws Exception {
		in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
		request = new HttpRequest(in);
	}
	
	@Test
	public void getMethodTest() throws Exception {
//		assertEquals(HttpMethod.GET, request.getMethod());
		assertEquals("GET", request.getMethod());
	}
	
	@Test
	public void getPathTest() throws Exception {
		assertEquals("/user/create", request.getPath());
	}
	
	@Test
	public void getHeaderTest() throws Exception {
		assertEquals("keep-alive", request.getHeader("Connection"));
	}
	
	@Test
	public void getParameterTest() throws Exception {
		assertEquals("javajigi", request.getParameter("userId"));
	}

}
