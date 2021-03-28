package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.ahenteti.dummyserver.exception.InternalServerErrorException;
import com.github.ahenteti.dummyserver.model.DummyServerRequest;
import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;
import com.github.ahenteti.dummyserver.model.DummyServerResponse;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairConverter;
import com.github.ahenteti.dummyserver.service.impl.utils.ArrayUtils;
import com.github.ahenteti.dummyserver.service.impl.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.model.v3.OpenApi3;
import org.openapi4j.parser.model.v3.Operation;
import org.openapi4j.parser.model.v3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DummyServerRequestResponsePairConverter implements IDummyServerRequestResponsePairConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerRequestResponsePairConverter.class);

    private static final DummyServerRequestResponsePair[] EMPTY_ARRAY = {};

    @Autowired
    private JsonMapper jsonMapper;

    private OpenApi3Parser openApi3Parser = new OpenApi3Parser();

    @Override
    public DummyServerRequestResponsePair[] toRequestResponsePairs(String requestBody, String rawRequestBodyFormat) {
        try {
            String requestBodyFormat = rawRequestBodyFormat.replaceAll("[-.]", "");
            if (StringUtils.equalsIgnoreCase("default", requestBodyFormat)) {
                return jsonMapper.readValue(requestBody, DummyServerRequestResponsePair[].class);
            }
            if (StringUtils.equalsIgnoreCase("openapiv3", requestBodyFormat)) {
                return toRequestResponsePairsFromOpenApiV3(requestBody);
            }
            throw new InternalServerErrorException("unknown requestBodyFormat: " + rawRequestBodyFormat);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder("error while converting responseBody to an array of DummyServerRequestResponsePair. requestBody: ");
            message.append(requestBody);
            message.append(", requestBodyFormat: ");
            message.append(rawRequestBodyFormat);
            throw new InternalServerErrorException(message.toString(), e);
        }
    }

    private DummyServerRequestResponsePair[] toRequestResponsePairsFromOpenApiV3(String openApiV3Content) {
        Path openApiV3Path = null;
        try {
            List<DummyServerRequestResponsePair> res = new ArrayList<>();
            openApiV3Path = Files.createTempFile("openapiv3", ".json");
            Files.writeString(openApiV3Path, openApiV3Content);
            OpenApi3 openApi = openApi3Parser.parse(openApiV3Path.toFile(), false);
            for (Map.Entry<String, org.openapi4j.parser.model.v3.Path> pathEntry : openApi.getPaths().entrySet()) {
                String pathString = pathEntry.getKey();
                org.openapi4j.parser.model.v3.Path path = pathEntry.getValue();
                for (Map.Entry<String, Operation> operationEntry : path.getOperations().entrySet()) {
                    DummyServerRequestResponsePair reqResPair = new DummyServerRequestResponsePair();
                    reqResPair.setName(operationEntry.getKey().toUpperCase() + " " + pathString);
                    reqResPair.setDescription(operationEntry.getValue().getDescription());
                    reqResPair.setRequest(toDummyServerRequest(pathString, operationEntry));
                    reqResPair.setResponse(toDummyServerResponse(operationEntry));
                    res.add(reqResPair);
                }
            }
            return ArrayUtils.toArray(res, DummyServerRequestResponsePair.class);
        } catch (Exception e) {
            return EMPTY_ARRAY;
        } finally {
            FileUtils.deleteSilently(openApiV3Path);
        }
    }

    private DummyServerRequest toDummyServerRequest(String path, Map.Entry<String, Operation> operation) {
        DummyServerRequest res = new DummyServerRequest();
        res.setMethod(operation.getKey().toUpperCase());
        res.setPath(path.replaceAll("\\{.+?}", "(.+?)"));
        return res;
    }

    private DummyServerResponse toDummyServerResponse(Map.Entry<String, Operation> operation) {
        DummyServerResponse res = new DummyServerResponse();
        if (operation.getValue().getResponse("200") != null) {
            res.setStatus(200);
        } else {
            res.setStatus(404);
            for (Map.Entry<String, Response> responseEntry : operation.getValue().getResponses().entrySet()) {
                try {
                    res.setStatus(Integer.parseInt(responseEntry.getKey()));
                    break;
                } catch (Exception e) {
                    LOGGER.debug("error while parsing response status. we log the error and continue processing the swagger file", e);
                }
            }
        }
        return res;
    }

}
