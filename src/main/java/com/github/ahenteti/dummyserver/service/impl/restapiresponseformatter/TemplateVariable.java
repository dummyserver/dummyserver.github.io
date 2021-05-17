package com.github.ahenteti.dummyserver.service.impl.restapiresponseformatter;

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
    public static final String REMOVE_QUOTES_OPTION = "--remove-quotes";

    private String name;
    private String rawOptions;
    private String options;
    private HttpServletRequest request;
    private Matcher matcher;

    private TemplateVariable(String name, String rawOptions, String options, HttpServletRequest request, Matcher matcher) {
        this.name = name;
        this.rawOptions = rawOptions;
        this.options = this.rawOptions.replace(REMOVE_QUOTES_OPTION, "");
        this.request = request;
        this.matcher = matcher;
    }

    @Override
    public String toString() {
        return "{{" + name + rawOptions + "}}";
    }

    public boolean withOption(String option) {
        return rawOptions.contains(option);
    }

    public Optional<String> getOption(String option) {
        Pattern pattern = Pattern.compile(option + "='(.+?)'");
        Matcher matcher = pattern.matcher(rawOptions);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }

    public Optional<String> getRequestBody() {
        try {
            return Optional.of(IOUtils.toString(getRequest().getInputStream(), UTF_8));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public int start() {
        if (withOption(REMOVE_QUOTES_OPTION)) {
            return matcher.start() - 1;
        }
        return matcher.start();
    }

    public int end() {
        if (withOption(REMOVE_QUOTES_OPTION)) {
            return matcher.end() + 1;
        }
        return matcher.end();
    }

}
