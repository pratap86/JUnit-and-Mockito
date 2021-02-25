package com.pratap.hotel.booking.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pratap.hotel.booking.dao.BookingDAO;
import com.pratap.hotel.booking.exceptions.BusinessException;
import com.pratap.hotel.booking.model.request.BookingRequest;
import com.pratap.hotel.booking.model.request.Room;
import com.pratap.hotel.booking.utils.services.CurrencyConverter;
import com.pratap.hotel.booking.utils.services.MailSender;
import com.pratap.hotel.booking.utils.services.PaymentService;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

	@Mock
	private PaymentService paymentServiceMock;

	@Mock
	private RoomService roomServiceMock;

	@Mock
	private BookingDAO bookingDAOMock;

	@Mock
	private MailSender mailSenderMock;

	@InjectMocks
	private BookingService bookingService;
	
	@Captor
	private ArgumentCaptor<Double> doubleCapture;

	@Test
	void testCalculatePrice_When_CorrectInput() {

		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
				2, false);

		double expected = 4 * 2 * 50.0;

		double actual = bookingService.calculatePrice(bookingRequest);

		assertEquals(expected, actual, "should be same");

	}

	@Test
	void testGetAvailablePlaceCount_When_OneRoomAvailable() {

		when(this.roomServiceMock.getAvailableRooms()).thenReturn(Collections.singletonList(new Room("Room-1", 2)));

		int expected = 2;

		int actual = bookingService.getAvailablePlaceCount();

		assertEquals(expected, actual, "should be matched");
	}
	
	@Test
	void testGetAvailablePlaceCount_When_MultipleRoomsAvailable() {

		List<Room> rooms = Arrays.asList(new Room("Room-1", 2), new Room("Room-2", 5));
		when(this.roomServiceMock.getAvailableRooms()).thenReturn(rooms);

		int expected = 2 + 5;

		int actual = bookingService.getAvailablePlaceCount();

		assertEquals(expected, actual, "should be matched");
	}
	
	@Test
	void testGetAvailablePlaceCount_When_CalledMultipleTimes() {

		List<Room> rooms = Arrays.asList(new Room("Room-1", 2), new Room("Room-2", 5));
		when(this.roomServiceMock.getAvailableRooms())
				.thenReturn(rooms)
				.thenReturn(Collections.singletonList(new Room("Room-2", 2)))
				.thenReturn(Collections.emptyList());

		int expectedFirstCall = 2 + 5;
		int expectedSecondCall = 2;
		int expectedThirdCall = 0;
				

		int actualFirst = bookingService.getAvailablePlaceCount();
		int actualSecond = bookingService.getAvailablePlaceCount();
		int actualThird = bookingService.getAvailablePlaceCount();

		assertAll(
			() -> assertEquals(expectedFirstCall, actualFirst, 7, "should be same"),
			() -> assertEquals(expectedSecondCall, actualSecond, 2, "should be same"),
			() -> assertEquals(expectedThirdCall, actualThird, 0, "should be same")
		);
	}
	
	@Test
	void testMakeBooking_ThrowBusinessException_When_NoRoomAvailable() {
		
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
				2, false);
			
		when(this.roomServiceMock.findAvailableRoomId(bookingRequest))
			.thenThrow(BusinessException.class);
		
		assertThrows(BusinessException.class, () -> bookingService.makeBooking(bookingRequest));
		
	}
	
	@Test
	void testMakeBooking_ThrowBusinessException_When_PriceTooHigh() {
		
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
				2, true);
			
		when(this.paymentServiceMock.pay(any(), eq(400.0)))
			.thenThrow(BusinessException.class);
		
		assertThrows(BusinessException.class, () -> bookingService.makeBooking(bookingRequest));
		
	}
	
	@Test
	void testMakeBooking_Should_InvokePayment_When_Prepaid() {
		
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
				2, true);
		bookingService.makeBooking(bookingRequest);
		
		verify(paymentServiceMock, times(1)).pay(bookingRequest, 400.0);
		verifyNoMoreInteractions(paymentServiceMock);
		
	}
	
	@Test
	void testMakeBooking_Should_NotInvokePayment_When_NotPrepaid() {
		
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
				2, false);
		bookingService.makeBooking(bookingRequest);
		
		verify(paymentServiceMock, never()).pay(any(), anyDouble());
		verifyNoMoreInteractions(paymentServiceMock);
		
	}
	
	@Test
	void testMakeBooking_Should_ThrowException_When_MailNotReady() {
		
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
				2, false);
		
		doThrow(BusinessException.class).when(mailSenderMock).sendBookingConfirmation(any());
		
		assertThrows(BusinessException.class, () -> bookingService.makeBooking(bookingRequest));
		
	}
	
	@Test
	void testMakeBooking_Should_PayCorrectPrice_When_InputOK() {
		
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
				2, true);
		
		bookingService.makeBooking(bookingRequest);
		
		verify(paymentServiceMock, times(1)).pay(eq(bookingRequest), doubleCapture.capture());
		assertEquals(400.0, doubleCapture.getValue());
		
	}
	
	@Test
	void testMakeBooking_Should_PayCorrectPrice_When_MultipleCalls() {
		
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
				2, true);
		
		BookingRequest bookingRequest2 = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 02),
				2, true);
		
		List<Double> expextedValues = Arrays.asList(400.0, 100.0);
		
		bookingService.makeBooking(bookingRequest);
		bookingService.makeBooking(bookingRequest2);
		
		verify(paymentServiceMock, times(2)).pay(any(), doubleCapture.capture());
		assertEquals(expextedValues, doubleCapture.getAllValues());
		
	}
	
	@Test
	void testCalculatePriceEuro() {
		
		try(MockedStatic<CurrencyConverter> mockedConverter = mockStatic(CurrencyConverter.class)){
			
			BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
					2, false);
			double expected = 400.0;
			
			mockedConverter.when(() -> CurrencyConverter.toEuro(anyDouble())).thenReturn(400.0);
			
			assertEquals(expected, bookingService.calculatePriceEuro(bookingRequest));
		}
	}
	
	@Test
	void testCalculatePriceEuroWitnAnswer() {
		
		try(MockedStatic<CurrencyConverter> mockedConverter = mockStatic(CurrencyConverter.class)){
			
			BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 01, 01), LocalDate.of(2021, 01, 05),
					2, false);
			double expected = 400.0 * 0.8;
			
			mockedConverter.when(() -> CurrencyConverter.toEuro(anyDouble()))
								.thenAnswer(invocation -> (double)invocation.getArgument(0) * 0.8);
			
			assertEquals(expected, bookingService.calculatePriceEuro(bookingRequest));
		}
	}

}
