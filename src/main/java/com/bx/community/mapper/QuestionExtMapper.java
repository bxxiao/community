package com.bx.community.mapper;

import com.bx.community.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionExtMapper {
    void incView(@Param("addCount") Integer addCount, @Param("id")Long id);

    void incCommentCount(@Param("addCount") Integer addCount, @Param("id")Long id);

    List<Question> selectRelated(Question question);
}
