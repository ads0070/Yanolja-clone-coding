package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
        try{
            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPwd(PatchPwdReq patchPwdReq) throws BaseException{
        try{
            return userDao.checkPwd(patchPwdReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserCouponRes> getUserCoupon(int userId) throws BaseException {
        try {
            List<GetUserCouponRes> getUserCouponRes = userDao.getUserCoupon(userId);
            return getUserCouponRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkWish(PostWishReq postWishReq) throws BaseException{
        try{
            return userDao.checkWish(postWishReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetWishRes> getWishList(int userId) throws BaseException {
        try {
            List<GetWishRes> getWishRes = userDao.getWishList(userId);
            return getWishRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPointRes> getPoint(int userId) throws BaseException{
        try{
            return userDao.getPoint(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkAnswer(int reviewId) throws BaseException{
        try{
            return userDao.checkAnswer(reviewId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetBasketRes> getBaskets(int userId) throws BaseException{
        try{
            return userDao.getBaskets(userId);
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetReserveRes> getReserves(int userId, int term) throws BaseException {
        try{
            return userDao.getReserves(userId, term);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSearchRes> getSearch(int userId) throws BaseException {
        try{
            return userDao.getSearch(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkReserve(int reserveId) throws BaseException{
        try{
            return userDao.checkReserve(reserveId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkNickname(String nickname) throws BaseException {
        try {
            return userDao.checkNickname(nickname);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
