package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.ahenteti.dummyserver.exception.InternalServerErrorException;
import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DummyServerRequestResponsePairConverter implements IDummyServerRequestResponsePairConverter {

    private static final DummyServerRequestResponsePair[] EMPTY_ARRAY = {};

    @Autowired
    private JsonMapper jsonMapper;

    @Override
    public DummyServerRequestResponsePair[] toRequestResponsePairs(String requestBody, String rawRequestBodyFormat) {
        try {
            String requestBodyFormat = rawRequestBodyFormat.replaceAll("[-.]", "");
            if (StringUtils.equalsIgnoreCase("default", requestBodyFormat)) {
                return jsonMapper.readValue(requestBody, DummyServerRequestResponsePair[].class);
            }
            if (StringUtils.equalsIgnoreCase("openapiv3", requestBodyFormat)) {
                return EMPTY_ARRAY;
            }
            throw new InternalServerErrorException("unknown requestBodyFormat: " + rawRequestBodyFormat);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder("error while converting responseBody to an array of DummyServerRequestResponsePair. requestBody: ");
            message.append(requestBody);
            message.append(", requestBodyFormat: ");
            message.append(rawRequestBodyFormat);
            throw new InternalServerErrorException(message.toString(), e);
        }
    }
}
