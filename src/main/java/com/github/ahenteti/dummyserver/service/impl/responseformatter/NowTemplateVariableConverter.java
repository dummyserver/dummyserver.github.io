package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NowTemplateVariableConverter extends BaseTemplateVariableConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NowTemplateVariableConverter.class);
    public static final int DEFAULT_DATE_TIME_CHANGE = 0;
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    @Override
    public String getTemplateVariableName() {
        return "now";
    }

    @Override
    public String convert(String templateVariableName, String templateVariableOptions) {
        if (cannotConvert(templateVariableName, templateVariableOptions)) {
            return super.convert(templateVariableName, templateVariableOptions);
        }
        ZonedDateTime res = ZonedDateTime.now(getDateTimeZoneId(templateVariableOptions));
        res = res.plus(getDateTimeChange(templateVariableOptions, "+", "years", "y"), ChronoUnit.YEARS);
        res = res.minus(getDateTimeChange(templateVariableOptions, "-", "years", "y"), ChronoUnit.YEARS);
        res = res.plus(getDateTimeChange(templateVariableOptions, "+", "month", "M"), ChronoUnit.MONTHS);
        res = res.minus(getDateTimeChange(templateVariableOptions, "-", "month", "M"), ChronoUnit.MONTHS);
        res = res.plus(getDateTimeChange(templateVariableOptions, "+", "days", "d"), ChronoUnit.DAYS);
        res = res.minus(getDateTimeChange(templateVariableOptions, "-", "days", "d"), ChronoUnit.DAYS);
        res = res.plus(getDateTimeChange(templateVariableOptions, "+", "hours", "H"), ChronoUnit.HOURS);
        res = res.minus(getDateTimeChange(templateVariableOptions, "-", "hours", "H"), ChronoUnit.HOURS);
        res = res.plus(getDateTimeChange(templateVariableOptions, "+", "minutes", "m"), ChronoUnit.MINUTES);
        res = res.minus(getDateTimeChange(templateVariableOptions, "-", "minutes", "m"), ChronoUnit.MINUTES);
        res = res.plus(getDateTimeChange(templateVariableOptions, "+", "seconds", "s"), ChronoUnit.SECONDS);
        res = res.minus(getDateTimeChange(templateVariableOptions, "-", "seconds", "s"), ChronoUnit.SECONDS);
        return res.format(getDateTimeFormatter(templateVariableOptions));
    }

    private int getDateTimeChange(String options, String sign, String symbol, String shortSymbol) {
        Pattern pattern = Pattern.compile(String.format("\\%s(\\d+)(%s|%s)", sign, symbol, shortSymbol));
        Matcher matcher = pattern.matcher(options);
        if (matcher.find()) return Integer.parseInt(matcher.group(1));
        return DEFAULT_DATE_TIME_CHANGE;
    }

    private ZoneId getDateTimeZoneId(String options) {
        Pattern pattern = Pattern.compile("--zoneId='(.+?)'");
        Matcher matcher = pattern.matcher(options);
        if (matcher.find()) {
            String zoneIdString = matcher.group(1);
            try {
                return ZoneId.of(zoneIdString);
            } catch (Exception e) {
                LOGGER.debug("invalid zoneId: {}. we ignore the error and use UTC zonId", zoneIdString, e);
                return DEFAULT_ZONE_ID;
            }
        }
        return DEFAULT_ZONE_ID;
    }

    private DateTimeFormatter getDateTimeFormatter(String options) {
        Pattern pattern = Pattern.compile("--format='(.+?)'");
        Matcher matcher = pattern.matcher(options);
        if (matcher.find()) {
            String format = matcher.group(1);
            try {
                return DateTimeFormatter.ofPattern(format);
            } catch (Exception e) {
                LOGGER.debug("invalid format: {}. we ignore the error and use UTC zonId", format, e);
                return DEFAULT_DATETIME_FORMATTER;
            }
        }
        return DEFAULT_DATETIME_FORMATTER;
    }
}
