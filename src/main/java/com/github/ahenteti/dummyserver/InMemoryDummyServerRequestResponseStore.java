package com.github.ahenteti.dummyserver;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    public List<DummyServerRequestResponsePair> find(HttpServletRequest servletRequest) {
        List<DummyServerRequestResponsePair> res = new ArrayList<>();
        for (DummyServerRequestResponsePair requestResponsePair : requestResponseList) {
            if (requestResponsePair.getRequest().matches(servletRequest)) {
                res.add(requestResponsePair);
            }
        }
        return res;
    }
}
