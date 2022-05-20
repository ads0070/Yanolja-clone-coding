package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /app/signup
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_INVALID_PHONE(false, 2018, "전화번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONE(false,2019,"중복된 전화번호입니다."),
    POST_USERS_INVALID_PASSWORD(false, 2020, "비밀번호 형식을 확인해주세요."),
    POST_USERS_INVALID_NICKNAME(false,2021,"닉네임 형식을 확인해주세요."),
    POST_USERS_EXISTS_NICKNAME(false,2022,"중복된 닉네임입니다."),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    // [POST] /users/wish
    DUPLICATED_WISH(false, 3015, "이미 찜 목록에 추가되어 있습니다."),

    // [PATCH] /users/password/{userId}
    PWD_NOT_MATCHED(false, 3020, "비밀번호가 일치하지 않습니다."),

    // [DELETE] /app/users/reservations/{userId}/{reserveId}
    FAILED_TO_CANCEL_BOOK(false, 3021, "취소 불가능한 예약입니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    //[PATCH] /users/{userId}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    // [PATCH] /users/password/{userId}
    MODIFY_FAIL_PASSWORD(false, 4015, "유저 비밀번호 변경 실패"),

    // [POST] /app/users/wish
    INSERT_FAIL_WISH(false, 4016, "찜 추가 실패"),
    // [DELETE] /users/wish/{wishId}
    DELETE_FAIL_WISH(false, 4017, "찜 취소 실패"),
    // [DELETE] /users/reviews/{userId}/{reviewId}
    DELETE_FAIL_REVIEW(false, 4018, "후기 삭제 실패"),
    DELETE_FAIL_ANSWER(false, 4019, "사장님 답변 삭제 실패"),
    // [DELETE] /users/reviews/{userId}/{basketId}
    DELETE_FAIL_BASKET(false, 4020, "장바구니 삭제 실패"),
    // [POST] /app/users/baskets
    INSERT_FAIL_BASKET(false, 4021, "장바구니 추가 실패"),
    // [POST] /app/users/reservations
    INSERT_FAIL_RESERVATION(false, 4022, "숙박 예약 실패"),
    // [POST] /app/accommodations/{ownerId}
    INSERT_FAIL_ACC(false, 4023, "숙소 추가 실패"),
    INSERT_FAIL_ADDR(false, 4024, "숙소 주소 추가 실패"),
    INSERT_FAIL_ACC_MATCH(false, 4025, "점주 + 숙소 매칭 실패"),
    // [PATCH] /app/accommodations/accInfo/{ownerId}
    MODIFY_FAIL_ACC_INFO(false, 4026, "숙소 정보 수정 실패"),
    // [PATCH] /app/accommodations/accAddr/{ownerId}
    MODIFY_FAIL_ACC_ADDR(false, 4027, "숙소 주소 수정 실패"),
    // [POST] /app/accommodations/room/{ownerId}
    INSERT_FAIL_ROOM(false, 4028, "객실 추가 실패"),
    // [PATCH] /app/accommodations/room/{ownerId}
    MODIFY_FAIL_ROOM(false, 4029, "객실 정보 수정 실패"),
    // [DELETE] /app/users/reservations/{userId}/{reserveId}
    DELETE_FAIL_RESERVATION(false, 4030, "예약 취소 실패"),

    INSERT_FAIL_USER(false, 4031, "회원가입 실패"),

    UPDATE_FAIL_TOKEN(false, 4032, "액세스 토큰 갱신 실패");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
