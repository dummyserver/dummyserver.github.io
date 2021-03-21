package com.github.ahenteti.dummyserver.service.impl;

import com.github.ahenteti.dummyserver.model.DummyServerRequest;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestComparator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class DummyServerRequestComparator implements IDummyServerRequestComparator {

    @Override
    public boolean equals(DummyServerRequest dummyServerRequest, HttpServletRequest httpServletRequest) {
        if (!httpServletRequest.getRequestURI().matches(dummyServerRequest.getPath())) return false;
        if (!StringUtils.equalsIgnoreCase(dummyServerRequest.getMethod(), httpServletRequest.getMethod())) return false;
        for (Map.Entry<String, Object> query : dummyServerRequest.getQueries().entrySet()) {
            String queryName = query.getKey();
            String queryValue = httpServletRequest.getParameter(queryName);
            Object expectation = query.getValue();
            if (ValueUtils.isNotAsExpected(queryValue, expectation)) return false;
        }
        for (Map.Entry<String, Object> header : dummyServerRequest.getHeaders().entrySet()) {
            String headerName = header.getKey();
            String headerValue = httpServletRequest.getHeader(headerName);
            Object expectation = header.getValue();
            if (ValueUtils.isNotAsExpected(headerValue, expectation)) return false;
        }
        return true;
    }

}
