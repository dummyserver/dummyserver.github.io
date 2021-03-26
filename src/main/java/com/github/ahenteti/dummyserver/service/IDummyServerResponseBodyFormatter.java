package com.github.ahenteti.dummyserver.service;

import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.http.HttpServletRequest;

public interface IDummyServerResponseBodyFormatter {

    String format(String body, HttpServletRequest request);

    JsonNode format(JsonNode body, HttpServletRequest request);

}
