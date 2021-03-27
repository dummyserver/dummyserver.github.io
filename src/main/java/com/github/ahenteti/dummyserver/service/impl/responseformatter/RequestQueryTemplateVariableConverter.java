package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestQueryTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.query";
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
        Pattern pattern = Pattern.compile("request\\.query\\.([^.]+)");
        Matcher matcher = pattern.matcher(templateVariable.getName());
        if (!matcher.find()) {
            return templateVariable.toString();
        }
        String queryName = matcher.group(1);
        String queryValue = templateVariable.getRequest().getParameter(queryName);
        return StringUtils.isNotBlank(queryValue) ? queryValue : templateVariable.toString();
    }

}
