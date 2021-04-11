package com.github.ahenteti.dummyserver.restapi.service.impl.restapiconverter;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.PasswordSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import org.apache.commons.lang3.StringUtils;

import javax.json.Json;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.ahenteti.dummyserver.restapi.service.impl.restapiresponseformatter.TemplateVariable.REMOVE_QUOTES_OPTION;

public class OpenApiV3SchemaConverter {

    public Map<String, Object> getModels(OpenAPI openApi) {
        Map<String, Object> res = new HashMap<>();
        for (Map.Entry<String, Schema> entry : openApi.getComponents().getSchemas().entrySet()) {
            res.put("#/components/schemas/" + entry.getKey(), convert(entry.getValue(), openApi));
        }
        return res;
    }

    public Object convert(Schema<?> schema, OpenAPI openApi) {
        if (schema instanceof ObjectSchema) return convert((ObjectSchema) schema, openApi);
        if (schema instanceof ArraySchema) return convert((ArraySchema) schema, openApi);
        if (schema instanceof IntegerSchema) return "{{randomValue --type='NUMERIC' " + REMOVE_QUOTES_OPTION + "}}";
        if (schema instanceof NumberSchema) return "{{randomValue --type='NUMERIC' " + REMOVE_QUOTES_OPTION + "}}";
        if (schema instanceof DateSchema) return "{{now}}";
        if (schema instanceof DateTimeSchema) return "{{now}}";
        if (schema instanceof BooleanSchema) return "{{oneOf true false " + REMOVE_QUOTES_OPTION + "}}";
        if (schema instanceof UUIDSchema) return "{{randomValue --type='UUID'}}";
        if (schema instanceof BinarySchema) return "{{randomValue --type='ASCII'}}";
        if (schema instanceof EmailSchema) return "username@mail.com";
        if (schema instanceof StringSchema) return "{{randomValue --type='ALPHABETIC'}}";
        if (schema instanceof PasswordSchema) return "{{randomValue --type='ASCII'}}";
        if (schema.get$ref() != null) return convert(getSchemaRef(schema, openApi), openApi);
        return Json.createObjectBuilder().build();
    }

    private Object convert(ObjectSchema schema, OpenAPI openApi) {
        Map<String, Object> res = new HashMap<>();
        for (Map.Entry<String, Schema> entry : schema.getProperties().entrySet()) {
            res.put(entry.getKey(), convert(entry.getValue(), openApi));
        }
        return res;
    }

    private Object convert(ArraySchema schema, OpenAPI openApi) {
        List<Object> res = new ArrayList<>();
        res.add(convert(schema.getItems(), openApi));
        return res;
    }

    private Schema getSchemaRef(Schema<?> schema, OpenAPI openApi) {
        return openApi.getComponents().getSchemas().get(getSchemaRefName(schema));
    }

    private String getSchemaRefName(Schema<?> schema) {
        return StringUtils.substringAfterLast(schema.get$ref(), "/");
    }
}
