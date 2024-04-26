package com.immoben.admin.city;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "City not found")
public class CityNotFoundRestException extends Exception {

	private static final long serialVersionUID = 1L;

}
