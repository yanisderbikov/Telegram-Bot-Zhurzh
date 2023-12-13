package com.zhurzh.commonutils.exception;

public class OrderException extends Exception{
    public OrderException(String message, Throwable cause) {

        super(message, cause);
    }
    public OrderException(Long id) {
        super(String.format("Order '%s' wasn't found", id));
    }
    public OrderException(String id) {
        super(String.format("Order '%s' wasn't found", id));
    }
//
//    public OrderException(String message) {
//        super(message);
//    }
//
//    public OrderException(String message, StackTraceElement[] stackTrace) {
//        super(message);
//        setStackTrace(stackTrace);
//    }
//
//    public OrderException(StackTraceElement[] stackTrace) {
//        super("No one command was excuted");
//        setStackTrace(stackTrace);
//    }
}
