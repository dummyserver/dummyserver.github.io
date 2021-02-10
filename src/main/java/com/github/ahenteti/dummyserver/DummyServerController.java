package com.github.ahenteti.dummyserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class DummyServerController {
    
    @Autowired
    private IDummyServerRequestResponseStore store;

    @PostMapping("/dummy-response")
    public void addDummyResponse(@RequestBody DummyServerRequestResponsePair request) {
        this.store.add(request);
    }

    @RequestMapping("/**")
    public ResponseEntity<?> getDummyResponse(HttpServletRequest request) {
        Optional<DummyServerResponse> response = store.find(request);
        if (response.isPresent()) {
            return response.get().toResponseEntity();
        }
        return ResponseEntity.notFound().build();
    }
}
