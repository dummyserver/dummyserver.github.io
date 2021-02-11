package com.github.ahenteti.dummyserver;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class InMemoryDummyServerRequestResponseStore implements IDummyServerRequestResponseStore {
    private Set<DummyServerRequestResponsePair> requestResponseList = new HashSet<>();

    @Override
    public void add(DummyServerRequestResponsePair requestResponse) {
        requestResponseList.remove(requestResponse);
        requestResponseList.add(requestResponse);
    }

    @Override
    public Optional<DummyServerResponse> find(HttpServletRequest servletRequest) {
        for (DummyServerRequestResponsePair requestResponsePair : requestResponseList) {
            if (requestResponsePair.getRequest().equals(servletRequest)) {
                return Optional.of(requestResponsePair.getResponse());
            }
        }
        return Optional.empty();
    }
}
