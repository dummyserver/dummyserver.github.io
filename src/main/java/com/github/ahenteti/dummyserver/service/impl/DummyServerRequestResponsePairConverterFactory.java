package com.github.ahenteti.dummyserver.service.impl;

import com.github.ahenteti.dummyserver.exception.InternalServerErrorException;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairConverter;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairConverterFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DummyServerRequestResponsePairConverterFactory implements IDummyServerRequestResponsePairConverterFactory {

    @Autowired
    private DefaultDummyServerRequestResponsePairConverter defaultDummyServerRequestResponsePairConverter;

    @Autowired
    private OpenApiV3DummyServerRequestResponsePairConverter openApiV3DummyServerRequestResponsePairConverter;

    @Override
    public IDummyServerRequestResponsePairConverter create(String rawRequestBodyFormat) {
        String requestBodyFormat = rawRequestBodyFormat.replaceAll("[-.]", "");
        if (StringUtils.equalsIgnoreCase("default", requestBodyFormat)) {
            return defaultDummyServerRequestResponsePairConverter;
        }
        if (StringUtils.equalsIgnoreCase("openapiv3", requestBodyFormat)) {
            return openApiV3DummyServerRequestResponsePairConverter;
        }
        throw new InternalServerErrorException("unknown requestBodyFormat: " + rawRequestBodyFormat);
    }

}
