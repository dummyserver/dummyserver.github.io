package com.github.ahenteti.dummyserver.service.impl.restapiresponseformatter;

public class RequestSchemaTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.schema";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        return templateVariable.getRequest().getScheme();
    }

}
