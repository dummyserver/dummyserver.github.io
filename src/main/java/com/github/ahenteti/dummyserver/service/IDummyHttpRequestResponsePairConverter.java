package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.DummyHttpRequestResponsePair;

public interface IDummyHttpRequestResponsePairConverter {

    DummyHttpRequestResponsePair[] toRequestResponsePairs(String requestBody);

}
