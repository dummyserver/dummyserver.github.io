package com.github.ahenteti.dummyserver.service.impl;

import com.github.ahenteti.dummyserver.model.DummyHttpResponse;
import com.github.ahenteti.dummyserver.service.IDummyHttpResponseBodyFormatter;
import com.github.ahenteti.dummyserver.service.IDummyHttpResponseConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.json.JsonString;
import javax.servlet.http.HttpServletRequest;

@Service
public class DummyHttpResponseConverter implements IDummyHttpResponseConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyHttpResponseConverter.class);

    @Autowired
    private IDummyHttpResponseBodyFormatter bodyFormatter;

    @Override
    public ResponseEntity<?> toResponseEntity(DummyHttpResponse response, HttpServletRequest request) {
        sleepSilently(response);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatus());
        response.getHeaders().forEach(responseBuilder::header);
        if (response.getBody() == null) {
            return responseBuilder.build();
        }
        if (response.getBody() instanceof JsonString) {
            return responseBuilder.body(bodyFormatter.format(response.getBody().toString(), request));
        }
        return responseBuilder.body(bodyFormatter.format(response.getBody(), request));
    }

    private void sleepSilently(DummyHttpResponse response) {
        if (response.getDelayInMillis() <= 0) return;
        try {
            Thread.sleep(response.getDelayInMillis());
        } catch (Exception e) {
            LOGGER.error("error while delaying response. we log the error and return", e);
        }
    }

}
