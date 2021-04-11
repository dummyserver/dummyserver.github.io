package com.github.ahenteti.dummyserver.restapi.service.impl.restapiresponseformatter;

public class RequestPortTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.port";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        return String.valueOf(templateVariable.getRequest().getServerPort());
    }

}
