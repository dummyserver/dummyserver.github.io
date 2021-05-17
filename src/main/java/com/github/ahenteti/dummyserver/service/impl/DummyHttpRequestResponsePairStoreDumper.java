package com.github.ahenteti.dummyserver.service.impl;

import com.github.ahenteti.dummyserver.service.IDummyHttpRequestResponsePairStore;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Aspect
@Component
public class DummyHttpRequestResponsePairStoreDumper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyHttpRequestResponsePairStoreDumper.class);

    @Autowired
    private IDummyHttpRequestResponsePairStore store;

    @Value("${dummy.http.request.response.pairs.file}")
    private String dummyHttpRequestResponsePairsFile;

    @Value("${dummy.http.request.response.pairs.file.autodump}")
    private boolean autoDump;

    @After("@annotation(com.github.ahenteti.dummyserver.service.impl.Dump)")
    public void dumpStore(JoinPoint joinPoint) {
        if (!autoDump) return;
        try (Jsonb jsonb = JsonbBuilder.create()) {
            Path dummyResponsesFilePath = Paths.get(dummyHttpRequestResponsePairsFile);
            createFileIfNotExists(dummyResponsesFilePath);
            jsonb.toJson(store.getAll(), Files.newBufferedWriter(dummyResponsesFilePath));
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
