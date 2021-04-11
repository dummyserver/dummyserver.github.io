package com.github.ahenteti.dummyserver.restapi.service;

public interface IRestApiConverterFactory {

    IRestApiConverter create(String rawRequestBodyFormat);

}
