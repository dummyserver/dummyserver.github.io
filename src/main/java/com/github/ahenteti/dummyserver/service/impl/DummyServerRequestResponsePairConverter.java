package com.github.ahenteti.dummyserver.service.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.ahenteti.dummyserver.exception.InternalServerErrorException;
import com.github.ahenteti.dummyserver.model.DummyServerRequest;
import com.github.ahenteti.dummyserver.model.DummyServerRequestResponsePair;
import com.github.ahenteti.dummyserver.model.DummyServerResponse;
import com.github.ahenteti.dummyserver.service.IDummyServerRequestResponsePairConverter;
import com.github.ahenteti.dummyserver.service.impl.utils.ArrayUtils;
import com.github.ahenteti.dummyserver.service.impl.utils.FileUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DummyServerRequestResponsePairConverter implements IDummyServerRequestResponsePairConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyServerRequestResponsePairConverter.class);

    private static final DummyServerRequestResponsePair[] EMPTY_ARRAY = {};

    @Autowired
    private JsonMapper jsonMapper;

    private OpenAPIV3Parser openApi3Parser = new OpenAPIV3Parser();

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
            ParseOptions parseOptions = new ParseOptions();
            parseOptions.setFlatten(true);
            OpenAPI openApi = openApi3Parser.read(openApiV3Path.toAbsolutePath().toString(), null, parseOptions);
            for (Map.Entry<String, PathItem> pathEntry : openApi.getPaths().entrySet()) {
                String pathString = pathEntry.getKey();
                PathItem path = pathEntry.getValue();
                for (Map.Entry<String, Operation> operationEntry : getOperations(path).entrySet()) {
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

    private Map<String, Operation> getOperations(PathItem path) {
        Map<String, Operation> res = new HashMap<>();
        if (path.getGet() != null) res.put("GET", path.getGet());
        if (path.getPost() != null) res.put("POST", path.getPost());
        if (path.getPut() != null) res.put("PUT", path.getPut());
        if (path.getDelete() != null) res.put("DELETE", path.getDelete());
        if (path.getOptions() != null) res.put("OPTIONS", path.getOptions());
        if (path.getHead() != null) res.put("HEAD", path.getHead());
        if (path.getPatch() != null) res.put("PATCH", path.getPatch());
        if (path.getTrace() != null) res.put("TRACE", path.getTrace());
        return res;
    }

    private DummyServerRequest toDummyServerRequest(String path, Map.Entry<String, Operation> operation) {
        DummyServerRequest res = new DummyServerRequest();
        res.setMethod(operation.getKey().toUpperCase());
        res.setPath(path.replaceAll("\\{.+?}", "(.+?)"));
        return res;
    }

    private DummyServerResponse toDummyServerResponse(Map.Entry<String, Operation> operation) {
        DummyServerResponse res = new DummyServerResponse();
        if (operation.getValue().getResponses().get("200") != null) {
            res.setStatus(200);
        } else {
            res.setStatus(404);
            for (Map.Entry<String, ApiResponse> responseEntry : operation.getValue().getResponses().entrySet()) {
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
