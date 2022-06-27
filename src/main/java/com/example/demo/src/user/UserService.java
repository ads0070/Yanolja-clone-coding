package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        if(userProvider.checkNickname(patchUserReq.getNickname()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        try{
            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserPwd(PatchPwdReq patchPwdReq) throws BaseException {
        String originPwd, newPwd;
        try{
            originPwd = new SHA256().encrypt(patchPwdReq.getOriginPwd());
            newPwd = new SHA256().encrypt(patchPwdReq.getNewPwd());
            patchPwdReq.setOriginPwd(originPwd);
            patchPwdReq.setNewPwd(newPwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        if(userProvider.checkPwd(patchPwdReq) == 0){
            throw new BaseException(PWD_NOT_MATCHED);
        }

        try{
            int result = userDao.modifyUserPwd(patchPwdReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PASSWORD);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createWish(PostWishReq postWishReq) throws BaseException {
        LocalDateTime localDateTime = LocalDateTime.now();
        postWishReq.setCreate_at(localDateTime);

        if(userProvider.checkWish(postWishReq) == 1){
            throw new BaseException(DUPLICATED_WISH);
        }

        try{
            int result = userDao.createWish(postWishReq);
            if(result == 0){
                throw new BaseException(INSERT_FAIL_WISH);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteWish(int wishId) throws BaseException {
        try{
            int result = userDao.deleteWish(wishId);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_WISH);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteReview(int reviewId) throws BaseException {
        try{
            int result = userDao.deleteReview(reviewId);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_REVIEW);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteAnswer(int answerId) throws BaseException {
        try{
            int result = userDao.deleteAnswer(answerId);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_ANSWER);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteBasket(int basketId) throws BaseException {
        try {
            int result = userDao.deleteBasket(basketId);
            if(result == 0) {
                throw new BaseException(DELETE_FAIL_BASKET);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void postReserve(PostReserveReq postReserveReq) throws BaseException {
        try {
            int result = userDao.createReserve(postReserveReq);
            if(result == 0) {
                throw new BaseException(INSERT_FAIL_RESERVATION);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void postBasket(PostBasketReq postBasketReq) throws BaseException {
        try {
            int result = userDao.createBasket(postBasketReq);
            if(result == 0) {
                throw new BaseException(INSERT_FAIL_BASKET);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteReserve(int reserveId) throws BaseException {
        if(userProvider.checkReserve(reserveId) == 0){
            throw new BaseException(FAILED_TO_CANCEL_BOOK);
        }

        try{
            int result = userDao.deleteReserve(reserveId);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_RESERVATION);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
