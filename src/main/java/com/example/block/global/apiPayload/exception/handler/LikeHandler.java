package com.example.block.global.apiPayload.exception.handler;

import com.example.block.global.apiPayload.code.BaseErrorCode;
import com.example.block.global.apiPayload.exception.GeneralException;

public class LikeHandler extends GeneralException {
    public LikeHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}


