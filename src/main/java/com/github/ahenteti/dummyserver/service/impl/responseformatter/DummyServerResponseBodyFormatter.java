package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.ahenteti.dummyserver.service.IDummyServerResponseBodyFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DummyServerResponseBodyFormatter implements IDummyServerResponseBodyFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerResponseBodyFormatter.class);

    @Autowired
    private JsonMapper mapper;

    @Autowired
    private TemplateVariableConverterFactory templateVariableConverterFactory;

    private ITemplateVariableConverter templateVariableConverter;

    @PostConstruct
    public void init() {
        this.templateVariableConverter = templateVariableConverterFactory.create();
    }

    @Override
    public String format(String template, HttpServletRequest request) {
        try {
            Pattern pattern = Pattern.compile("\\{\\{([^\\s}]+)(.*?)}}");
            Matcher matcher = pattern.matcher(template);
            StringBuilder output = new StringBuilder();
            int lastIndex = 0;
            while (matcher.find()) {
                TemplateVariable templateVariable = TemplateVariable.builder().name(matcher.group(1))
                        .rawOptions(matcher.group(2)).request(request).matcher(matcher).build();
                output.append(template, lastIndex, templateVariable.start());
                output.append(templateVariableConverter.convert(templateVariable));
                lastIndex = templateVariable.end();
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

    @Override
    public JsonNode format(JsonNode body, HttpServletRequest request) {
        try {
            String bodyString = format(body.toString(), request);
            return mapper.readTree(bodyString);
        } catch (Exception e) {
            LOGGER.error("error while formatting body. we return the body without formatting", e);
            return body;
        }
    }

}
