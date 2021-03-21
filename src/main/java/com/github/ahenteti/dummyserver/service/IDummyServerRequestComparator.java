package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.DummyServerRequest;

import javax.servlet.http.HttpServletRequest;

public interface IDummyServerRequestComparator {

    boolean equals(DummyServerRequest dummyServerRequest, HttpServletRequest httpServletRequest);

}
