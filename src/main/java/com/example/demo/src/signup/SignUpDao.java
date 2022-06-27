package com.example.demo.src.signup;

import com.example.demo.src.signup.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SignUpDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into user(email, password, phone_number, nickname, create_at) values(?, ?, ?, ?, ?)";
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassword(),
                postUserReq.getPhone_number(), postUserReq.getNickname(), postUserReq.getCreate_at()};
        return this.jdbcTemplate.update(createUserQuery, createUserParams);
    }

    public int getLastInsertId() {
        String getLastInsertIdQuery = "SELECT LAST_INSERT_ID()";
        return this.jdbcTemplate.queryForObject(getLastInsertIdQuery, Integer.class);
    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select id from user where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public int checkPhone(String phone) {
        String checkPhoneQuery = "select exists(select id from user where phone_number = ?)";
        String checkPhoneParams = phone;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }

    public int checkNickname(String nickname) {
        String checkNickameQuery = "select exists(select id from user where nickname = ?)";
        String checkNickameParams = nickname;
        return this.jdbcTemplate.queryForObject(checkNickameQuery,
                int.class,
                checkNickameParams);
    }

}
