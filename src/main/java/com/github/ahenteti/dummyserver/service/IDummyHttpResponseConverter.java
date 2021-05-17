package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.DummyHttpResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface IDummyHttpResponseConverter {

    ResponseEntity<?> toResponseEntity(DummyHttpResponse response, HttpServletRequest request);

}
