package processor;

public interface FunctionThrowable<T, R> {

    R apply(T t) throws Exception;
}
