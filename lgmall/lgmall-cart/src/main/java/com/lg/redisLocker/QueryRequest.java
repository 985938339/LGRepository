package com.lg.redisLocker;


/**
 * @author liug132055
 */
public class QueryRequest<T> extends Request<T> {

    private QueryFunction<T> queryFunction;

    public QueryRequest(String key, QueryFunction<T> queryFunction) {
        this.key = key;
        this.queryFunction = queryFunction;
    }

    @Override
    public void execute() {
        if (queryFunction != null) {
            result = queryFunction.queryExecution(key);
        }
    }
}
