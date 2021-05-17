package com.github.ahenteti.dummyserver.service;

public interface IDummyHttpRequestResponsePairConverterFactory {

    IDummyHttpRequestResponsePairConverter create(String rawRequestBodyFormat);

}
