package org.example.fourtreesproject.groupbuy.model.request;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyCreateRequest {

    private Long userIdx;
    private String category;
    private String title;
    private String content;
    private Integer gpbuyQuantity;


}