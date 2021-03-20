package com.github.ahenteti.dummyserver;

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

    @Autowired
    private IDummyServerRequestResponseStore store;

    @GetMapping("/api/dummy-response-list")
    public Set<DummyServerRequestResponsePair> getDummyResponseList() {
        return this.store.getAll();
    }
    
    @PostMapping("/api/dummy-response")
    public void addDummyResponse(@RequestBody DummyServerRequestResponsePair request) {
        this.store.add(request);
    }

    @PostMapping("/api/dummy-response-list")
    public void addDummyResponseList(@RequestBody List<DummyServerRequestResponsePair> requests) {
        requests.forEach(this::addDummyResponse);
    }

    @DeleteMapping("/api/dummy-response")
    public void removeDummyResponse(@RequestBody DummyServerRequestResponsePair request) {
        this.store.remove(request);
    }

    @RequestMapping("/api/**")
    public ResponseEntity<?> getDummyResponse(HttpServletRequest request) {
        List<DummyServerRequestResponsePair> requestResponsePairList = store.find(request);
        if (requestResponsePairList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        requestResponsePairList.sort(Comparator.comparing(DummyServerRequestResponsePair::getPriority));
        return requestResponsePairList.get(0).getResponse().toResponseEntity();
    }
}
