package com.github.ahenteti.dummyserver.restapi.service.impl.restapiresponseformatter;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHeaderTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.header";
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
        Pattern pattern = Pattern.compile("request\\.header\\.([^.]+)");
        Matcher matcher = pattern.matcher(templateVariable.getName());
        if (!matcher.find()) {
            return templateVariable.toString();
        }
        String headerName = matcher.group(1);
        String headerValue = templateVariable.getRequest().getHeader(headerName);
        return StringUtils.isNotBlank(headerValue) ? headerValue : templateVariable.toString();
    }

}
