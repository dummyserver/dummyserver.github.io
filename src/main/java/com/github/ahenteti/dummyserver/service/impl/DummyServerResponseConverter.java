package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.databind.node.TextNode;
import com.github.ahenteti.dummyserver.model.DummyServerResponse;
import com.github.ahenteti.dummyserver.service.IDummyServerResponseBodyFormatter;
import com.github.ahenteti.dummyserver.service.IDummyServerResponseConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.json.JsonString;
import javax.servlet.http.HttpServletRequest;

@Service
public class DummyServerResponseConverter implements IDummyServerResponseConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerResponseConverter.class);

    @Autowired
    private IDummyServerResponseBodyFormatter bodyFormatter;

    @Override
    public ResponseEntity<?> toResponseEntity(DummyServerResponse response, HttpServletRequest request) {
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

    private void sleepSilently(DummyServerResponse response) {
        if (response.getDelayInMillis() <= 0) return;
        try {
            Thread.sleep(response.getDelayInMillis());
        } catch (Exception e) {
            LOGGER.error("error while delaying response. we log the error and return", e);
        }
    }

}
