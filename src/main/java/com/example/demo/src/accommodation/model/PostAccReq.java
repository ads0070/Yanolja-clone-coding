package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAccReq {
    private String name;
    private String store_phone_number;
    private String main_category;
    private String sub_category;

    private String sido;
    private String sigungu;
    private String eubmyeondong;
    private String ri;
    private String beonji;
    private String detail;
}
