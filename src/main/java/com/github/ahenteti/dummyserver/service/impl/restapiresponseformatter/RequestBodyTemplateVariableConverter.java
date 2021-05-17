package com.github.ahenteti.dummyserver.service.impl.restapiresponseformatter;

import com.github.ahenteti.dummyserver.service.impl.utils.XmlPathUtils;
import com.jayway.jsonpath.JsonPath;

import java.util.Optional;

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

        Optional<String> requestBody = templateVariable.getRequestBody();
        if (!requestBody.isPresent()) {
            return templateVariable.toString();
        }

        Optional<String> jsonPath = templateVariable.getOption("--json-path");
        if (jsonPath.isPresent()) {
            Object value = JsonPath.read(requestBody.get(), jsonPath.get());
            if (value != null) {
                return value.toString();
            }
        }

        Optional<String> xmlPath = templateVariable.getOption("--xml-path");
        if (xmlPath.isPresent()) {
            Optional<String> value = XmlPathUtils.read(requestBody.get(), xmlPath.get());
            if (value.isPresent()) {
                return value.get();
            }
        }

        return templateVariable.toString();
    }

}
