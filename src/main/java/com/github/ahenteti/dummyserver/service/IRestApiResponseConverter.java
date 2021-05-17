package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.RestApiResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface IRestApiResponseConverter {

    ResponseEntity<?> toResponseEntity(RestApiResponse response, HttpServletRequest request);

}
