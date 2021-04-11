package com.github.ahenteti.dummyserver.restapi.service.impl.restapiconverter;

import com.github.ahenteti.dummyserver.common.InternalServerErrorException;
import com.github.ahenteti.dummyserver.restapi.service.IRestApiConverter;
import com.github.ahenteti.dummyserver.restapi.service.IRestApiConverterFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestApiConverterFactory implements IRestApiConverterFactory {

    @Autowired
    private DefaultRestApiConverter defaultRestApiConverter;

    @Autowired
    private OpenApiV3RestApiConverter openApiV3RestApiConverter;

    @Override
    public IRestApiConverter create(String rawRequestBodyFormat) {
        String requestBodyFormat = rawRequestBodyFormat.replaceAll("[-.]", "");
        if (StringUtils.equalsIgnoreCase("default", requestBodyFormat)) {
            return defaultRestApiConverter;
        }
        if (StringUtils.equalsIgnoreCase("openapiv3", requestBodyFormat)) {
            return openApiV3RestApiConverter;
        }
        throw new InternalServerErrorException("unknown requestBodyFormat: " + rawRequestBodyFormat);
    }

}
