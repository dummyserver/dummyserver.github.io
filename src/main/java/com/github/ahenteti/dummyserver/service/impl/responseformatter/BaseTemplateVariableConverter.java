package com.github.ahenteti.dummyserver.service.impl.responseformatter;

public abstract class BaseTemplateVariableConverter implements ITemplateVariableConverter {

    private ITemplateVariableConverter next;

    @Override
    public String convert(String templateVariableName, String templateVariableOptions) {
        if (next != null) {
            return next.convert(templateVariableName, templateVariableOptions);
        }
        return templateVariableName + templateVariableOptions;
    }

    public void setNext(ITemplateVariableConverter next) {
        this.next = next;
    }
}
