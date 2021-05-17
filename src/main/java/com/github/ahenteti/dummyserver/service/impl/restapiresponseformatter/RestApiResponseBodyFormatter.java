package com.github.ahenteti.dummyserver.service.impl.restapiresponseformatter;

import com.github.ahenteti.dummyserver.service.IRestApiResponseBodyFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RestApiResponseBodyFormatter implements IRestApiResponseBodyFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiResponseBodyFormatter.class);

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
    public Object format(Object body, HttpServletRequest request) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            String bodyString = format(jsonb.toJson(body), request);
            return jsonb.fromJson(bodyString, Object.class);
        } catch (Exception e) {
            LOGGER.error("error while formatting body. we return the body without formatting", e);
            return body;
        }
    }

}
