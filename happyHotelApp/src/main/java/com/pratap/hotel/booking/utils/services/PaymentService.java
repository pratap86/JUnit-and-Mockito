package com.pratap.hotel.booking.utils.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.pratap.hotel.booking.model.request.BookingRequest;

public class PaymentService {

	private final Map<String, Double> payments = new HashMap<>();

	public String pay(BookingRequest bookingRequest, double price) {
		if (price > 200.0 && bookingRequest.getGuestCount() < 3) {
			throw new UnsupportedOperationException("Only small payments are supported.");
		}
		String id = UUID.randomUUID().toString();
		payments.put(id, price);
		return id;
	}
}
