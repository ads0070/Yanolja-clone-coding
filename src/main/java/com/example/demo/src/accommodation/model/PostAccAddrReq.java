package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostAccAddrReq {
    private int accommodation_id;
    private String sido;
    private String sigungu;
    private String eubmyeondong;
    private String ri;
    private String beonji;
    private String detail;
    private LocalDateTime create_at;
}
