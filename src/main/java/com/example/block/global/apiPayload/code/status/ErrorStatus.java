package com.example.block.global.apiPayload.code.status;

import com.example.block.global.apiPayload.code.BaseErrorCode;
import com.example.block.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
//  LIKE 관련 오류
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "EMAIL400", "Email 정보가 없습니다."),
    USERID_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER400", "USER 정보가 없습니다."),
    LIKE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER400", "이미 좋아요를 하였습니다!"),
    LIKE_DOESNT_EXIST(HttpStatus.BAD_REQUEST, "USER400", "좋아요가 존재하지않습니다!"),

    //결제 관련 오류
    _PAY_CANCEL(HttpStatus.BAD_REQUEST,"PAY400","결제가 취소되었습니다."),
    _PAY_FAIL(HttpStatus.BAD_REQUEST,"PAY400","결제에 실패하였습니다."),
    _KAKAO_PAY_READY_FAIL(HttpStatus.BAD_REQUEST,"PAY400","카카오페이 준비에 실패하였습니다."),
    _ALREADY_PAID(HttpStatus.BAD_REQUEST,"PAY400","이미 결제한 리뷰입니다."),
    _NEED_PAY(HttpStatus.BAD_REQUEST,"PAY400","결제가 필요합니다."),
    //리뷰 관련
    _REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,"REVIEW404","리뷰가 존재하지 않습니다."),
    //유저 관련
    _USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER404","유저가 존재하지 않습니다."),

    CHALLENGER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "APPLY400", "이미 지원한 공모전입니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


//    두개가 필요한 이유가 뭘까?

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}