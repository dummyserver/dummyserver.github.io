package com.github.ahenteti.dummyserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class DummyServerResponseConverter implements IDummyServerResponseConverter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerResponseConverter.class);

    @Override
    public ResponseEntity<?> toResponseEntity(DummyServerResponse response) {
        sleepSilently(response);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatus());
        response.getHeaders().forEach(responseBuilder::header);
        if (response.getBody() == null) {
            return responseBuilder.build();
        }
        if (response.getBody() instanceof TextNode) {
            return responseBuilder.body(response.getBody().asText());
        }
        return responseBuilder.body(response.getBody());
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
