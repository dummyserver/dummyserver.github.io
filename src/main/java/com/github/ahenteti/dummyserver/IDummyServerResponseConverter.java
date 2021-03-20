package com.github.ahenteti.dummyserver;

import org.springframework.http.ResponseEntity;

public interface IDummyServerResponseConverter {

    ResponseEntity<?> toResponseEntity(DummyServerResponse response);

}
