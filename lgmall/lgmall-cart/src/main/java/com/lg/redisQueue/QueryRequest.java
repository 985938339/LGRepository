package com.lg.redisQueue;

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
        try {
            if (queryFunction != null) {
                result = queryFunction.queryExecution(key);
            }
        } catch (Exception e) {
            this.requestThrowable =e;
        } finally {
            synchronized (this) {
                isDone=true;
                this.notifyAll();
            }
        }
    }


}
