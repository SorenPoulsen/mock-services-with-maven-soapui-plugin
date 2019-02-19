package com.sorenpoulsen.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tempuri.TestPortType;
import org.tempuri.TestRequest;
import org.tempuri.TestResponse;
import org.tempuri.TestService;

/**
 * Test the JAX-WS service client by calling the SOAPUI mock service.
 */
public class ServiceTest {

	String mockEndpoint;

	@Before
	public void readMockPort() {
		Properties p = new Properties();
		try (InputStream is = ServiceTest.class.getResourceAsStream("/test.properties")) {
			p.load(is);
		} catch (IOException e) {
			Assert.fail();
		}
		String mockport = p.getProperty("mockport");
		Assert.assertNotNull(mockport);
		mockEndpoint = "http://localhost:" + mockport + "/testservice";
	}

	/**
	 * Sending the message "hello" must return a "triggered" response.
	 */
	@Test
	public void testTriggeredResponse() {
		TestService testService = new TestService();
		TestPortType testPort = testService.getTestPort();
		BindingProvider binding = (BindingProvider) testPort;
		binding.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, mockEndpoint);
		TestRequest testRequest = new TestRequest();
		testRequest.setMessage("hello");
		TestResponse sendMessage = testPort.sendMessage(testRequest);
		String response = sendMessage.getResponse();
		Assert.assertEquals("triggered", response);
	}

	/**
	 * Sending any other message must return the "Default" response
	 */
	@Test
	public void testDefaultResponse() {
		TestService testService = new TestService();
		TestPortType testPort = testService.getTestPort();
		BindingProvider binding = (BindingProvider) testPort;
		binding.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, mockEndpoint);
		TestRequest testRequest = new TestRequest();
		testRequest.setMessage("xyz");
		TestResponse sendMessage = testPort.sendMessage(testRequest);
		String response = sendMessage.getResponse();
		Assert.assertEquals("Default", response);
	}
}
