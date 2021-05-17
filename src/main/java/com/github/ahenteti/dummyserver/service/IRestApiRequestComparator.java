package com.github.ahenteti.dummyserver.service;

import com.github.ahenteti.dummyserver.model.RestApiRequest;

import javax.servlet.http.HttpServletRequest;

public interface IRestApiRequestComparator {

    boolean equals(RestApiRequest dummyServerRequest, HttpServletRequest httpServletRequest);

}
