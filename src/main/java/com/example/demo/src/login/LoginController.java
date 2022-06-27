package com.example.demo.src.login;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.login.model.PostLoginReq;
import com.example.demo.src.login.model.PostLoginRes;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.config.BaseResponseStatus.POST_USERS_INVALID_NICKNAME;
import static com.example.demo.utils.ValidationRegex.isRegexNickname;

@RestController
@RequestMapping("/app/login")
public class LoginController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LoginProvider loginProvider;
    @Autowired
    private final LoginService loginService;
    @Autowired
    private final JwtService jwtService;

    public LoginController(LoginProvider loginProvider, LoginService loginService, JwtService jwtService){
        this.loginProvider = loginProvider;
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    /**
     * 로그인 API
     * [POST] /app/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = loginProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 카카오 로그인 API
     * [GET] /app/login/kakao
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<PostLoginRes> kakaoLogin(@RequestParam(required = false) String code) {
        System.out.println("code : " + code);
        try {
            // URL에 포함된 code를 이용하여 액세스 토큰 발급
            String accessToken = loginService.getKakaoAccessToken(code);
            System.out.println(accessToken);

            // 액세스 토큰을 이용하여 카카오 서버에서 유저 정보(닉네임, 이메일) 받아오기
            HashMap<String, Object> userInfo = loginService.getUserInfo(accessToken);
            System.out.println("login Controller : " + userInfo);

            PostLoginRes postLoginRes = null;

            // 만일, DB에 해당 email을 가지는 유저가 없으면 회원가입 시키고 유저 식별자와 JWT 반환
            // 현재 카카오 유저의 전화번호를 받아올 권한이 없어서 테스트를 하지 못함.
            if(loginProvider.checkEmail(String.valueOf(userInfo.get("email"))) == 0) {
                //PostLoginRes postLoginRes = 해당 서비스;
                return new BaseResponse<>(postLoginRes);
            } else {
                // 아니면 기존 유저의 로그인으로 판단하고 유저 식별자와 JWT 반환
                postLoginRes = loginProvider.getUserInfo(String.valueOf(userInfo.get("email")));
                return new BaseResponse<>(postLoginRes);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 카카오 토큰 갱신 API
     * [GET] /app/login/kakao/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/kakao/{userId}")
    public BaseResponse<String> updateKakaoToken(@PathVariable int userId) {
        String result = "";

        try {
            //jwt에서 id 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            loginService.updateKakaoToken(userId);

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
