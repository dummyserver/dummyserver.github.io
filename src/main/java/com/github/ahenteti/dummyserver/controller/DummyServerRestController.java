package com.github.ahenteti.dummyserver.controller;

import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;
import com.github.ahenteti.dummyserver.model.DummyServerResponse;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairConverter;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairStore;
import com.github.ahenteti.dummyserver.service.IDummyServerResponseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@RestController
public class DummyServerRestController {

    public static final int FIRST_RESPONSE = 0;

    @Autowired
    private IDummyServerRequestResponsePairStore requestResponsePairStore;

    @Autowired
    private IDummyServerRequestResponsePairConverter requestResponsePairConverter;

    @Autowired
    private IDummyServerResponseConverter responseConverter;


    @PostMapping("/api/dummy-response")
    public void addDummyResponse(@RequestBody DummyServerRequestResponsePair request) {
        this.requestResponsePairStore.add(request);
    }

    @DeleteMapping("/api/dummy-response")
    public void removeDummyResponse(@RequestBody DummyServerRequestResponsePair request) {
        this.requestResponsePairStore.remove(request);
    }

    @GetMapping("/api/dummy-response-list")
    public Set<DummyServerRequestResponsePair> getDummyResponseList() {
        return this.requestResponsePairStore.getAll();
    }

    @PostMapping("/api/dummy-response-list")
    public void addDummyResponseList(@RequestBody String requestBody, @RequestParam(value = "format", required = false) String requestBodyFormatRequestParam, @RequestHeader(value = "X-Request-Body-Format", required = false) String requestBodyFormatRequestHeader) {
        String requestBodyFormat = getRequestBodyFormat(requestBodyFormatRequestParam, requestBodyFormatRequestHeader);
        DummyServerRequestResponsePair[] requestResponsePairs = requestResponsePairConverter
                .toRequestResponsePairs(requestBody, requestBodyFormat);
        requestResponsePairStore.add(requestResponsePairs);
    }

    private String getRequestBodyFormat(String requestBodyTypeRequestParam, String requestBodyTypeRequestHeader) {
        if (requestBodyTypeRequestParam != null) {
            return requestBodyTypeRequestParam;
        }
        if (requestBodyTypeRequestHeader != null) {
            return requestBodyTypeRequestHeader;
        }
        return "DEFAULT";
    }

    @DeleteMapping("/api/dummy-response-list")
    public void removeDummyResponseList() {
        this.requestResponsePairStore.clear();
    }

    @RequestMapping("/api/**")
    public ResponseEntity<?> getDummyResponse(HttpServletRequest request) {
        List<DummyServerRequestResponsePair> requestResponsePairList = requestResponsePairStore
                .getByHttpRequest(request);
        if (requestResponsePairList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        requestResponsePairList.sort(Comparator.comparing(DummyServerRequestResponsePair::getPriority));
        DummyServerResponse response = requestResponsePairList.get(FIRST_RESPONSE).getResponse();
        return responseConverter.toResponseEntity(response, request);
    }
}
