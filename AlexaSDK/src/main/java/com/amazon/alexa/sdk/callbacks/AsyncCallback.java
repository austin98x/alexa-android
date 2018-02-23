package com.amazon.alexa.sdk.callbacks;

/**
 * A generic callback to handle four states of asynchronous operations
 */
public interface AsyncCallback<D, E>{
    void start();
    void success(D result);
    void failure(E error);
    void complete();
}
