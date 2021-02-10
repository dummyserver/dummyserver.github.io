package com.github.ahenteti.dummyserver;

import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Data
public class DummyServerResponse {

    private Integer status;
    private Map<String, String> headers = new HashMap<>();
    private String jsonBody;
    private String xmlBody;
    private String textBody;
    private Integer delay;
    
    public ResponseEntity<?> toResponseEntity() {
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(this.status);
        headers.forEach(responseBuilder::header);
        return responseBuilder.build();
    } 

}
