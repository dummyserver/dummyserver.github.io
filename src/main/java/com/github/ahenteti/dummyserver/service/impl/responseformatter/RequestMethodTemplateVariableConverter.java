package com.github.ahenteti.dummyserver.service.impl.responseformatter;

public class RequestMethodTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.method";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        return templateVariable.getRequest().getMethod();
    }

}
