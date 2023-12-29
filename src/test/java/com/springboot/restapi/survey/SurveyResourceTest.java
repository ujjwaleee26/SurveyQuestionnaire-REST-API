package com.springboot.restapi.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

//SurveyResource(launch for this specific resource only)
@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters=false)
class SurveyResourceTest 
{
    //Mock-->surveyService.getQuestionByQId(surveyId,questionId)(business layer used by web layer)\
	@MockBean
    private SurveyService surveyService;
	
    
    //Main entry point for server-side Spring MVC test support-Firing Request
    //FireRequest-->http://localhost:8080/surveys/Survey1/questions/Question1
    @Autowired
    private MockMvc mockMvc;
	
    private static String SQURL="http://localhost:8080/surveys/Survey1/questions/Question1";
	private static String GQURL="http://localhost:8080/surveys/Survey1/questions";

    
    //good test for 404 scenario
	@Test
	void retrieveSurveyQuestionsFromId_404unitTest() throws Exception
	{
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get(SQURL).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(404,mvcResult.getResponse().getStatus());
		
		//System.out.println(mvcResult.getResponse().getContentAsString());
		//System.out.println(mvcResult.getResponse().getStatus());//404-mock return null
	}
	
	//test for 200 scenario
	@Test
	void retrieveSurveyQuestionsFromId_200unitTest() throws Exception
	{
		Question question = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                        "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
		 String expectedResponse="""
				{
				"id":"Question1",
				"description":"Most Popular Cloud Platform Today",
				"options":[
				    "AWS",
				    "Azure",
				    "Google Cloud",
				    "Oracle Cloud"],
				"correctAnswer":"AWS"
				}
				""";
		
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get(SQURL).accept(MediaType.APPLICATION_JSON);
		when(surveyService.getQuestionByQId("Survey1","Question1")).thenReturn(question);
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(200,mvcResult.getResponse().getStatus());
		JSONAssert.assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString(),false);
		
		//System.out.println(mvcResult.getResponse().getContentAsString());
		//System.out.println(mvcResult.getResponse().getStatus());
	}
	
	//POST
	//addNewSurveyQuestion_201unitTest
	//surveyService.addNewSurveyQuestion(surveyId,question)
	//requestBody
	//201
	//location-header
	
	@Test
	void addNewSurveyQuestion_201unitTest() throws Exception
	{
		String requestBody="""
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
		
		//OngoingStubbing<String> thenReturn = when(surveyService.addNewSurveyQuestion(anyString(),any())).thenReturn("SOME-ID");
		when(surveyService.addNewSurveyQuestion(anyString(),any())).thenReturn("SOME-ID");
		RequestBuilder requestBuilder=
				MockMvcRequestBuilders.post(GQURL)
				                      .accept(MediaType.APPLICATION_JSON)
				                      .content(requestBody)
				                      .contentType(MediaType.APPLICATION_JSON);
		
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		String locationHeader = mvcResult.getResponse().getHeader("Location");
		assertEquals(201,mvcResult.getResponse().getStatus());
        assertTrue(locationHeader.contains("surveys/Survey1/questions/SOME-ID"));
		//http://localhost:8080/surveys/Survey1/questions/SOME-ID
		//System.out.println(locationHeader);
	}

}
