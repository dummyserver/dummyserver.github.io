package com.github.ahenteti.dummyserver.service.impl.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ValueUtils {

    private ValueUtils() {
        throw new IllegalStateException("Utility class contains only static methods");
    }

    private static final String CONTAINS_EXPECTATION = "contains";
    private static final String ABSENT_EXPECTATION = "absent";

    public static boolean isNotAsExpected(String value, Object expectation) {
        return !isAsExpected(value, expectation);
    }

    public static boolean isAsExpected(String value, Object expectation) {
        if (expectation instanceof String) {
            return StringUtils.equalsIgnoreCase((String) expectation, value);
        }
        if (expectation instanceof Map) {
            Map expectationMap = (Map) expectation;
            Object expectationValue = expectationMap.get(CONTAINS_EXPECTATION);
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
