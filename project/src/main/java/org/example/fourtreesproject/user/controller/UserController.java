package org.example.fourtreesproject.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.delivery.model.request.DeliveryAddressRegisterRequest;
import org.example.fourtreesproject.emailVerify.model.dto.EmailVerifyDto;
import org.example.fourtreesproject.emailVerify.service.EmailVerifyService;
import org.example.fourtreesproject.user.exception.custom.InvalidUserException;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.example.fourtreesproject.user.model.request.SellerSignupRequest;
import org.example.fourtreesproject.user.model.request.UserSignupRequest;
import org.example.fourtreesproject.user.model.response.SellerInfoResponse;
import org.example.fourtreesproject.user.model.response.UserInfoResponse;
import org.example.fourtreesproject.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.example.fourtreesproject.common.BaseResponseStatus.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailVerifyService emailVerifyService;

    @PostMapping("/user/basic/signup")
    public BaseResponse<String> signup(@RequestBody UserSignupRequest userSignupRequest) throws Exception{
        String uuid = userService.sendEmail(userSignupRequest.getEmail());
        userService.signup(userSignupRequest);
        emailVerifyService.save(EmailVerifyDto.builder()
                .email(userSignupRequest.getEmail())
                .uuid(uuid)
                .build());
        return new BaseResponse<>("");
    }

    @PostMapping("/seller/signup")
    public BaseResponse<String> sellerSignup(@RequestBody SellerSignupRequest sellerSignupRequest) throws Exception{
        String uuid = userService.sendEmail(sellerSignupRequest.getEmail());
        userService.sellerSignup(sellerSignupRequest);
        emailVerifyService.save(EmailVerifyDto.builder()
                .email(sellerSignupRequest.getEmail())
                .uuid(uuid)
                .build());
        return new BaseResponse<>();
    }

    @GetMapping("/user/verify")
    public BaseResponse<String> verify(String email, String uuid) throws Exception{
        Boolean verify = emailVerifyService.verifyEmail(EmailVerifyDto.builder()
                .email(email)
                .uuid(uuid)
                .build());
        if (verify){
            userService.activeMember(email);
            return new BaseResponse<>("");
        }
        return new BaseResponse<>(USER_EMAIL_AUTH_FAIL);
    }

    @PostMapping("/user/delivery/register")
    public BaseResponse<String> deliveryRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @RequestBody DeliveryAddressRegisterRequest deliveryAddressRegisterRequest) throws Exception{
        if (customUserDetails == null){
            return new BaseResponse<>(USER_NOT_LOGIN);
        }
        userService.registerDelivery(customUserDetails.getUser(), deliveryAddressRegisterRequest);
        return new BaseResponse<>();
    }

    @GetMapping("/user/info/detail")
    public BaseResponse<UserInfoResponse> userInfoRead(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception{
        if (customUserDetails == null){
            throw new InvalidUserException(USER_NOT_LOGIN);
        }
        UserInfoResponse userInfoResponse = userService.getUserInfoDetail(customUserDetails.getIdx());
        return new BaseResponse<>(userInfoResponse);
    }

    @GetMapping("/seller/info/detail")
    public BaseResponse<SellerInfoResponse> sellerInfoRead(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception{
        if (customUserDetails == null){
            throw new InvalidUserException(USER_NOT_LOGIN);
        }
        SellerInfoResponse sellerInfoResponse = userService.getSellerInfoDetail(customUserDetails.getIdx());
        return new BaseResponse<>(sellerInfoResponse);
    }
}
