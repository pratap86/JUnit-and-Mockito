package com.pratap.mockito.dto;

import com.pratap.mockito.constant.OrderStatus;

public class Order {
	
	private int id;
	
	private OrderStatus status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
}
