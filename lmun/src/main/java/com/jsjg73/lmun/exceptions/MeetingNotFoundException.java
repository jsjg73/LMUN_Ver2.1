package com.jsjg73.lmun.exceptions;

public class MeetingNotFoundException extends RuntimeException{
    public MeetingNotFoundException(String msg){
        super(msg);
    }
    public MeetingNotFoundException(){

    }
}
