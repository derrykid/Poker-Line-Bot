package org.derryclub.linebot.commands;

@FunctionalInterface
public interface FunctionThrowable<T, R> {
    R apply(T t) throws Exception;
}
