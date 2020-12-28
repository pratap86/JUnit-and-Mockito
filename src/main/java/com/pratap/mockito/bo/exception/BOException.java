package com.pratap.mockito.bo.exception;

import java.sql.SQLException;

public class BOException extends Exception {

	private static final long serialVersionUID = -5057129015210329654L;

	public BOException(SQLException e) {
		super(e);
	}


}
