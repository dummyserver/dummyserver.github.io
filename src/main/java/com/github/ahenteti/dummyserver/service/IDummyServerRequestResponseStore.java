package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface IDummyServerRequestResponseStore {
    void add(DummyServerRequestResponsePair requestResponse);
    
    boolean remove(DummyServerRequestResponsePair requestResponse);

    List<DummyServerRequestResponsePair> getByHttpRequest(HttpServletRequest request);

    Set<DummyServerRequestResponsePair> getAll();

    void clear();
}
