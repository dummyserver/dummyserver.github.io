package com.github.ahenteti.dummyserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DummyServerResponse {

    private Integer status;
    private Map<String, String> headers = new HashMap<>();
    private JsonNode body;
    private Integer delayInMillis = 0;

}
