package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Service;

@Service
public class DummyServerJsonMapper extends JsonMapper {

    public DummyServerJsonMapper() {
        super();
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.enable(SerializationFeature.INDENT_OUTPUT);
    }
}
