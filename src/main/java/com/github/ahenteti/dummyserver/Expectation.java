package com.github.ahenteti.dummyserver;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class Expectation {

    private static final String EQUALS_TO_EXPECTATION = "equalsTo";
    private static final String CONTAINS_EXPECTATION = "contains";
    private static final String ABSENT_EXPECTATION = "absent";

    public static boolean valueNotAsExpected(String value, Object expectation) {
        return !valueAsExpected(value, expectation);
    }

    public static boolean valueAsExpected(String value, Object expectation) {
        if (expectation instanceof String) {
            return StringUtils.equalsIgnoreCase((String) expectation, value);
        }
        if (expectation instanceof Map) {
            Map expectationMap = (Map) expectation;
            Object expectationValue = expectationMap.get(EQUALS_TO_EXPECTATION);
            if (expectationValue instanceof String) {
                return StringUtils.equalsIgnoreCase(value, (String) expectationValue);
            }

            expectationValue = expectationMap.get(CONTAINS_EXPECTATION);
            if (expectationValue instanceof String) {
                return StringUtils.containsIgnoreCase(value, (String) expectationValue);
            }

            expectationValue = expectationMap.get(ABSENT_EXPECTATION);
            if (expectationValue != null) {
                return StringUtils.isBlank(value);
            }
        }
        return false;
    }
}
