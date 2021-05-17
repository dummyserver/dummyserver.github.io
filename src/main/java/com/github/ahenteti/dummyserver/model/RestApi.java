package com.github.ahenteti.dummyserver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestApi {

    private Integer priority = 5;
    private String name;
    private String description;
    private RestApiRequest request;
    private RestApiResponse response;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestApi)) return false;
        RestApi that = (RestApi) o;
        return Objects.equals(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request);
    }

}
