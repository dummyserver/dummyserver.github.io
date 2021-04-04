package com.github.ahenteti.dummyserver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DummyServerResponse {

    private Integer status;
    private Map<String, String> headers = new HashMap<>();
    private Object body;
    private Integer delayInMillis = 0;

}
