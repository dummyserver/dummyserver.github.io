package com.github.ahenteti.dummyserver.service.impl.restapiresponseformatter;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TemplateVariableConverterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateVariableConverterFactory.class);

    private Reflections reflections = new Reflections("com.github.ahenteti.dummyserver");

    public ITemplateVariableConverter create() {
        List<BaseTemplateVariableConverter> converters = reflections.getSubTypesOf(BaseTemplateVariableConverter.class)
                .stream().map(this::toNewInstance).filter(Objects::nonNull).collect(Collectors.toList());
        if (converters.isEmpty()) {
            return new NullTemplateVariableConverter();
        }
        for (int i = 0; i < converters.size() - 1; i++) {
            converters.get(i).setNext(converters.get(i + 1));
        }
        return converters.get(0);
    }

    private BaseTemplateVariableConverter toNewInstance(Class<? extends BaseTemplateVariableConverter> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LOGGER.debug("error while creating new instance of {} class. we will ignore it", controller);
            return null;
        }
    }
}
