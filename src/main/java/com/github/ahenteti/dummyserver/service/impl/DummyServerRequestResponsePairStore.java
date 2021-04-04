package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestComparator;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairStore;
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
public class DummyServerRequestResponsePairStore implements IDummyServerRequestResponsePairStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerRequestResponsePairStore.class);

    private Set<DummyServerRequestResponsePair> requestResponseList = new HashSet<>();

    @Value("${dummy.responses.file}")
    private String dummyResponsesFile;

    @Autowired
    private IDummyServerRequestComparator requestComparator;

    @PostConstruct
    public void init() {
        try (Reader reader = newBufferedReader(Paths.get(dummyResponsesFile)); Jsonb jsonb = JsonbBuilder.create()) {
            DummyServerRequestResponsePair[] dummyResponses;
            dummyResponses = jsonb.fromJson(reader, DummyServerRequestResponsePair[].class);
            Arrays.asList(dummyResponses).forEach(this::add);
        } catch (Exception e) {
            LOGGER.error("error while reading {}", dummyResponsesFile, e);
        }
    }

    @Override
    @DumpStore
    public void add(DummyServerRequestResponsePair[] requestResponsePairs) {
        Arrays.stream(requestResponsePairs).forEach(this::add);
    }

    @Override
    @DumpStore
    public void add(DummyServerRequestResponsePair requestResponse) {
        requestResponseList.remove(requestResponse);
        requestResponseList.add(requestResponse);
    }

    @Override
    @DumpStore
    public boolean remove(DummyServerRequestResponsePair requestResponse) {
        return requestResponseList.remove(requestResponse);
    }

    @Override
    public List<DummyServerRequestResponsePair> getByHttpRequest(HttpServletRequest servletRequest) {
        List<DummyServerRequestResponsePair> res = new ArrayList<>();
        for (DummyServerRequestResponsePair requestResponsePair : requestResponseList) {
            if (requestComparator.equals(requestResponsePair.getRequest(), servletRequest)) {
                res.add(requestResponsePair);
            }
        }
        return res;
    }

    @Override
    public Set<DummyServerRequestResponsePair> getAll() {
        return requestResponseList;
    }

    @Override
    @DumpStore
    public void clear() {
        requestResponseList.clear();
    }
}
