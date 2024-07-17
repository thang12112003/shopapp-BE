package com.project.shopapp.exceptions;

public class DataNotFoundException extends Exception{//kiểm tra Exception để phân loại
    public DataNotFoundException(String message) {
        super(message);
    }
}
