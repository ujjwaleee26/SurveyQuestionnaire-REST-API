package com.springboot.restapi.user;



import java.util.List;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner 
{
    
    private Logger logger=LoggerFactory.getLogger(getClass());
    private UserDetailsRepository repository;
	public UserDetailsCommandLineRunner(UserDetailsRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public void run(String... args) throws Exception 
	{
		repository.save(new UserDetails("Ujjwal","Admin"));
		repository.save(new UserDetails("Shubham","Viewer"));
		repository.save(new UserDetails("Shubhu","Viewer"));
		
		List<UserDetails> users=repository.findByRole("Viewer");
		users.forEach(user -> logger.info(user.toString()));
	}
  
}
