package com.github.ahenteti.dummyserver;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InMemoryDummyServerRequestResponseStore implements IDummyServerRequestResponseStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryDummyServerRequestResponseStore.class);

    private Set<DummyServerRequestResponsePair> requestResponseList = new HashSet<>();

    @Value("${dummy.responses.file}")
    private String dummyResponsesFile;

    @Autowired
    private JsonMapper jsonMapper;

    @PostConstruct
    public void init() {
        try (Reader reader = Files.newBufferedReader(Paths.get(dummyResponsesFile))) {
            DummyServerRequestResponsePair[] dummyResponses = jsonMapper.readValue(reader, DummyServerRequestResponsePair[].class);
            Arrays.asList(dummyResponses).forEach(this::add);
        } catch (Exception e) {
            LOGGER.error("error while reading {}", dummyResponsesFile, e);
        }
    }

    @Override
    public void add(DummyServerRequestResponsePair requestResponse) {
        requestResponseList.remove(requestResponse);
        requestResponseList.add(requestResponse);
    }

    @Override
    public boolean remove(DummyServerRequestResponsePair requestResponse) {
        return requestResponseList.remove(requestResponse);
    }

    @Override
    public List<DummyServerRequestResponsePair> find(HttpServletRequest servletRequest) {
        List<DummyServerRequestResponsePair> res = new ArrayList<>();
        for (DummyServerRequestResponsePair requestResponsePair : requestResponseList) {
            if (requestResponsePair.getRequest().matches(servletRequest)) {
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
    public void clear() {
        requestResponseList.clear();
    }
}
