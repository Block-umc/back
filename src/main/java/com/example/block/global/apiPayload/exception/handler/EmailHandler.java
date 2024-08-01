package com.example.block.global.apiPayload.exception.handler;

import com.example.block.global.apiPayload.code.BaseErrorCode;
import com.example.block.global.apiPayload.exception.GeneralException;

public class EmailHandler extends GeneralException {
    public EmailHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
