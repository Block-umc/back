package com.example.block.global.apiPayload.exception.handler;

import com.example.block.global.apiPayload.code.BaseErrorCode;
import com.example.block.global.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
