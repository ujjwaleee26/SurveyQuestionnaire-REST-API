package com.springboot.restapi.helloworld;

public class HelloWorldBean   //kind-of-like-structure(TodoStructure)
{
    public HelloWorldBean(String message) {
	super();
	this.message = message;
     }
  
     private String message;

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "HelloWorldBean [message=" + message + "]";
	}
     
     
}
