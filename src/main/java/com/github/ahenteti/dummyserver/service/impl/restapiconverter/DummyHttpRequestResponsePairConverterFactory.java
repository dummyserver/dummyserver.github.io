package com.github.ahenteti.dummyserver.service.impl.restapiconverter;

import com.github.ahenteti.dummyserver.exception.DummyServerException;
import com.github.ahenteti.dummyserver.service.IDummyHttpRequestResponsePairConverter;
import com.github.ahenteti.dummyserver.service.IDummyHttpRequestResponsePairConverterFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DummyHttpRequestResponsePairConverterFactory implements IDummyHttpRequestResponsePairConverterFactory {

    @Autowired
    private DefaultDummyHttpRequestResponsePairConverter defaultRestApiConverter;

    @Autowired
    private OpenApiV3DummyHttpRequestResponsePairConverter openApiV3RestApiConverter;

    @Override
    public IDummyHttpRequestResponsePairConverter create(String rawRequestBodyFormat) {
        String requestBodyFormat = rawRequestBodyFormat.replaceAll("[-.]", "");
        if (StringUtils.equalsIgnoreCase("default", requestBodyFormat)) {
            return defaultRestApiConverter;
        }
        if (StringUtils.equalsIgnoreCase("openapiv3", requestBodyFormat)) {
            return openApiV3RestApiConverter;
        }
        throw new DummyServerException("unknown requestBodyFormat: " + rawRequestBodyFormat);
    }

}
