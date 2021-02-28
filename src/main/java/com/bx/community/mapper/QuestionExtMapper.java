package com.bx.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuestionExtMapper {
    void incView(@Param("addCount") Integer addCount, @Param("id")Long id);

    void incCommentCount(@Param("addCount") Integer addCount, @Param("id")Long id);
}
