package com.getir.testutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MvcUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String createJsonContent(final Object request) throws JsonProcessingException {
        return MAPPER.writeValueAsString(request);
    }
}
