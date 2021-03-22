package com.github.ahenteti.dummyserver.service.impl;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomValueTemplateVariableConverter implements Function<Matcher, String> {

    public static final String OPTIONS_GROUP_NAME = "options";
    public static final String REGEX = String.format("\\{\\{randomValue(?<%s>.*?)}}", OPTIONS_GROUP_NAME);
    public static final int RANDOM_VALUE_DEFAULT_LENGTH = 10;
    public static final String RANDOM_VALUE_DEFAULT_TYPE = "ALPHANUMERIC";

    @Override
    public String apply(Matcher matcher) {
        String options = matcher.group(OPTIONS_GROUP_NAME);
        int length = getLength(options);
        String type = getType(options);
        boolean uppercase = getUppercase(options);
        boolean lowercase = getLowercase(options);

        String rawValue;
        switch (type) {
            case "UUID":
                rawValue = UUID.randomUUID().toString();
                break;
            case "ALPHABETIC":
                rawValue = RandomStringUtils.randomAlphabetic(length);
                break;
            case "ASCII":
                rawValue = RandomStringUtils.randomAscii(length);
                break;
            case "NUMERIC":
                rawValue = RandomStringUtils.randomNumeric(length);
                break;
            default:
                rawValue = RandomStringUtils.randomAlphanumeric(length);
                break;
        }
        if (uppercase) return rawValue.toUpperCase();
        if (lowercase) return rawValue.toLowerCase();
        return rawValue;
    }

    private int getLength(String options) {
        Pattern pattern = Pattern.compile("--length=(\\d+)");
        Matcher matcher = pattern.matcher(options);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return RANDOM_VALUE_DEFAULT_LENGTH;
    }

    private String getType(String options) {
        Pattern pattern = Pattern.compile("--type='(.+?)'");
        Matcher matcher = pattern.matcher(options);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return RANDOM_VALUE_DEFAULT_TYPE;
    }

    private boolean getUppercase(String options) {
        Pattern pattern = Pattern.compile("--uppercase");
        Matcher matcher = pattern.matcher(options);
        return matcher.find();
    }

    private boolean getLowercase(String options) {
        Pattern pattern = Pattern.compile("--lowercase");
        Matcher matcher = pattern.matcher(options);
        return matcher.find();
    }
}
