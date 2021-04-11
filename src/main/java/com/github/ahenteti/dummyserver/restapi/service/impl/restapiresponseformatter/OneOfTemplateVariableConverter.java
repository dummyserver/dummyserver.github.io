package com.github.ahenteti.dummyserver.restapi.service.impl.restapiresponseformatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneOfTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "oneOf";
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        Pattern pattern = Pattern.compile(" '?([^\\s']+)'?");
        Matcher optionsMatcher = pattern.matcher(templateVariable.getOptions());
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
