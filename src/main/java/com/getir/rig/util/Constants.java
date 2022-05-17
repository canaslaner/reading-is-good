package com.getir.rig.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    @UtilityClass
    public final class ExceptionHandler {
        public static final String LOCALIZED_MESSAGE_KEY = "localizedMessage";
        public static final String MESSAGE_KEY = "message";
        public static final String PATH_KEY = "path";
        public static final String VIOLATIONS_KEY = "violations";

        public static final String VIOLATIONS_MESSAGE_KEY = "exception.constraintViolation";
        public static final String INTERNAL_SERVER_ERROR_MESSAGE_KEY = "exception.internalServer";
        public static final String INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE = "An error occurred.";
    }

    @UtilityClass
    public final class Security {
        public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
        public static final String X_FORWARDED_FOR_HEADER_NAME = "X-Forwarded-For";
        
        public static final String BEARER_TOKEN_PREFIX = "Bearer ";
        public static final int BEARER_TOKEN_PREFIX_LENGTH = BEARER_TOKEN_PREFIX.length();
        public static final String JWT_CLAIM_AUTHORITY_DELIMITER = ",";
        public static final String JWT_CLAIM_AUTHORITIES_KEY = "auth";

        public static final String ANONYMOUS_USER = "anonymousUser";
        public static final String LOCALHOST_IP_V6 = "0:0:0:0:0:0:0:1";
        public static final String LOCALHOST_IP = "127.0.0.1";
    }
}
