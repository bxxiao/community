package com.bx.community.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentExtMapper {
    int incCommentCount(@Param("addCount") int addCount, @Param("id") long id);
}