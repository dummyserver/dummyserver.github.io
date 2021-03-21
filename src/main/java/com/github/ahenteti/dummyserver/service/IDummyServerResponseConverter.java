package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.DummyServerResponse;
import org.springframework.http.ResponseEntity;

public interface IDummyServerResponseConverter {

    ResponseEntity<?> toResponseEntity(DummyServerResponse response);

}
