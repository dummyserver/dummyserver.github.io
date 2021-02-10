package com.github.ahenteti.dummyserver;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface IDummyServerRequestResponseStore {
    void add(DummyServerRequestResponsePair requestResponse);
    Optional<DummyServerResponse> find(HttpServletRequest request);
}
