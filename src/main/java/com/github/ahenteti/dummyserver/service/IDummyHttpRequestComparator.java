package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.DummyHttpRequest;

import javax.servlet.http.HttpServletRequest;

public interface IDummyHttpRequestComparator {

    boolean equals(DummyHttpRequest dummyServerRequest, HttpServletRequest httpServletRequest);

}
