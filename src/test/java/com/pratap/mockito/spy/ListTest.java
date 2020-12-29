package com.pratap.mockito.spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.anyInt;

@ExtendWith(MockitoExtension.class)
class ListTest {
	
	@Mock
	private List<String> mockList;
	

	@Test
	void testListSizeMethod() {
		when(mockList.size()).thenReturn(Integer.valueOf(2));
		assertEquals(2, mockList.size());
	
	}
	
	@Test
	void testListSizeMethod_Return_Multiple_Values() {
		when(mockList.size()).thenReturn(Integer.valueOf(2)).thenReturn(Integer.valueOf(3));
		assertEquals(2, mockList.size());
		assertEquals(3, mockList.size());
	
	}
	
	@Test
	void testListSizeMethodWithException() {
		when(mockList.size()).thenThrow(RuntimeException.class);
		assertThrows(RuntimeException.class, () -> mockList.size());
	
	}
	
	@Test
	void testListGetMethod_Return_Multiple_Values() {
		when(mockList.get(anyInt())).thenReturn("test").thenReturn("test123");
		assertEquals("test", mockList.get(0));
		assertEquals("test123", mockList.get(1));
	
	}

}
