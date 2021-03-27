package com.github.ahenteti.dummyserver.service.impl.responseformatter;

public class NullTemplateVariableConverter implements ITemplateVariableConverter {


    @Override
    public boolean canConvert(TemplateVariable templateVariable) {
        return false;
    }

    @Override
    public boolean cannotConvert(TemplateVariable templateVariable) {
        return true;
    }

    @Override
    public String getTemplateVariableName() {
        return "";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        return templateVariable.toString();
    }
}
