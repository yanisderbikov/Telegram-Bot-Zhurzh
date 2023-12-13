package com.zhurzh.commonutils.exception;

public class CommandException extends Exception{
    public CommandException(String message, Throwable cause) {

        super(message, cause);
    }
    public CommandException(String message){
        super(message);
    }
    public CommandException(String message, StackTraceElement[] stackTrace){
        super(message);
        setStackTrace(stackTrace);
    }
    public CommandException(StackTraceElement[] stackTrace){
        super("No one command was excuted");
        setStackTrace(stackTrace);
    }
}
