package com.pratap.mockito.scrapbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ATest {

	@Mock
	private B b;
	
	@InjectMocks
	private A a;
	
	@Test
	void testUsesVoidMethod_Should_Call_Void_Method_Stub_Implicitly() throws Exception {
		assertEquals(1, a.usesVoidMethod());
		verify(b).voidMethod();
	
	}
	
	@Test
	void testUsesVoidMethod_Should_Call_Void_Method_Stub_Explicitly() throws Exception {
		doNothing().when(b).voidMethod();
		assertEquals(1, a.usesVoidMethod());
		verify(b).voidMethod();
	
	}
	
	@Test
	void testUsesVoidMethodWithRuntimeException() throws Exception {
		doThrow(Exception.class).when(b).voidMethod();
		assertThrows(RuntimeException.class, () -> a.usesVoidMethod());
	}
	
	@Test
	void testConsecutiveCall() throws Exception {
		doNothing().doThrow(Exception.class).when(b).voidMethod();
		assertEquals(1, a.usesVoidMethod());
		verify(b).voidMethod();
		assertThrows(RuntimeException.class, () -> a.usesVoidMethod());
	}

}
