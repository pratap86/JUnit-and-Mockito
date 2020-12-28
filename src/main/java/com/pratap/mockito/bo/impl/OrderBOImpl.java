package com.pratap.mockito.bo.impl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pratap.mockito.bo.OrderBO;
import com.pratap.mockito.bo.exception.BOException;
import com.pratap.mockito.dao.OrderDAO;
import com.pratap.mockito.dto.Order;

@Service
public class OrderBOImpl implements OrderBO {

	@Autowired
	private OrderDAO orderDAO;
	
	@Override
	public boolean placeOrder(Order order) throws BOException {
		try {
			int id = orderDAO.create(order);
			if(id == 0) return false;
		} catch (SQLException e) {
			throw new BOException(e);
		}
		return true;
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
