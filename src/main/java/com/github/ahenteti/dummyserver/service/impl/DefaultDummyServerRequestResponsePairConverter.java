package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.ahenteti.dummyserver.exception.InternalServerErrorException;
import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultDummyServerRequestResponsePairConverter implements IDummyServerRequestResponsePairConverter {

    @Autowired
    private JsonMapper jsonMapper;

    @Override
    public DummyServerRequestResponsePair[] toRequestResponsePairs(String requestBody) {
        try {
            return jsonMapper.readValue(requestBody, DummyServerRequestResponsePair[].class);
        } catch (Exception e) {
            throw new InternalServerErrorException("error while converting responseBody to an array of DummyServerRequestResponsePair. requestBody: " + requestBody, e);
        }
    }

}
