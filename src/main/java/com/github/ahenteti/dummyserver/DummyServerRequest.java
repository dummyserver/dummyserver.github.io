package com.github.ahenteti.dummyserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DummyServerRequest {

    @JsonIgnore
    private JsonMapper jsonMapper = new CustomJsonMapper();

    private String method = "GET";
    private String path;
    private Map<String, ValueExpectation> queries = new HashMap<>();
    private Map<String, ValueExpectation> headers = new HashMap<>();
    private ValueExpectation body = new NoValueExpectation();

    public boolean equals(HttpServletRequest request) {
        if (!StringUtils.equalsIgnoreCase(path, request.getRequestURI())) return false;
        if (!StringUtils.equalsIgnoreCase(method, request.getMethod())) return false;
        for (Map.Entry<String, ValueExpectation> query : queries.entrySet()) {
            String queryName = query.getKey();
            String queryValue = request.getParameter(queryName);
            ValueExpectation valueExpectation = query.getValue();
            if (valueExpectation.isIncorrect(queryValue)) return false;
        }
        for (Map.Entry<String, ValueExpectation> header : headers.entrySet()) {
            String headerName = header.getKey();
            String headerValue = request.getHeader(headerName);
            ValueExpectation valueExpectation = header.getValue();
            if (valueExpectation.isIncorrect(headerValue)) return false;
        }
        try {
            String requestBody = IOUtils.toString(request.getReader());
            if (body.isIncorrect(requestBody)) return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DummyServerRequest)) return false;
        DummyServerRequest that = (DummyServerRequest) o;
        try {
            return Objects.equals(jsonMapper.readTree(this.toJson()), jsonMapper.readTree(that.toJson()));
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return toJson().hashCode();
    }

    public String toJson() {
        try {
            return jsonMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

}
