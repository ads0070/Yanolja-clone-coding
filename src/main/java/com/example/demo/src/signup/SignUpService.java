package com.example.demo.src.signup;

import com.example.demo.config.BaseException;
import com.example.demo.src.signup.model.PostUserReq;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class SignUpService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SignUpDao signUpDao;
    private final SignUpProvider signUpProvider;
    private final JwtService jwtService;

    @Autowired
    public SignUpService(SignUpDao signUpDao, SignUpProvider signUpProvider, JwtService jwtService) {
        this.signUpDao = signUpDao;
        this.signUpProvider = signUpProvider;
        this.jwtService = jwtService;
    }

    public int createUser(PostUserReq postUserReq) throws BaseException {
        int userId = -1;
        LocalDateTime localDateTime = LocalDateTime.now();
        postUserReq.setCreate_at(localDateTime);
        postUserReq.setPassword(new SHA256().encrypt(postUserReq.getPassword()));

        if(signUpProvider.checkEmail(postUserReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        if(signUpProvider.checkPhone(postUserReq.getPhone_number()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }

        if(signUpProvider.checkNickname(postUserReq.getNickname()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        try {
            int result = signUpDao.createUser(postUserReq);
            if(result == 0){
                throw new BaseException(INSERT_FAIL_USER);
            }
            userId = signUpProvider.getLastInsertId();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

        return userId;
    }
}
