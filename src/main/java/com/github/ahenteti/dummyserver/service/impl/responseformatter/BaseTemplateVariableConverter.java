package com.github.ahenteti.dummyserver.service.impl.responseformatter;

public abstract class BaseTemplateVariableConverter implements ITemplateVariableConverter {

    private ITemplateVariableConverter next;

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (next != null) {
            return next.convert(templateVariable);
        }
        return templateVariable.getName();
    }

    public void setNext(ITemplateVariableConverter next) {
        this.next = next;
    }
}
