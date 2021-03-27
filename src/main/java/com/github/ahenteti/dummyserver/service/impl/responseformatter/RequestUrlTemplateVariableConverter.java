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
        StringBuilder res = new StringBuilder(templateVariable.getRequest().getRequestURL().toString());
        if (templateVariable.withOption("--with-query-params")) {
            res.append("?");
            res.append(templateVariable.getRequest().getQueryString());
        }
        return res.toString();
    }

}
