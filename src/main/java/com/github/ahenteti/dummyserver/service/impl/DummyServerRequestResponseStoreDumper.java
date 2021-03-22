package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponseStore;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Aspect
@Component
public class DummyServerRequestResponseStoreDumper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerRequestResponseStoreDumper.class);

    @Autowired
    private IDummyServerRequestResponseStore store;

    @Autowired
    private JsonMapper jsonMapper;

    @Value("${dummy.responses.file}")
    private String dummyResponsesFile;

    @After("@annotation(com.github.ahenteti.dummyserver.service.impl.DumpStore)")
    public void dumpStore(JoinPoint joinPoint) {
        try {
            Path dummyResponsesFilePath = Paths.get(dummyResponsesFile);
            createFileIfNotExists(dummyResponsesFilePath);
            jsonMapper.writeValue(dummyResponsesFilePath.toFile(), store.getAll());
        } catch (Exception e) {
            LOGGER.error("error while dumping dummyserver store. we log the error and return", e);
        }
    }

    private void createFileIfNotExists(Path path) throws IOException {
        Files.createDirectories(path.toAbsolutePath().getParent());
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

}
