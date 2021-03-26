package com.github.ahenteti.dummyserver.service.impl.responseformatter;

public class RequestPathTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.path";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        return templateVariable.getRequest().getRequestURI();
    }

}
