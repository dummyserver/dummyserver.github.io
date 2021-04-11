package com.github.ahenteti.dummyserver.restapi;

import com.github.ahenteti.dummyserver.restapi.model.RestApi;
import com.github.ahenteti.dummyserver.restapi.model.RestApiResponse;
import com.github.ahenteti.dummyserver.restapi.service.IRestApiConverter;
import com.github.ahenteti.dummyserver.restapi.service.IRestApiConverterFactory;
import com.github.ahenteti.dummyserver.restapi.service.IRestApiStore;
import com.github.ahenteti.dummyserver.restapi.service.IRestApiResponseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@RestController
public class RestApiController {

    public static final int FIRST_RESPONSE = 0;

    @Autowired
    private IRestApiStore requestResponsePairStore;

    @Autowired
    private IRestApiConverterFactory requestResponsePairConverterFactory;

    @Autowired
    private IRestApiResponseConverter responseConverter;


    @PostMapping("/__admin/rest-api")
    public void addDummyResponse(@RequestBody RestApi request) {
        this.requestResponsePairStore.add(request);
    }

    @DeleteMapping("/__admin/rest-api")
    public void removeDummyResponse(@RequestBody RestApi request) {
        this.requestResponsePairStore.remove(request);
    }

    @GetMapping("/__admin/rest-apis")
    public Set<RestApi> getDummyResponseList() {
        return this.requestResponsePairStore.getAll();
    }

    @PostMapping("/__admin/rest-apis")
    public void addDummyResponseList(@RequestBody String requestBody, @RequestParam(value = "format", required = false) String requestBodyFormatRequestParam, @RequestHeader(value = "X-Request-Body-Format", required = false) String requestBodyFormatRequestHeader) {
        String requestBodyFormat = getRequestBodyFormat(requestBodyFormatRequestParam, requestBodyFormatRequestHeader);
        IRestApiConverter requestResponsePairConverter = requestResponsePairConverterFactory
                .create(requestBodyFormat);
        RestApi[] requestResponsePairs = requestResponsePairConverter
                .toRequestResponsePairs(requestBody);
        requestResponsePairStore.add(requestResponsePairs);
    }

    private String getRequestBodyFormat(String requestBodyTypeRequestParam, String requestBodyTypeRequestHeader) {
        if (requestBodyTypeRequestParam != null) {
            return requestBodyTypeRequestParam;
        }
        if (requestBodyTypeRequestHeader != null) {
            return requestBodyTypeRequestHeader;
        }
        return "DEFAULT";
    }

    @DeleteMapping("/__admin/rest-apis")
    public void removeDummyResponseList() {
        this.requestResponsePairStore.clear();
    }

    @RequestMapping("/api/**")
    public ResponseEntity<?> getDummyResponse(HttpServletRequest request) {
        List<RestApi> requestResponsePairList = requestResponsePairStore
                .getByHttpRequest(request);
        if (requestResponsePairList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        requestResponsePairList.sort(Comparator.comparing(RestApi::getPriority));
        RestApiResponse response = requestResponsePairList.get(FIRST_RESPONSE).getResponse();
        return responseConverter.toResponseEntity(response, request);
    }
}
