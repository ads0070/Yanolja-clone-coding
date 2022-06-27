package com.example.demo.src.signup;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.signup.model.PostUserReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/signup")
public class SignUpController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SignUpProvider signUpProvider;
    @Autowired
    private final SignUpService signUpService;
    @Autowired
    private final JwtService jwtService;

    public SignUpController(SignUpProvider signUpProvider, SignUpService signUpService, JwtService jwtService) {
        this.signUpProvider = signUpProvider;
        this.signUpService = signUpService;
        this.jwtService = jwtService;
    }

    /**
     * 사용자 회원가입 API
     * [POST] /app/signup
     * @return BaseResponse<Integer>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<Integer> createUser(@RequestBody PostUserReq postUserReq) {
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        if(!isRegexPhone(postUserReq.getPhone_number())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }

        if(!isRegexPassword(postUserReq.getPassword())) {
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }

        if(!isRegexNickname(postUserReq.getNickname())) {
            return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
        }

        try {
            int userId = signUpService.createUser(postUserReq);
            return new BaseResponse<>(userId);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
