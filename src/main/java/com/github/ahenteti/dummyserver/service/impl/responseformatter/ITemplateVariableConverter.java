package com.github.ahenteti.dummyserver.service.impl.responseformatter;

public interface ITemplateVariableConverter {

    boolean canConvert(String templateVariableName, String templateVariableOptions);

    String convert(String templateVariableName, String templateVariableOptions);

}
