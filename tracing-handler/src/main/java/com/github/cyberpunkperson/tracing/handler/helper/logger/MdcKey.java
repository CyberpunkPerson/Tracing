package com.github.cyberpunkperson.tracing.handler.helper.logger;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class MdcKey {
    public static final String OPERATION_NAME = "operationName";
    public static final String INTEGRATION_ID = "integrationId";
}
