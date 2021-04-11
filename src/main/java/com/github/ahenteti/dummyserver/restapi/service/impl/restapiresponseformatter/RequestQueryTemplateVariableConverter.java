package com.github.ahenteti.dummyserver.restapi.service.impl.restapiresponseformatter;

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
        Pattern pattern = Pattern.compile("request\\.query\\.([^\\[]+)(\\[(\\d+)])?");
        Matcher matcher = pattern.matcher(templateVariable.getName());
        if (!matcher.find()) {
            return templateVariable.toString();
        }
        String queryName = matcher.group(1);
        int queryNumber = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 1;
        String[] queryValues = templateVariable.getRequest().getParameterMap().get(queryName);
        if (queryValues == null) {
            return templateVariable.toString();
        }
        try {
            return queryValues[queryNumber - 1];
        } catch (Exception e) {
            return templateVariable.toString();
        }
    }

}
