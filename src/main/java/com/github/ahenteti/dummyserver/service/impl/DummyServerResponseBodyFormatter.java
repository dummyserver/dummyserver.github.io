package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.ahenteti.dummyserver.service.IDummyServerResponseBodyFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DummyServerResponseBodyFormatter implements IDummyServerResponseBodyFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerResponseBodyFormatter.class);

    @Autowired
    private JsonMapper mapper;

    @Override
    public String format(String template) {
        String res = template;
        Pattern pattern = Pattern.compile("\\{\\{now(?<options>.*)}}");
        Matcher matcher = pattern.matcher(template);
        int lastIndex = 0;
        StringBuilder output = new StringBuilder();
        while (matcher.find()) {
            output.append(res, lastIndex, matcher.start()).append(Instant.now().toString());
            lastIndex = matcher.end();
        }
        if (lastIndex < res.length()) {
            output.append(res, lastIndex, res.length());
        }
        return output.toString();
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


}
