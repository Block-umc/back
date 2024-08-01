package com.example.block.global.apiPayload.exception.handler;

import com.example.block.global.apiPayload.code.BaseErrorCode;
import com.example.block.global.apiPayload.exception.GeneralException;

public class ApplicantHandler extends GeneralException {

    public ApplicantHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
