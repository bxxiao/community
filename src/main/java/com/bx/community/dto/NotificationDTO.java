package com.bx.community.dto;

import com.bx.community.model.User;
import lombok.Data;


@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    // private User notifier;
    private String outerTitle;
    private Integer type;

    private Long notifier;
    private String notifierName;
    private Long outerid;
    private String typeName;
}
