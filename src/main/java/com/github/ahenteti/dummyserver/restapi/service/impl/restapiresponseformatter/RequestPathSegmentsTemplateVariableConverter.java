package com.github.ahenteti.dummyserver.restapi.service.impl.restapiresponseformatter;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestPathSegmentsTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.path.segments";
    }

    @Override
    public boolean canConvert(TemplateVariable templateVariable) {
        return StringUtils.startsWithIgnoreCase(templateVariable.getName(), getTemplateVariableName());
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        Pattern pattern = Pattern.compile("request\\.path\\.segments\\[(\\d+)]");
        Matcher matcher = pattern.matcher(templateVariable.getName());
        if (!matcher.find()) {
            return templateVariable.toString();
        }
        Integer segmentNumber = Integer.parseInt(matcher.group(1));
        String[] pathSegments = templateVariable.getRequest().getRequestURI().split("/");
        try {
            return pathSegments[segmentNumber];
        } catch (Exception e) {
            return templateVariable.toString();
        }
    }

}
