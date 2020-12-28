package com.pratap.junit5.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pratap.junit5.service.GreetingService;

class GreetingServiceImplTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingServiceImplTest.class);
	private GreetingService greetingService;
	 
	@BeforeEach
	public void setup() {
		LOGGER.info("setup..");
		greetingService = new GreetingServiceImpl();
	}
	
	@Test
	public void testGreetShouldRetunAValidOutput() {
		LOGGER.info("testGreetShouldRetunAValidOutput");
		String greet = greetingService.greet("Test");
		assertNotNull(greet);
		assertThat(greet).isEqualTo("Hello! Test");
	}
	
	@Test
	public void testGreetShouldThrowAnException_For_NameIsNull() {
		LOGGER.info("testGreetShouldThrowAnException_For_NameIsNull");
		assertThrows(IllegalArgumentException.class, () -> greetingService.greet(null));
	}
	
	@Test
	public void testGreetShouldThrowAnException_For_NameIsBlank() {
		LOGGER.info("testGreetShouldThrowAnException_For_NameIsBlank");
		assertThrows(IllegalArgumentException.class, () -> greetingService.greet(""));
	}
	
	@AfterEach
	public void tearDown() {
		LOGGER.info("tearDown..");
		greetingService = null;
	}

}
