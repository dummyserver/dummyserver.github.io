package com.github.ahenteti.dummyserver.service.impl.restapiconverter;

import com.github.ahenteti.dummyserver.exception.InternalServerErrorException;
import com.github.ahenteti.dummyserver.model.RestApi;
import com.github.ahenteti.dummyserver.service.IRestApiConverter;
import org.springframework.stereotype.Service;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@Service
public class DefaultRestApiConverter implements IRestApiConverter {

    @Override
    public RestApi[] toRequestResponsePairs(String requestBody) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.fromJson(requestBody, RestApi[].class);
        } catch (Exception e) {
            throw new InternalServerErrorException("error while converting responseBody to an array of DummyServerRequestResponsePair. requestBody: " + requestBody, e);
        }
    }

}
