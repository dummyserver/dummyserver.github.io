package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;

public interface IDummyServerRequestResponsePairConverter {

    DummyServerRequestResponsePair[] toRequestResponsePairs(String requestBody, String rawRequestBodyFormat);

}
