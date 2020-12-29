package com.pratap.mockito.bo.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

import org.mockito.junit.jupiter.MockitoExtension;

import com.pratap.mockito.bo.exception.BOException;
import com.pratap.mockito.dao.OrderDAO;
import com.pratap.mockito.dto.Order;

@ExtendWith(MockitoExtension.class)
class OrderBOImplTest {
	
	private static final int ORDER_ID = 123;


	@Mock 
	private OrderDAO orderDAO;
	
	
	@InjectMocks
	private OrderBOImpl orderBOImpl;

	
	@Test
	void testPlaceOrder_Should_Create_An_Order() throws SQLException, BOException {

		Order order = new Order();
		when(orderDAO.create(any(Order.class))).thenReturn(Integer.valueOf(1));//any(Order.class) create a Dummy Order object on the Fly.
		boolean result = orderBOImpl.placeOrder(order);
		assertTrue(result);
		verify(orderDAO).create(order);
		
	}
	
	@Test
	void testPlaceOrder_Should_Not_Create_An_Order() throws SQLException, BOException {

		Order order = new Order();
		when(orderDAO.create(any(Order.class))).thenReturn(Integer.valueOf(0));
		boolean result = orderBOImpl.placeOrder(order);
		assertFalse(result);
		verify(orderDAO).create(order);// by default verify method check the only one time execution of particular method
		verify(orderDAO, times(1)).create(order);
		verify(orderDAO, atLeastOnce()).create(order);
		verify(orderDAO, atLeast(1)).create(order);
		
	}
	
	@Test
	void testPlaceOrder_Should_Through_BOException() throws SQLException {
		Order order = new Order();
		when(orderDAO.create(any(Order.class))).thenThrow(SQLException.class);
		assertThrows(BOException.class, () -> orderBOImpl.placeOrder(order));
	}
	
	@Test
	void testCancelOrder_Should_Cancel_The_Order() throws SQLException, BOException {
		
		Order order = new Order();
		
		when(orderDAO.read(anyInt())).thenReturn(order);
		when(orderDAO.update(order)).thenReturn(Integer.valueOf(1));
		
		boolean result = orderBOImpl.cancelOrder(ORDER_ID);
		assertTrue(result);
		
		verify(orderDAO).read(ORDER_ID);
		verify(orderDAO).update(order);
	}
	
	@Test
	void testCancelOrder_Should_Not_Cancel_The_Order() throws SQLException, BOException {
		
		Order order = new Order();
		
		when(orderDAO.read(anyInt())).thenReturn(order);
		when(orderDAO.update(order)).thenReturn(Integer.valueOf(0));
		
		boolean result = orderBOImpl.cancelOrder(ORDER_ID);
		assertFalse(result);
		
		verify(orderDAO).read(ORDER_ID);
		verify(orderDAO).update(order);
	}
	
	@Test
	void testCancelOrder_Read_Should_Throw_BOException() throws SQLException {
		
		when(orderDAO.read(anyInt())).thenThrow(SQLException.class);
		assertThrows(BOException.class, () -> orderBOImpl.cancelOrder(ORDER_ID));
	}
	
	@Test
	void testCancelOrder_Update_Should_Throw_BOException() throws SQLException {
		
		when(orderDAO.read(anyInt())).thenReturn(new Order());
		when(orderDAO.update(any(Order.class))).thenThrow(SQLException.class);
		assertThrows(BOException.class, () -> orderBOImpl.cancelOrder(ORDER_ID));
	}
	
	@Test
	void testDeleteOrder_Should_Delete_An_Order() throws SQLException, BOException {
		when(orderDAO.delete(anyInt())).thenReturn(Integer.valueOf(1));
		boolean result = orderBOImpl.deleteOrder(ORDER_ID);
		assertTrue(result);
		
		verify(orderDAO).delete(ORDER_ID);
	}
	
	@Test
	void testDeleteOrder_Should_Not_Delete_An_Order() throws SQLException, BOException {
		when(orderDAO.delete(anyInt())).thenReturn(Integer.valueOf(0));
		boolean result = orderBOImpl.deleteOrder(ORDER_ID);
		assertFalse(result);
		
		verify(orderDAO).delete(ORDER_ID);
	}
	
	@Test
	void testDeleteOrder_Should_Throw_BOException() throws SQLException, BOException {
		when(orderDAO.delete(anyInt())).thenThrow(SQLException.class);
		assertThrows(BOException.class, () -> orderBOImpl.deleteOrder(ORDER_ID));
	}

}
