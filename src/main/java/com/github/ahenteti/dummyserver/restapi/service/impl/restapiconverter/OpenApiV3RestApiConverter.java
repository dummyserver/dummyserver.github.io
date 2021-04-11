package com.github.ahenteti.dummyserver.restapi.service.impl.restapiconverter;

import com.github.ahenteti.dummyserver.common.InternalServerErrorException;
import com.github.ahenteti.dummyserver.restapi.model.RestApiRequest;
import com.github.ahenteti.dummyserver.restapi.model.RestApi;
import com.github.ahenteti.dummyserver.restapi.model.RestApiResponse;
import com.github.ahenteti.dummyserver.restapi.service.IRestApiConverter;
import com.github.ahenteti.dummyserver.restapi.service.impl.utils.ArrayUtils;
import com.github.ahenteti.dummyserver.restapi.service.impl.utils.FileUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenApiV3RestApiConverter implements IRestApiConverter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(OpenApiV3RestApiConverter.class);

    private OpenAPIV3Parser openApi3Parser = new OpenAPIV3Parser();

    private OpenApiV3SchemaConverter schemaConverter = new OpenApiV3SchemaConverter();

    private Map<String, Object> openApiSchemas;

    private OpenAPI openApi;

    @Override
    public RestApi[] toRequestResponsePairs(String requestBody) {
        Path openApiV3Path = null;
        try {
            List<RestApi> res = new ArrayList<>();
            openApiV3Path = Files.createTempFile("openapiv3", ".json");
            Files.writeString(openApiV3Path, requestBody);
            openApi = openApi3Parser.read(openApiV3Path.toAbsolutePath().toString());
            openApiSchemas = schemaConverter.getModels(openApi);
            for (Map.Entry<String, PathItem> pathEntry : openApi.getPaths().entrySet()) {
                String pathString = pathEntry.getKey();
                PathItem path = pathEntry.getValue();
                for (Map.Entry<String, Operation> operationEntry : getOperations(path).entrySet()) {
                    RestApi reqResPair = new RestApi();
                    reqResPair.setName(operationEntry.getKey().toUpperCase() + " " + pathString);
                    reqResPair.setDescription(operationEntry.getValue().getDescription());
                    reqResPair.setRequest(toDummyServerRequest(pathString, operationEntry));
                    reqResPair.setResponse(toDummyServerResponse(operationEntry));
                    res.add(reqResPair);
                }
            }

            return ArrayUtils.toArray(res, RestApi.class);
        } catch (Exception e) {
            throw new InternalServerErrorException("error while converting responseBody to an array of DummyServerRequestResponsePair. requestBody: " + requestBody, e);
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

    private RestApiRequest toDummyServerRequest(String path, Map.Entry<String, Operation> operation) {
        RestApiRequest res = new RestApiRequest();
        res.setMethod(operation.getKey().toUpperCase());
        res.setPath("/api" + path.replaceAll("\\{.+?}", "(.+?)"));
        return res;
    }

    private RestApiResponse toDummyServerResponse(Map.Entry<String, Operation> operation) {
        RestApiResponse res = new RestApiResponse();
        ApiResponse okResponse = operation.getValue().getResponses().get("200");
        if (okResponse != null) {
            res.setStatus(200);
            MediaType jsonResponse = okResponse.getContent().get("application/json");
            if (jsonResponse != null) {
                Schema schema = jsonResponse.getSchema();
                if (schema.get$ref() != null && openApiSchemas.get(schema.get$ref()) != null) {
                    res.setBody(openApiSchemas.get(schema.get$ref()));
                } else {
                    res.setBody(schemaConverter.convert(schema, openApi));
                }
            }
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
