package com.github.ahenteti.dummyserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DummyServerResponseConverter implements IDummyServerResponseConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerResponseConverter.class);

    @Autowired
    private IDummyServerResponseBodyFormatter bodyFormatter;

    @Override
    public ResponseEntity<?> toResponseEntity(DummyServerResponse response) {
        sleepSilently(response);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatus());
        response.getHeaders().forEach(responseBuilder::header);
        if (response.getBody() == null) {
            return responseBuilder.build();
        }
        if (response.getBody() instanceof TextNode) {
            return responseBuilder.body(bodyFormatter.format(response.getBody().asText()));
        }
        return responseBuilder.body(bodyFormatter.format(response.getBody()));
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
