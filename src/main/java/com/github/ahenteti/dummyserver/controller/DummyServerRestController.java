package com.github.ahenteti.dummyserver.controller;

import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;
import com.github.ahenteti.dummyserver.model.DummyServerResponse;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponseStore;
import com.github.ahenteti.dummyserver.service.IDummyServerResponseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@RestController
public class DummyServerRestController {

    public static final int FIRST_RESPONSE = 0;

    @Autowired
    private IDummyServerRequestResponseStore store;

    @Autowired
    private IDummyServerResponseConverter converter;

    @PostMapping("/api/dummy-response")
    public void addDummyResponse(@RequestBody DummyServerRequestResponsePair request) {
        this.store.add(request);
    }

    @DeleteMapping("/api/dummy-response")
    public void removeDummyResponse(@RequestBody DummyServerRequestResponsePair request) {
        this.store.remove(request);
    }

    @GetMapping("/api/dummy-response-list")
    public Set<DummyServerRequestResponsePair> getDummyResponseList() {
        return this.store.getAll();
    }

    @PostMapping("/api/dummy-response-list")
    public void addDummyResponseList(@RequestBody List<DummyServerRequestResponsePair> requests) {
        requests.forEach(this::addDummyResponse);
    }

    @DeleteMapping("/api/dummy-response-list")
    public void removeDummyResponseList() {
        this.store.clear();
    }

    @RequestMapping("/api/**")
    public ResponseEntity<?> getDummyResponse(HttpServletRequest request) {
        List<DummyServerRequestResponsePair> requestResponsePairList = store.getByHttpRequest(request);
        if (requestResponsePairList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        requestResponsePairList.sort(Comparator.comparing(DummyServerRequestResponsePair::getPriority));
        DummyServerResponse response = requestResponsePairList.get(FIRST_RESPONSE).getResponse();
        return converter.toResponseEntity(response, request);
    }
}
