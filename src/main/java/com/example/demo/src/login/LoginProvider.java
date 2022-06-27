package com.example.demo.src.login;

import com.example.demo.config.BaseException;
import com.example.demo.src.login.model.KakaoToken;
import com.example.demo.src.login.model.PostLoginReq;
import com.example.demo.src.login.model.PostLoginRes;
import com.example.demo.src.login.model.User;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class LoginProvider {

    private final LoginDao loginDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LoginProvider(LoginDao loginDao, JwtService jwtService) {
        this.loginDao = loginDao;
        this.jwtService = jwtService;
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        User user = loginDao.getPwd(postLoginReq);
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userIdx = user.getId();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public int checkEmail(String email) throws BaseException {
        try {
            return loginDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public KakaoToken getKakaoToken(int userId) throws BaseException {
        try {
            return loginDao.getKakaoToken(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes getUserInfo(String email) throws BaseException {
        try {
            int userId = loginDao.getUserInfo(email);
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId, jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
