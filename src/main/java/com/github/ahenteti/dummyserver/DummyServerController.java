package com.github.ahenteti.dummyserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RestController
public class DummyServerController {

    @Autowired
    private IDummyServerRequestResponseStore store;

    @PostMapping("/dummy-response")
    public void addDummyResponse(@RequestBody DummyServerRequestResponsePair request) {
        this.store.add(request);
    }

    @PostMapping("/dummy-response-list")
    public void addDummyResponseList(@RequestBody List<DummyServerRequestResponsePair> requests) {
        requests.forEach(this::addDummyResponse);
    }

    @RequestMapping("/**")
    public ResponseEntity<?> getDummyResponse(HttpServletRequest request) {
        List<DummyServerRequestResponsePair> requestResponsePairList = store.find(request);
        if (requestResponsePairList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        requestResponsePairList.sort(Comparator.comparing(DummyServerRequestResponsePair::getPriority));
        return requestResponsePairList.get(0).getResponse().toResponseEntity();
    }
}
