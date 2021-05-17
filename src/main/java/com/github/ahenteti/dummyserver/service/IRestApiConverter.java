package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.RestApi;

public interface IRestApiConverter {

    RestApi[] toRequestResponsePairs(String requestBody);

}
