package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import org.apache.commons.lang3.StringUtils;

public interface ITemplateVariableConverter {

    default boolean canConvert(String templateVariableName, String templateVariableOptions) {
        return StringUtils.equalsIgnoreCase(getTemplateVariableName(), templateVariableName);
    }

    default boolean cannotConvert(String templateVariableName, String templateVariableOptions) {
        return !canConvert(templateVariableName, templateVariableOptions);
    }

    String getTemplateVariableName();

    String convert(String templateVariableName, String templateVariableOptions);

}
