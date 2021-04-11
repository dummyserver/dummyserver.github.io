package com.github.ahenteti.dummyserver.restapi.service.impl.restapiresponseformatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class HostnameTemplateVariableConverter extends BaseTemplateVariableConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostnameTemplateVariableConverter.class);

    private static String HOSTNAME;

    static {
        setHostname();
    }

    @Override
    public String getTemplateVariableName() {
        return "hostname";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        return HOSTNAME;
    }

    private static void setHostname() {
        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            LOGGER.error("Failed to look up hostname. we will return localhost");
            HOSTNAME = "localhost";
        }
    }
}
