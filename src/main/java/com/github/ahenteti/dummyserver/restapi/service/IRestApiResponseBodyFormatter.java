package com.github.ahenteti.dummyserver.restapi.service;

import javax.servlet.http.HttpServletRequest;

public interface IRestApiResponseBodyFormatter {

    String format(String body, HttpServletRequest request);

    Object format(Object body, HttpServletRequest request);

}
