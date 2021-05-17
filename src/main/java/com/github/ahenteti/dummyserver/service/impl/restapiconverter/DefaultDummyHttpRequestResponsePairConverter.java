package com.github.ahenteti.dummyserver.service.impl.restapiconverter;

import com.github.ahenteti.dummyserver.exception.DummyServerException;
import com.github.ahenteti.dummyserver.model.DummyHttpRequestResponsePair;
import com.github.ahenteti.dummyserver.service.IDummyHttpRequestResponsePairConverter;
import org.springframework.stereotype.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@Service
public class DefaultDummyHttpRequestResponsePairConverter implements IDummyHttpRequestResponsePairConverter {

    @Override
    public DummyHttpRequestResponsePair[] toRequestResponsePairs(String requestBody) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.fromJson(requestBody, DummyHttpRequestResponsePair[].class);
        } catch (Exception e) {
            throw new DummyServerException("error while converting responseBody to an array of DummyServerRequestResponsePair. requestBody: " + requestBody, e);
        }
    }

}
