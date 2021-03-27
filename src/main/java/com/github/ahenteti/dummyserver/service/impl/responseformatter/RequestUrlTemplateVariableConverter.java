package com.github.ahenteti.dummyserver.service.impl.responseformatter;

public class RequestUrlTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.url";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        return templateVariable.getRequest().getRequestURL().toString();
    }

}
