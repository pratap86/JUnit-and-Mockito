package com.pratap.mockito.bo;

import com.pratap.mockito.bo.exception.BOException;
import com.pratap.mockito.dto.Order;

public interface OrderBO {

	boolean placeOrder(Order order) throws BOException;
	boolean cancelOrder(int id) throws BOException;
	boolean deleteOrder(int id) throws BOException;
}
