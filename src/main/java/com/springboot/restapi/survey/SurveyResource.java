package com.springboot.restapi.survey;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class SurveyResource
{
	private SurveyService surveyService=new SurveyService();
	
    public SurveyResource(SurveyService surveySurvice) {
		super();
		this.surveyService = surveySurvice;
	}

	//surveys/survey1
    @RequestMapping(value="/surveys")
	public List<Survey> retrieveAllSurveys()
	{
		return surveyService.retrieveAllSurveys();
	}
    
    @RequestMapping(value="/surveys/{surveyId}")
	public Survey retrieveSurveyFromId(@PathVariable String surveyId)
	{
		Survey survey= surveyService.retrieveIdBasedSurvey(surveyId);
		//to prevent status 200 for invalid ID -->404
		if(survey==null)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return survey;
	}
    
    @RequestMapping(value="/surveys/{surveyId}/questions")
   	public List<Question> retrieveAllSurveyQuestionsFromId(@PathVariable String surveyId)
   	{
    	 List<Question> questions= surveyService.retrieveAllSurveyQuestions(surveyId);
   		//to prevent status 200 for invalid ID -->404
   		if(questions==null)
   		{
   			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
   		}
   		return questions;
   	}
    
    @RequestMapping(value="/surveys/{surveyId}/questions/{questionId}")
   	public Question retrieveSurveyQuestionsFromId(@PathVariable String surveyId , @PathVariable String questionId)
   	{
    	 Question question=surveyService.getQuestionByQId(surveyId,questionId);
   		//to prevent status 200 for invalid ID -->404
   		if(question==null)
   		{
   			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
   		}
   		return question;
   	}
    
    //POST Request
    @RequestMapping(value="/surveys/{surveyId}/questions",method=RequestMethod.POST)
   	public ResponseEntity<Object>  addNewSurveyQuestion(@PathVariable String surveyId,@RequestBody Question question)
   	{
    	 String questionId=surveyService.addNewSurveyQuestion(surveyId,question);
    	 URI location=ServletUriComponentsBuilder.fromCurrentRequest()
    			 .path("/{questionId}")
    			 .buildAndExpand(questionId).toUri();
		return ResponseEntity.created(location).build();
   	}
    
    //DELETE Request
    @RequestMapping(value="/surveys/{surveyId}/questions/{questionId}",method=RequestMethod.DELETE)
   	public ResponseEntity<Object> deleteSurveyQuestionsById(@PathVariable String surveyId , @PathVariable String questionId)
   	{
    	surveyService.deleteQuestionById(surveyId,questionId);
   		
   		return ResponseEntity.noContent().build();
   	}
    
    //UPDATE Request
    @RequestMapping(value="/surveys/{surveyId}/questions/{questionId}",method=RequestMethod.PUT)
   	public ResponseEntity<Object> updateSurveyQuestionsById(@PathVariable String surveyId ,
   			                                                @PathVariable String questionId,
   	                                                        @RequestBody Question question)
   	{
    	surveyService.updateQuestionById(surveyId,questionId,question);
   		
   		return ResponseEntity.noContent().build();
   	}
}
