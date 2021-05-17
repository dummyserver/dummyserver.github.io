package com.github.ahenteti.dummyserver.service.impl;

import com.github.ahenteti.dummyserver.model.DummyHttpRequestResponsePair;
import com.github.ahenteti.dummyserver.service.IDummyHttpRequestComparator;
import com.github.ahenteti.dummyserver.service.IDummyHttpRequestResponsePairStore;
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
public class DummyHttpRequestResponsePairStore implements IDummyHttpRequestResponsePairStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyHttpRequestResponsePairStore.class);

    private Set<DummyHttpRequestResponsePair> restApis = new HashSet<>();

    @Value("${dummy.http.request.response.pairs.file}")
    private String dummyHttpRequestResponsePairsFile;

    @Autowired
    private IDummyHttpRequestComparator requestComparator;

    @PostConstruct
    public void init() {
        try (Reader reader = newBufferedReader(Paths.get(dummyHttpRequestResponsePairsFile)); Jsonb jsonb = JsonbBuilder.create()) {
            DummyHttpRequestResponsePair[] dummyResponses;
            dummyResponses = jsonb.fromJson(reader, DummyHttpRequestResponsePair[].class);
            Arrays.asList(dummyResponses).forEach(this::add);
        } catch (Exception e) {
            LOGGER.error("error while reading {}", dummyHttpRequestResponsePairsFile, e);
        }
    }

    @Dump
    @Override
    public void add(DummyHttpRequestResponsePair[] requestResponsePairs) {
        Arrays.stream(requestResponsePairs).forEach(this::add);
    }

    @Dump
    @Override
    public void add(DummyHttpRequestResponsePair requestResponse) {
        restApis.remove(requestResponse);
        restApis.add(requestResponse);
    }

    @Dump
    @Override
    public boolean remove(DummyHttpRequestResponsePair requestResponse) {
        return restApis.remove(requestResponse);
    }

    @Override
    public List<DummyHttpRequestResponsePair> getByHttpRequest(HttpServletRequest servletRequest) {
        List<DummyHttpRequestResponsePair> res = new ArrayList<>();
        for (DummyHttpRequestResponsePair requestResponsePair : restApis) {
            if (requestComparator.equals(requestResponsePair.getRequest(), servletRequest)) {
                res.add(requestResponsePair);
            }
        }
        return res;
    }

    @Override
    public Set<DummyHttpRequestResponsePair> getAll() {
        return restApis;
    }

    @Dump
    @Override
    public void clear() {
        restApis.clear();
    }
}
