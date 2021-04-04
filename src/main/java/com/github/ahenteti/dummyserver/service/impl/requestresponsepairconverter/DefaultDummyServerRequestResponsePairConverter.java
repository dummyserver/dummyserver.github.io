package com.github.ahenteti.dummyserver.service.impl.requestresponsepairconverter;

import com.github.ahenteti.dummyserver.exception.InternalServerErrorException;
import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairConverter;
import org.springframework.stereotype.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@Service
public class DefaultDummyServerRequestResponsePairConverter implements IDummyServerRequestResponsePairConverter {

    @Override
    public DummyServerRequestResponsePair[] toRequestResponsePairs(String requestBody) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.fromJson(requestBody, DummyServerRequestResponsePair[].class);
        } catch (Exception e) {
            throw new InternalServerErrorException("error while converting responseBody to an array of DummyServerRequestResponsePair. requestBody: " + requestBody, e);
        }
    }

}
