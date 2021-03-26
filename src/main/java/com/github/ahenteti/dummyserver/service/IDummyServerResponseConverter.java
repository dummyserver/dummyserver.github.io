package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.DummyServerResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface IDummyServerResponseConverter {

    ResponseEntity<?> toResponseEntity(DummyServerResponse response, HttpServletRequest request);

}
