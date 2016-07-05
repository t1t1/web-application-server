package webserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;


public class HttpRequestTest{
	private String testDirectory = "./src/test/resources/";

	@Test
	public void testGetMethod() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPath() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetHeader() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParameter() {
		fail("Not yet implemented");
	}
	
	@Test
	public void requestGetTest() {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
		HttpRequest request = new HttpRequest(in);
//		assertEquals(HttpMethod.GET, request.getMethod());
		assertEquals("GET", request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("javajigi", request.getParameter("userId"));
	}

}
