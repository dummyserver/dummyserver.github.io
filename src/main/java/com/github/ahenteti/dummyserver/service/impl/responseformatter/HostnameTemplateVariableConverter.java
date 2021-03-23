package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.function.Function;
import java.util.regex.Matcher;

public class HostnameTemplateVariableConverter implements Function<Matcher, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostnameTemplateVariableConverter.class);

    private static String HOSTNAME;

    static {
        setHostname();
    }

    public static final String HOSTNAME_REGEX = "\\{\\{hostname}}";

    @Override
    public String apply(Matcher matcher) {
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
