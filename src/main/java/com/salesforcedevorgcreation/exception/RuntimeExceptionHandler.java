package com.salesforcedevorgcreation.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class RuntimeExceptionHandler {

    @FunctionalInterface
    public interface FunctionWithException<T, R, E extends Throwable> {
        R apply(T t) throws E;
    }

    public static <T, R, E extends Throwable> Function<T, R> handle(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}
