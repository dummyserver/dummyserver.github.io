package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.DummyHttpRequestResponsePair;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface IDummyHttpRequestResponsePairStore {

    void add(DummyHttpRequestResponsePair[] requestResponsePairs);

    void add(DummyHttpRequestResponsePair requestResponse);

    boolean remove(DummyHttpRequestResponsePair requestResponse);

    List<DummyHttpRequestResponsePair> getByHttpRequest(HttpServletRequest request);

    Set<DummyHttpRequestResponsePair> getAll();

    void clear();

}
