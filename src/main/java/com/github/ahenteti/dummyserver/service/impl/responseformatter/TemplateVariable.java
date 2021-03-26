package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.StringJoiner;

@Builder
@Getter
public class TemplateVariable {
    private String name;
    private String options;
    private HttpServletRequest request;

    @Override
    public String toString() {
        return "{{" + name + options + "}}";
    }
}
