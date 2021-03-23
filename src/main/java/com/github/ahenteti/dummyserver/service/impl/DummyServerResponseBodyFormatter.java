package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.ahenteti.dummyserver.service.IDummyServerResponseBodyFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.ahenteti.dummyserver.service.impl.HostnameTemplateVariableConverter.HOSTNAME_REGEX;
import static com.github.ahenteti.dummyserver.service.impl.NowTemplateVariableConverter.NOW_REGEX;
import static com.github.ahenteti.dummyserver.service.impl.OneOfTemplateVariableConverter.ONE_OF_REGEX;
import static com.github.ahenteti.dummyserver.service.impl.RandomValueTemplateVariableConverter.RANDOM_VALUE_REGEX;

@Service
public class DummyServerResponseBodyFormatter implements IDummyServerResponseBodyFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerResponseBodyFormatter.class);

    @Autowired
    private JsonMapper mapper;

    @Override
    public String format(String template) {
        String res = format(template, NOW_REGEX, new NowTemplateVariableConverter());
        res = format(res, RANDOM_VALUE_REGEX, new RandomValueTemplateVariableConverter());
        res = format(res, ONE_OF_REGEX, new OneOfTemplateVariableConverter());
        res = format(res, HOSTNAME_REGEX, new HostnameTemplateVariableConverter());
        return res;
    }

    @Override
    public JsonNode format(JsonNode body) {
        try {
            String bodyString = format(body.toString());
            return mapper.readTree(bodyString);
        } catch (Exception e) {
            LOGGER.error("error while formatting body. we return the body without formatting", e);
            return body;
        }
    }

    private String format(String template, String regex, Function<Matcher, String> converter) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(template);
            int lastIndex = 0;
            StringBuilder output = new StringBuilder();
            while (matcher.find()) {
                output.append(template, lastIndex, matcher.start()).append(converter.apply(matcher));
                lastIndex = matcher.end();
            }
            if (lastIndex < template.length()) {
                output.append(template, lastIndex, template.length());
            }
            return output.toString();
        } catch (Exception e) {
            LOGGER.error("error while formatting body. we return the template without formatting", e);
            return template;
        }
    }

}
