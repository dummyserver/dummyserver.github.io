package com.github.ahenteti.dummyserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class CustomJsonMapper extends JsonMapper {

    public CustomJsonMapper() {
        super();
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
