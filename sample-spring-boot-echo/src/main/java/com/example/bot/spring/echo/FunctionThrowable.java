package com.example.bot.spring.echo;

public interface FunctionThrowable<T, R> {

    R apply(T t) throws Exception;
}
