package com.pratap.junit5.bo;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pratap.junit5.service.GreetingService;

@ExtendWith(MockitoExtension.class)
class GreetingImplTest {

	@Mock
	private GreetingService greetingService;
	
	@InjectMocks
	private GreetingImpl greetingImpl;
	
	@Test
	public void testGreetShouldReturnAValidOutPut() {
		when(greetingService.greet(Mockito.anyString())).thenReturn("Hello JUnit5");
		String result = greetingImpl.greet("test");
		Assertions.assertNotNull(result, "result should not null");
		Assertions.assertEquals("Hello JUnit5", result);
	}
	
	@Test
	public void testGreetShouldThrowAnException_For_NameIsNull() {
		doThrow(IllegalArgumentException.class).when(greetingService).greet(null);
		Assertions.assertThrows(IllegalArgumentException.class, () -> greetingImpl.greet(null));
	}
	
	@Test
	public void testGreetShouldThrowAnException_For_NameIsBlank() {
		doThrow(IllegalArgumentException.class).when(greetingService).greet("");
		Assertions.assertThrows(IllegalArgumentException.class, () -> greetingImpl.greet(""));
	}
}
