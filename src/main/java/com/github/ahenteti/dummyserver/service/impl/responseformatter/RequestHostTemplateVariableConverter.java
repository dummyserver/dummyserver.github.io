package com.github.ahenteti.dummyserver.service.impl.responseformatter;

public class RequestHostTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.host";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        return templateVariable.getRequest().getServerName();
    }

}
