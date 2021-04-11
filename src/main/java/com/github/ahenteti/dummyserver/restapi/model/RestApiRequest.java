package com.github.ahenteti.dummyserver.restapi.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Data
public class RestApiRequest {

    private String method = "GET";
    private String path;
    private Map<String, Object> queries = new HashMap<>();
    private Map<String, Object> headers = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestApiRequest)) return false;
        RestApiRequest that = (RestApiRequest) o;
        return Objects.equals(this.toString(), that.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RestApiRequest.class.getSimpleName() + "[", "]")
                .add("method='" + method + "'").add("path='" + path + "'").add("queries=" + toString(queries))
                .add("headers=" + toString(headers)).toString();
    }

    private String toString(Map<String, Object> map) {
        return map.keySet().stream().sorted().map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
