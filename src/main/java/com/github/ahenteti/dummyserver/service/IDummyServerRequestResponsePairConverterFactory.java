package com.github.ahenteti.dummyserver.service;

public interface IDummyServerRequestResponsePairConverterFactory {

    IDummyServerRequestResponsePairConverter create(String rawRequestBodyFormat);

}
