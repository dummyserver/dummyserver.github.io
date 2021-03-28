package com.github.ahenteti.dummyserver.service.impl;

import com.github.ahenteti.dummyserver.model.DummyServerRequest;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestComparator;
import com.github.ahenteti.dummyserver.service.impl.utils.ValueUtils;
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
            Object expectation = query.getValue();
            String[] queryValues = httpServletRequest.getParameterMap().get(queryName);
            if (queryValues != null) {
                for (String queryValue : queryValues) {
                    if (ValueUtils.isAsExpected(queryValue, expectation)) return true;
                }
            }
            return false;
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
