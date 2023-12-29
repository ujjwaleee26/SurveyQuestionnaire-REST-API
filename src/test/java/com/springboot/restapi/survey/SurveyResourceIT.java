package com.springboot.restapi.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	
	@Autowired
	private TestRestTemplate template;
	
	private static String SPECIFIC_QUESTION_TEST="/surveys/Survey1/questions/Question1"; 
	private static String QUESTION_TEST="/surveys/Survey1/questions"; 

	
    @Test
    void retrieveSurveyQuestionsFromId_basic() throws JSONException
    {
    	ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_TEST, String.class);
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
    	ResponseEntity<String> responseEntity = template.getForEntity(QUESTION_TEST, String.class);
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
    	HttpHeaders headers=new HttpHeaders();
    	headers.add("Content-Type", "application/json");
    	HttpEntity<String> httpEntity=new HttpEntity<String>(requestBody,headers);
    	ResponseEntity<String> responseEntity = template.exchange(QUESTION_TEST, HttpMethod.POST, httpEntity, String.class);
    	assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    	String LocationHeader = responseEntity.getHeaders().get("Location").get(0);
		assertTrue(LocationHeader.contains("/surveys/Survey1/questions")) ;
        template.delete(LocationHeader);
    	
    }
}