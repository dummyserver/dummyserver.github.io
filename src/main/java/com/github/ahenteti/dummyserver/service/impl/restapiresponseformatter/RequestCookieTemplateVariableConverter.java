package com.github.ahenteti.dummyserver.service.impl.restapiresponseformatter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestCookieTemplateVariableConverter extends BaseTemplateVariableConverter {

    @Override
    public String getTemplateVariableName() {
        return "request.cookies";
    }

    @Override
    public boolean canConvert(TemplateVariable templateVariable) {
        return StringUtils.startsWithIgnoreCase(templateVariable.getName(), getTemplateVariableName());
    }

    @Override
    public String convert(TemplateVariable templateVariable) {
        if (cannotConvert(templateVariable)) {
            return super.convert(templateVariable);
        }
        Pattern pattern = Pattern.compile("request\\.cookies\\.([^.]+)");
        Matcher matcher = pattern.matcher(templateVariable.getName());
        if (!matcher.find()) {
            return templateVariable.toString();
        }
        String cookieName = matcher.group(1);
        Cookie[] cookies = templateVariable.getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equalsIgnoreCase(cookieName, cookie.getName())) {
                    return cookieName;
                }
            }
        }
        return templateVariable.toString();
    }

}
