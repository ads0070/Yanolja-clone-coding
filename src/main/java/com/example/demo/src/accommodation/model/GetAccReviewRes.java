package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetAccReviewRes {
    private int review_id;
    private String nickname;
    private String room_name;
    private String content;
    private int kindness_rating;
    private int cleanliness_rating;
    private int conveniencerrating;
    private int equipment_satisfaction_rating;
    private Timestamp create_at;
    private String storage_name;
    private int answer_id;
    private String answer_context;
    private Timestamp answer_create_at;
}
