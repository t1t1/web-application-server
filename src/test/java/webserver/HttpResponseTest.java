package webserver;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

public class HttpResponseTest {
	private String testDirectory = "./src/test/resources/";

	@Test
	public void test() {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
		HttpRequest request = new HttpRequest(in);
		assertEquals(HttpMethod.POST, request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("javajigi", request.getParameter("userId"));
	}

}
