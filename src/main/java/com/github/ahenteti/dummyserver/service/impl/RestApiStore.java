package com.github.ahenteti.dummyserver.service.impl;

import com.github.ahenteti.dummyserver.model.RestApi;
import com.github.ahenteti.dummyserver.service.IRestApiRequestComparator;
import com.github.ahenteti.dummyserver.service.IRestApiStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.nio.file.Files.newBufferedReader;

@Service
public class RestApiStore implements IRestApiStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiStore.class);

    private Set<RestApi> restApis = new HashSet<>();

    @Value("${restapi.responses.file}")
    private String restApiResponsesFile;

    @Autowired
    private IRestApiRequestComparator requestComparator;

    @PostConstruct
    public void init() {
        try (Reader reader = newBufferedReader(Paths.get(restApiResponsesFile)); Jsonb jsonb = JsonbBuilder.create()) {
            RestApi[] dummyResponses;
            dummyResponses = jsonb.fromJson(reader, RestApi[].class);
            Arrays.asList(dummyResponses).forEach(this::add);
        } catch (Exception e) {
            LOGGER.error("error while reading {}", restApiResponsesFile, e);
        }
    }

    @Override
    @DumpRestApiStore
    public void add(RestApi[] requestResponsePairs) {
        Arrays.stream(requestResponsePairs).forEach(this::add);
    }

    @Override
    @DumpRestApiStore
    public void add(RestApi requestResponse) {
        restApis.remove(requestResponse);
        restApis.add(requestResponse);
    }

    @Override
    @DumpRestApiStore
    public boolean remove(RestApi requestResponse) {
        return restApis.remove(requestResponse);
    }

    @Override
    public List<RestApi> getByHttpRequest(HttpServletRequest servletRequest) {
        List<RestApi> res = new ArrayList<>();
        for (RestApi requestResponsePair : restApis) {
            if (requestComparator.equals(requestResponsePair.getRequest(), servletRequest)) {
                res.add(requestResponsePair);
            }
        }
        return res;
    }

    @Override
    public Set<RestApi> getAll() {
        return restApis;
    }

    @Override
    @DumpRestApiStore
    public void clear() {
        restApis.clear();
    }
}
