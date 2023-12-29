package com.springboot.restapi.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration       //create a number of spring beans and return them back(have their config)
public class SpringSecurityConfiguration 
{
    //for login and password of users LDAP or database are needed
	//currently using inMemory
	//UserDetails is an interface, can't be directly instanced , needs a builder

//    @Bean
//    public InMemoryUserDetailsManager createUserDetailsManager()
//    {
//    	UserDetails user= User.withDefaultPasswordEncoder() //should never be used as all passwords should be encoded
//    	.username("Ujjwal")
//    	.password("dummy")
//    	.roles("USER","ADMIN")
//    	.build();
//    	
//    	return new InMemoryUserDetailsManager(user); 
//    }
//	
	@Bean
    public InMemoryUserDetailsManager createUserDetailsManager()
    {
		UserDetails user1 = createNewUser("admin", "password"); 
		UserDetails user2 = createNewUser("Shubham", "virat"); 
    	
    	return new InMemoryUserDetailsManager(user1,user2);
    }

	private UserDetails createNewUser(String username, String password) {
		Function<String, String> passwordEncoder=input -> passwordEncoder().encode(input);
		UserDetails user= User.builder()
    			              .passwordEncoder(passwordEncoder)
    			              .username(username)
    	                      .password(password)
    	                      .roles("USER","ADMIN")
    	                      .build();
		return user;
	}
    
    @Bean
    public PasswordEncoder passwordEncoder() 
    {
    	return new BCryptPasswordEncoder();
    }
    
    //all url's are protected
    //a login form is shown for unauthorized requests
    //Cross Server Request Forgery(CSRF) disable
    //Frames allowed
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
    	http.authorizeHttpRequests( auth -> auth.anyRequest().authenticated());
    	http.httpBasic(withDefaults());
    	http.csrf().disable(); //POST
    	http.headers().frameOptions().disable();
    	return http.build();
    }
}