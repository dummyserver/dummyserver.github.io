package com.github.ahenteti.dummyserver.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface IDummyServerResponseBodyFormatter {

    String format(String body);

    JsonNode format(JsonNode body);

}
