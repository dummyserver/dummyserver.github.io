package com.github.ahenteti.dummyserver.service.impl.restapiresponseformatter;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomValueTemplateVariableConverter extends BaseTemplateVariableConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomValueTemplateVariableConverter.class);

    public static final int RANDOM_VALUE_DEFAULT_LENGTH = 10;
    public static final String RANDOM_VALUE_DEFAULT_TYPE = "ALPHANUMERIC";

    @Override
    public String getTemplateVariableName() {
        return "randomValue";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        String options = templateVariable.getOptions();
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
                rawValue = randomNumeric(length);
                break;
            default:
                rawValue = RandomStringUtils.randomAlphanumeric(length);
                break;
        }
        if (uppercase) return rawValue.toUpperCase();
        if (lowercase) return rawValue.toLowerCase();
        return rawValue;
    }

    private String randomNumeric(int length) {
        try {
            return Integer.valueOf(RandomStringUtils.randomNumeric(length)).toString();
        } catch (Exception e) {
            LOGGER.debug("error while generating random number", e);
            return "1";
        }
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
