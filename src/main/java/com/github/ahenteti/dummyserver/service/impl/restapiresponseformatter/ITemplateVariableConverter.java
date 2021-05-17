package com.github.ahenteti.dummyserver.service.impl.restapiresponseformatter;

import org.apache.commons.lang3.StringUtils;

public interface ITemplateVariableConverter {

    default boolean canConvert(TemplateVariable templateVariable) {
        return StringUtils.equalsIgnoreCase(getTemplateVariableName(), templateVariable.getName());
    }

    default boolean cannotConvert(TemplateVariable templateVariable) {
        return !canConvert(templateVariable);
    }

    String getTemplateVariableName();

    String convert(TemplateVariable templateVariable);

}
