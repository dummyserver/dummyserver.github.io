package com.github.ahenteti.dummyserver.service;

public interface IRestApiConverterFactory {

    IRestApiConverter create(String rawRequestBodyFormat);

}
