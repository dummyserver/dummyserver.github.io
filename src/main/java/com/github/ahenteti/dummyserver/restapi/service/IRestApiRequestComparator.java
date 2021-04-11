package com.github.ahenteti.dummyserver.restapi.service;

import com.github.ahenteti.dummyserver.restapi.model.RestApiRequest;

import javax.servlet.http.HttpServletRequest;

public interface IRestApiRequestComparator {

    boolean equals(RestApiRequest dummyServerRequest, HttpServletRequest httpServletRequest);

}
