package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import org.springframework.stereotype.Service;

@Service
public class TemplateVariableConverterFactory {

    public ITemplateVariableConverter create() {
        BaseTemplateVariableConverter c1 = new HostnameTemplateVariableConverter();
        BaseTemplateVariableConverter c2 = new NowTemplateVariableConverter();
        BaseTemplateVariableConverter c3 = new OneOfTemplateVariableConverter();
        BaseTemplateVariableConverter c4 = new RandomValueTemplateVariableConverter();
        c1.setNext(c2);
        c2.setNext(c3);
        c3.setNext(c4);
        return c1;
    }
}
