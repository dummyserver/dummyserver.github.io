package com.github.ahenteti.dummyserver;

import com.fasterxml.jackson.databind.JsonNode;

public interface IDummyServerResponseBodyFormatter {

    String format(String body);

    JsonNode format(JsonNode body);

}
