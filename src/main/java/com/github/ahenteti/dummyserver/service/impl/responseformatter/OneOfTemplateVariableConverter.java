package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneOfTemplateVariableConverter implements Function<Matcher, String> {

    public static final String OPTIONS_GROUP_NAME = "options";
    public static final String ONE_OF_REGEX = String.format("\\{\\{oneOf(?<%s>.*?)}}", OPTIONS_GROUP_NAME);

    @Override
    public String apply(Matcher matcher) {
        String options = matcher.group(OPTIONS_GROUP_NAME);
        Pattern pattern = Pattern.compile(" '?([^\\s']+)'?");
        Matcher optionsMatcher = pattern.matcher(options);
        List<String> optionsAsList = new ArrayList<>();
        while (optionsMatcher.find()) {
            optionsAsList.add(optionsMatcher.group(1));
        }
        if (optionsAsList.isEmpty()) {
            return matcher.group();
        }
        return optionsAsList.get(ThreadLocalRandom.current().nextInt(optionsAsList.size()));
    }

}
