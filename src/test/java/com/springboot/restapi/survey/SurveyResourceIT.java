package com.springboot.restapi.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)//launches entire spring boot application
public class SurveyResourceIT 
{
	//Authorization
	//
	@Autowired
	private TestRestTemplate template;
	
	private static String SPECIFIC_QUESTION_TEST="/surveys/Survey1/questions/Question1"; 
	private static String QUESTION_TEST="/surveys/Survey1/questions"; 

	
    @Test
    void retrieveSurveyQuestionsFromId_basic() throws JSONException
    {
    	HttpHeaders headers = createHttpContentTypeAndAuthorization();    	
    	HttpEntity<String> httpEntity=new HttpEntity<String>(null,headers);
    	ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_QUESTION_TEST, HttpMethod.GET, httpEntity, String.class);
    	String expectedResponse=""" 
    			{"id":"Question1","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
    			""";
    	assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    	String contentHeader = responseEntity.getHeaders().get("Content-Type").get(0);
		assertEquals("application/json", contentHeader);
    	JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
   	
    }
    
    @Test
    void retrieveAllSurveyQuestionsFromId_basic() throws JSONException
    {
    	HttpHeaders headers = createHttpContentTypeAndAuthorization();    	
    	HttpEntity<String> httpEntity=new HttpEntity<String>(null,headers);
    	ResponseEntity<String> responseEntity = template.exchange(QUESTION_TEST, HttpMethod.GET, httpEntity, String.class);
    	String expectedResponse=""" 
    			[
                 {
                   "id": "Question1"
                 },
                 {
                   "id": "Question2"
                 },
                 {
                   "id": "Question3"
                 }
                ]
    			""";
    	assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    	JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
   	
    }
    
    @Test
    void addNewSurveyQuestion_basic() throws JSONException
    {
    	String requestBody=
                """
        		{
                  "description": "Most Popular Language Today",
                  "options": [
                  "Java",
                  "TypeScript",
                  "Haskell",
                  "Python"
                             ],
                  "correctAnswer": "Java"
                }
                """;
    	HttpHeaders headers = createHttpContentTypeAndAuthorization();    	
    	HttpEntity<String> httpEntity=new HttpEntity<String>(requestBody,headers);
    	ResponseEntity<String> responseEntity = template.exchange(QUESTION_TEST, HttpMethod.POST, httpEntity, String.class);
    	assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    	String LocationHeader = responseEntity.getHeaders().get("Location").get(0);
		assertTrue(LocationHeader.contains("/surveys/Survey1/questions")) ;
		
		ResponseEntity<String> responseEntityDelete = template.exchange(QUESTION_TEST, HttpMethod.DELETE, httpEntity, String.class);
        assertTrue(responseEntityDelete.getStatusCode().is2xxSuccessful());
		//template.delete(LocationHeader);
    	
    }

	private HttpHeaders createHttpContentTypeAndAuthorization() {
		HttpHeaders headers=new HttpHeaders();
    	headers.add("Content-Type", "application/json");
    	headers.add("Authorization", "Basic "+ basicAuthEncoding("admin","password"));
		return headers;
	}
    
    String basicAuthEncoding(String name , String password)
    {
    	String combined=name+":"+password;
    	//Base64 Encoding-->bytes
    	byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
    	return new String(encodedBytes);
    }
}