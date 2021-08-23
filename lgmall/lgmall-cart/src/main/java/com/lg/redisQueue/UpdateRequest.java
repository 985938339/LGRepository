package com.lg.redisQueue;

/**
 * @author liug132055
 */
public class UpdateRequest<T> extends Request<T> {
    private T value;
    private UpdateFunction<T> updateFunction;

    public UpdateRequest(String key, T value, UpdateFunction<T> updateFunction) {
        this.key = key;
        this.value = value;
        this.updateFunction = updateFunction;
    }

    @Override
    public void execute() {
        try {
            if (updateFunction!=null){
                updateFunction.updateExecution(key, value);
            }
        } catch (Exception e) {
            this.requestException =e;
        } finally {
            synchronized (this) {
                isDone=true;
                this.notify();
            }
        }
    }
}
