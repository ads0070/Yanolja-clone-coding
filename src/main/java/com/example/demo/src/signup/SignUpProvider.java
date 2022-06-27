package com.example.demo.src.signup;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SignUpProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SignUpDao signUpDao;
    private final JwtService jwtService;

    @Autowired
    public SignUpProvider(SignUpDao signUpDao, JwtService jwtService) {
        this.signUpDao = signUpDao;
        this.jwtService = jwtService;
    }

    public int getLastInsertId() throws BaseException {
        try {
            return signUpDao.getLastInsertId();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException {
        try {
            return signUpDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPhone(String phone) throws BaseException {
        try {
            return signUpDao.checkPhone(phone);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkNickname(String nickname) throws BaseException {
        try {
            return signUpDao.checkNickname(nickname);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
