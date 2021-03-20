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

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DummyServerResponse {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerResponse.class);

    private Integer status;
    private Map<String, String> headers = new HashMap<>();
    private JsonNode body;
    private Integer delayInMillis = 0;

    public ResponseEntity<?> toResponseEntity() {
        sleepSilently();
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(this.status);
        headers.forEach(responseBuilder::header);
        if (body != null) {
            if (body instanceof TextNode) {
                return responseBuilder.body(body.asText());
            }
            return responseBuilder.body(body);
        }
        return responseBuilder.build();
    }

    private void sleepSilently() {
        if (delayInMillis <= 0) return;
        try {
            Thread.sleep(delayInMillis);
        } catch (Exception e) {
            LOGGER.error("error while delaying response. we log the error and return", e);
        }
    }

}
