package com.pratap.junit5.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pratap.junit5.service.GreetingService;

@Component
public class GreetingImpl implements Greeting {

	@Autowired
	private GreetingService greetingService;
	
	@Override
	public String greet(String name) {
		return greetingService.greet(name);
	}

}
