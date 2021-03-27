package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

@Builder
@Getter
public class TemplateVariable {
    private String name;
    private String options;
    private HttpServletRequest request;

    @Override
    public String toString() {
        return "{{" + name + options + "}}";
    }

    public boolean withOption(String option) {
        return options.contains(option);
    }

    public Optional<String> getOption(String option) {
        Pattern pattern = Pattern.compile(option + "='(.+?)'");
        Matcher matcher = pattern.matcher(options);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }

    public Optional<String> getRequestBody() {
        try {
            return Optional.of(IOUtils.toString(getRequest().getInputStream(), UTF_8));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
