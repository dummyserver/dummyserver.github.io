package com.github.ahenteti.dummyserver.service.impl;

import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;

public class RandomValueTemplateVariableConverter implements Function<Matcher, String> {

    public static final String REGEX = "\\{\\{randomValue(?<options>.*?)}}";

    @Override
    public String apply(Matcher matcher) {
        return UUID.randomUUID().toString();
    }
}
