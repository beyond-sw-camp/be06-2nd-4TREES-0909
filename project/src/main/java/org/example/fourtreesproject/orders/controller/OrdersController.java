package org.example.fourtreesproject.orders.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.sun.jna.platform.unix.X11;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.orders.model.response.OrderPageResponse;
import org.example.fourtreesproject.orders.model.response.OrdersListResponse;
import org.example.fourtreesproject.orders.service.OrdersService;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @Operation(summary = "결제(공구 시작, 공구 참여) api")
    @GetMapping("/register")
    public BaseResponse<String> orderRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              String impUid) throws IamportResponseException, IOException, RuntimeException {

        ordersService.registerOrder(customUserDetails.getIdx(),impUid);
        return new BaseResponse<>();
    }

    @Operation(summary = "자신의 주문 정보 조회 api")
    @GetMapping("/info/list")
    public BaseResponse<List<OrdersListResponse>> readOrderInfoList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                   Integer page, Integer size) throws RuntimeException {
        List<OrdersListResponse> orderInfoList = ordersService.getOrderInfoList(page, size, customUserDetails.getIdx());
        return new BaseResponse<>(orderInfoList);
    }

    @Operation(summary = "주문 페이지 정보 조회 api")
    @GetMapping("/page")
    public BaseResponse<OrderPageResponse> getOrderPageInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws RuntimeException {
        OrderPageResponse orderPageResponse = ordersService.loadOrderPage(customUserDetails.getIdx());
        return new BaseResponse<>(orderPageResponse);
    }
}
