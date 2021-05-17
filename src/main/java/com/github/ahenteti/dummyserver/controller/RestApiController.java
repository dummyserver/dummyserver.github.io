package com.github.ahenteti.dummyserver.controller;

import com.github.ahenteti.dummyserver.model.DummyHttpRequestResponsePair;
import com.github.ahenteti.dummyserver.model.DummyHttpResponse;
import com.github.ahenteti.dummyserver.service.IDummyHttpRequestResponsePairConverter;
import com.github.ahenteti.dummyserver.service.IDummyHttpRequestResponsePairConverterFactory;
import com.github.ahenteti.dummyserver.service.IDummyHttpResponseConverter;
import com.github.ahenteti.dummyserver.service.IDummyHttpRequestResponsePairStore;
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
public class RestApiController {

    public static final int FIRST_RESPONSE = 0;

    @Autowired
    private IDummyHttpRequestResponsePairStore requestResponsePairStore;

    @Autowired
    private IDummyHttpRequestResponsePairConverterFactory requestResponsePairConverterFactory;

    @Autowired
    private IDummyHttpResponseConverter responseConverter;


    @PostMapping("/__admin/dummy-http-request-response-pair")
    public void addDummyHttpRequestResponsePair(@RequestBody DummyHttpRequestResponsePair request) {
        this.requestResponsePairStore.add(request);
    }

    @DeleteMapping("/__admin/dummy-http-request-response-pair")
    public void removeDummyHttpRequestResponsePair(@RequestBody DummyHttpRequestResponsePair request) {
        this.requestResponsePairStore.remove(request);
    }

    @GetMapping("/__admin/dummy-http-request-response-pairs")
    public Set<DummyHttpRequestResponsePair> getDummyHttpRequestResponsePairs() {
        return this.requestResponsePairStore.getAll();
    }

    @PostMapping("/__admin/dummy-http-request-response-pairs")
    public void addDummyHttpRequestResponsePairs(@RequestBody String requestBody, @RequestParam(value = "format", required = false) String requestBodyFormatRequestParam, @RequestHeader(value = "X-Request-Body-Format", required = false) String requestBodyFormatRequestHeader) {
        String requestBodyFormat = getRequestBodyFormat(requestBodyFormatRequestParam, requestBodyFormatRequestHeader);
        IDummyHttpRequestResponsePairConverter requestResponsePairConverter = requestResponsePairConverterFactory
                .create(requestBodyFormat);
        DummyHttpRequestResponsePair[] requestResponsePairs = requestResponsePairConverter
                .toRequestResponsePairs(requestBody);
        requestResponsePairStore.add(requestResponsePairs);
    }

    @DeleteMapping("/__admin/dummy-http-request-response-pairs")
    public void removeDummyHttpRequestResponsePairs() {
        this.requestResponsePairStore.clear();
    }

    @RequestMapping("/api/**")
    public ResponseEntity<?> getDummyResponse(HttpServletRequest request) {
        List<DummyHttpRequestResponsePair> requestResponsePairList = requestResponsePairStore
                .getByHttpRequest(request);
        if (requestResponsePairList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        requestResponsePairList.sort(Comparator.comparing(DummyHttpRequestResponsePair::getPriority));
        DummyHttpResponse response = requestResponsePairList.get(FIRST_RESPONSE).getResponse();
        return responseConverter.toResponseEntity(response, request);
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
}
