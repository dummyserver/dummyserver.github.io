package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import com.jayway.jsonpath.JsonPath;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestBodyTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.body";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        Optional<String> jsonPath = getJsonPath(templateVariable);
        Optional<String> requestBody = templateVariable.getRequestBody();
        if (!jsonPath.isPresent() || !requestBody.isPresent()) {
            return templateVariable.toString();
        }
        Object value = JsonPath.read(requestBody.get(), jsonPath.get());
        return Objects.isNull(value) ? templateVariable.toString() : value.toString();
    }

    private Optional<String> getJsonPath(TemplateVariable templateVariable) {
        Pattern pattern = Pattern.compile("--json-path='(.+?)'");
        Matcher matcher = pattern.matcher(templateVariable.getOptions());
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }

}
