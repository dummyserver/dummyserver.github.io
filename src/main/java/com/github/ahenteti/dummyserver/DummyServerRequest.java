package com.github.ahenteti.dummyserver;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Data
public class DummyServerRequest {

    private String method;
    private String path;
    private Map<String, FilterOperator> queries;
    private Map<String, FilterOperator> headers;
    private Map<String, FilterOperator> cookies;
    private FilterOperator body;
    
    public boolean isDefiningDifferentPathThan(HttpServletRequest request) {
        return !StringUtils.equalsIgnoreCase(path, request.getRequestURI());
    }

    public boolean isDefiningDifferentMethodThan(HttpServletRequest request) {
        return !StringUtils.equalsIgnoreCase(method, request.getMethod());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DummyServerRequest)) return false;
        DummyServerRequest that = (DummyServerRequest) o;
        return method.equals(that.method) && path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }

    @Data
    public static class FilterOperator {
        private String equalsTo;
        private String equalsToXml;
        private String equalsToJson;
        private String matches;
        private String doesNotMatch;
        private String contains;
        private boolean absent;
    }

}
