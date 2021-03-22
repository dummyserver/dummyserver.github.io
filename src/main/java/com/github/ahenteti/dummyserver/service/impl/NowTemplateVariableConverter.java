package com.github.ahenteti.dummyserver.service.impl;

import java.time.Instant;
import java.util.function.Function;
import java.util.regex.Matcher;

public class NowTemplateVariableConverter implements Function<Matcher, String> {

    public static final String REGEX = "\\{\\{now(?<options>.*?)}}";

    @Override
    public String apply(Matcher matcher) {
        return Instant.now().toString();
    }
}
