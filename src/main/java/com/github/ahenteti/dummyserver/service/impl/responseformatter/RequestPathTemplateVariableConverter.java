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
        StringBuilder res = new StringBuilder(templateVariable.getRequest().getRequestURI());
        if (templateVariable.withOption("--with-query-params")) {
            res.append("?");
            res.append(templateVariable.getRequest().getQueryString());
        }
        return res.toString();
    }

}
