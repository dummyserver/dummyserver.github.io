package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneOfTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public boolean canConvert(String templateVariableName, String templateVariableOptions) {
        return StringUtils.equalsIgnoreCase("oneOf", templateVariableName);
    }

    @Override
    public String convert(String templateVariableName, String templateVariableOptions) {
        if (!canConvert(templateVariableName, templateVariableOptions)) {
            return super.convert(templateVariableName, templateVariableOptions);
        }
        Pattern pattern = Pattern.compile(" '?([^\\s']+)'?");
        Matcher optionsMatcher = pattern.matcher(templateVariableOptions);
        List<String> optionsAsList = new ArrayList<>();
        while (optionsMatcher.find()) {
            optionsAsList.add(optionsMatcher.group(1));
        }
        if (optionsAsList.isEmpty()) {
            return "";
        }
        return optionsAsList.get(ThreadLocalRandom.current().nextInt(optionsAsList.size()));
    }

}
