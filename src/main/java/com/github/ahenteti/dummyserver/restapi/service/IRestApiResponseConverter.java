package com.github.ahenteti.dummyserver.restapi.service;

import com.github.ahenteti.dummyserver.restapi.model.RestApiResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface IRestApiResponseConverter {

    ResponseEntity<?> toResponseEntity(RestApiResponse response, HttpServletRequest request);

}
