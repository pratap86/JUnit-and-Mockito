package com.pratap.mockito.bo.impl;

import com.pratap.mockito.bo.OrderBO;
import com.pratap.mockito.bo.exception.BOException;
import com.pratap.mockito.dto.Order;

public class OrderBOImpl implements OrderBO {

	@Override
	public boolean placeOrder(Order order) throws BOException {
		return false;
	}

	@Override
	public boolean cancelOrder(int id) throws BOException {
		return false;
	}

	@Override
	public boolean deleteOrder(int id) throws BOException {
		return false;
	}

}
