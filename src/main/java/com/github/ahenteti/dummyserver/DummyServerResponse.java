package com.github.ahenteti.dummyserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DummyServerResponse {

    private Integer status;
    private Map<String, String> headers = new HashMap<>();
    private JsonNode body;

    public ResponseEntity<?> toResponseEntity() {
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(this.status);
        headers.forEach(responseBuilder::header);
        if (body != null) {
            if (body instanceof TextNode) {
                return responseBuilder.body(((TextNode) body).asText());
            }
            return responseBuilder.body(body);
        }
        return responseBuilder.build();
    }

}
