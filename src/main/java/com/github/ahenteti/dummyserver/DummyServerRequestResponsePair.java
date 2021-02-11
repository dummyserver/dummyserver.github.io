package com.github.ahenteti.dummyserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DummyServerRequestResponsePair {
    private DummyServerRequest request;
    private DummyServerResponse response;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DummyServerRequestResponsePair)) return false;
        DummyServerRequestResponsePair that = (DummyServerRequestResponsePair) o;
        return Objects.equals(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request);
    }
}
