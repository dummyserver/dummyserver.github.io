package com.github.ahenteti.dummyserver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IDummyServerRequestResponseStore {
    void add(DummyServerRequestResponsePair requestResponse);
    
    boolean remove(DummyServerRequestResponsePair requestResponse);

    List<DummyServerRequestResponsePair> find(HttpServletRequest request);
}
