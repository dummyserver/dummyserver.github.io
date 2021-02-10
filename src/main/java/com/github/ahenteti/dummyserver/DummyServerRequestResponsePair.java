package com.github.ahenteti.dummyserver;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Data
public class DummyServerRequestResponsePair {
    private DummyServerRequest request;
    private DummyServerResponse response;
    
    public boolean isDefiningDifferentMethodThan(HttpServletRequest request) {
        return this.request.isDefiningDifferentMethodThan(request);
    }

    public boolean isDefiningDifferentPathThan(HttpServletRequest request) {
        return this.request.isDefiningDifferentPathThan(request);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DummyServerRequestResponsePair)) return false;
        DummyServerRequestResponsePair that = (DummyServerRequestResponsePair) o;
        return request.equals(that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request);
    }
}
