package com.bx.community.service;

import com.bx.community.dto.NotificationDTO;
import com.bx.community.dto.PaginationDTO;
import com.bx.community.eums.NotificationStatusEnum;
import com.bx.community.eums.NotificationTypeEnum;
import com.bx.community.exception.CustomizeErrorCode;
import com.bx.community.exception.CustomizeException;
import com.bx.community.mapper.NotificationMapper;
import com.bx.community.model.Notification;
import com.bx.community.model.NotificationExample;
import com.bx.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper mapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) mapper.countByExample(notificationExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page, size);

        //size*(page-1)
        Integer offset = size * (page - 1);
        List<Notification> notifications = mapper.listLimit(userId, offset, size);
        if (notifications.size() == 0) {
            return paginationDTO;
        }

        List<NotificationDTO> dtos = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO dto = new NotificationDTO();
            BeanUtils.copyProperties(notification, dto);
            dto.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            dtos.add(dto);
        }

        paginationDTO.setData(dtos);
        return paginationDTO;
    }

    public long queryUnReadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.or().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return mapper.countByExample(example);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = mapper.selectByPrimaryKey(id);
        // 校验
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        // 若未读，置为已读
        if(notification.getStatus()==NotificationStatusEnum.UNREAD.getStatus()) {
            notification.setStatus(NotificationStatusEnum.READ.getStatus());
            mapper.updateByPrimaryKeySelective(notification);
        }

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
