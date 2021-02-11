package com.github.ahenteti.dummyserver;

import org.apache.commons.lang3.StringUtils;

public class ValueExpectation {
    private String equalsTo;
    private String contains;
    private Boolean absent;

    public boolean isIncorrect(String value) {
        return !isCorrect(value);
    }

    public boolean isCorrect(String value) {
        if (equalsTo != null) {
            return StringUtils.equalsIgnoreCase(equalsTo, value);
        }
        if (contains != null) {
            return StringUtils.containsIgnoreCase(value, contains);
        }
        if (absent != null) {
            return StringUtils.isBlank(value);
        }
        return false;
    }
}
