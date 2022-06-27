package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PatchAccAddrReq {
    private int id;
    private String sido;
    private String sigungu;
    private String eubmyeondong;
    private String ri;
    private String beonji;
    private String detail;
    private LocalDateTime update_at;
}
