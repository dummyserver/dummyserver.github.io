package com.github.ahenteti.dummyserver.restapi.service;

import com.github.ahenteti.dummyserver.restapi.model.RestApi;

public interface IRestApiConverter {

    RestApi[] toRequestResponsePairs(String requestBody);

}
