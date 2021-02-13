package com.github.ahenteti.dummyserver;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Data
public class DummyServerRequest {

    private String method = "GET";
    private String path;
    private Map<String, Object> queries = new HashMap<>();
    private Map<String, Object> headers = new HashMap<>();

    public boolean matches(HttpServletRequest request) {
        if (!request.getRequestURI().matches(path)) return false;
        if (!StringUtils.equalsIgnoreCase(method, request.getMethod())) return false;
        for (Map.Entry<String, Object> query : queries.entrySet()) {
            String queryName = query.getKey();
            String queryValue = request.getParameter(queryName);
            Object expectation = query.getValue();
            if (Expectation.valueNotAsExpected(queryValue, expectation)) return false;
        }
        for (Map.Entry<String, Object> header : headers.entrySet()) {
            String headerName = header.getKey();
            String headerValue = request.getHeader(headerName);
            Object expectation = header.getValue();
            if (Expectation.valueNotAsExpected(headerValue, expectation)) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DummyServerRequest)) return false;
        DummyServerRequest that = (DummyServerRequest) o;
        return Objects.equals(this.toString(), that.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DummyServerRequest.class.getSimpleName() + "[", "]")
                .add("method='" + method + "'")
                .add("path='" + path + "'")
                .add("queries=" + toString(queries))
                .add("headers=" + toString(headers))
                .toString();
    }
    
    private String toString(Map<String, Object> map) {
        return map.keySet()
                .stream()
                .sorted()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
