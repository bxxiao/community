package com.bx.community.service;

import com.bx.community.dto.CommentDTO;
import com.bx.community.eums.CommentTypeEnum;
import com.bx.community.exception.CustomizeErrorCode;
import com.bx.community.exception.CustomizeException;
import com.bx.community.mapper.*;
import com.bx.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        // type为空，或类型不存在
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            // 评论未找到
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            commentMapper.insertSelective(comment);
            // 增加回复的回复数
            commentExtMapper.incCommentCount(1, dbComment.getId());
        }else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            // 添加问题的回复数
            questionExtMapper.incCommentCount(1, question.getId());
        }

    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum typeEnum) {
        // 根据问题id和评论类型，查询出对应的评论
        CommentExample commentExample = new CommentExample();
        commentExample.or().andParentIdEqualTo(id)
                .andTypeEqualTo(typeEnum.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if(comments.size()==0){
            return new ArrayList<>();
        }

        // 获取去重的评论人的id set
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        // 将set转换为ArrayList，并查询出user
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        UserExample userExample = new UserExample();
        userExample.or().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);

        // 将 users 转换为 userId-user 的 map
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
