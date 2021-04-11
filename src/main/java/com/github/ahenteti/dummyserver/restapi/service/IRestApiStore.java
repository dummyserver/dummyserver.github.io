package com.github.ahenteti.dummyserver.restapi.service;

import com.github.ahenteti.dummyserver.restapi.model.RestApi;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface IRestApiStore {

    void add(RestApi[] requestResponsePairs);

    void add(RestApi requestResponse);

    boolean remove(RestApi requestResponse);

    List<RestApi> getByHttpRequest(HttpServletRequest request);

    Set<RestApi> getAll();

    void clear();

}
