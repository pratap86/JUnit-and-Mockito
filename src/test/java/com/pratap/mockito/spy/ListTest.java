package com.pratap.mockito.spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
	
	@Test
	void testVerificationBasics() {
		//SUT
		mockList.get(0);
		mockList.get(1);
		
		//verify
		verify(mockList).get(0);
		verify(mockList, times(1)).get(0);
		verify(mockList, times(2)).get(anyInt());
		verify(mockList, atLeast(1)).get(anyInt());
		verify(mockList, atLeastOnce()).get(anyInt());
		verify(mockList, atMost(2)).get(anyInt());
		verify(mockList, never()).get(2);
	}
	
	@Test
	void testArgumentCapturing() {
		//SUT
		mockList.add("test");
		
		//verifying
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(mockList).add(captor.capture());
		
		assertEquals("test", captor.getValue());
	}
	
	@Test
	void testMultipleArgumentCapturing() {
		//SUT
		mockList.add("test1");
		mockList.add("test2");
		
		//verifying
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(mockList, times(2)).add(captor.capture());
		
		List<String> allValues = captor.getAllValues();
		assertEquals("test1", allValues.get(0));
		assertEquals("test2", allValues.get(1));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testMocking() {
		List<String> mockList = mock(ArrayList.class);
		System.out.println("mockList.get(0) :"+mockList.get(0));//null
		System.out.println("mockList.size() :"+ mockList.size());//0
		mockList.add("test1");
		mockList.add("test2");
		System.out.println("mockList.size() : "+ mockList.size());
		when(mockList.size()).thenReturn(Integer.valueOf(5));
		System.out.println("mockList.size(), After overriding : "+ mockList.size());
	}
	
	@Test
	void testSpying() {
		List<String> mockList = spy(ArrayList.class);
//		System.out.println("mockList.get(0) :"+ mockList.get(0));//throw ArrayIndexOfBoundException
		System.out.println("mockList.size() :"+ mockList.size());//0
		mockList.add("test1");
		mockList.add("test2");
		System.out.println("mockList.size() : "+ mockList.size());
		when(mockList.size()).thenReturn(Integer.valueOf(5));
		System.out.println("mockList.size(), After overriding : "+ mockList.size());
		mockList.add("test3");
		System.out.println("added one more object: "+mockList.size());
	}

}
