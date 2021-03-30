package com.bx.community.schedule;


import com.bx.community.cache.HotTagCache;
import com.bx.community.mapper.QuestionMapper;
import com.bx.community.model.Question;
import com.bx.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    // 3 个小时更新一次
    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    // @Scheduled(fixedRate = 10000)
    public void hotTagSchedule() {
        log.info("hotTagSchedule start {}", new Date());
        List<Question> list;

        Map<String, Integer> priorities = new HashMap<>();

        list = questionMapper.selectByExample(new QuestionExample());
        for (Question question : list) {
            String[] tags = StringUtils.split(question.getTag(), ",");
            for (String tag : tags) {
                Integer priority = priorities.get(tag);
                if (priority != null) {
                    priorities.put(tag, priority + 5 + question.getCommentCount());
                } else {
                    priorities.put(tag, 5 + question.getCommentCount());
                }
            }
        }
        hotTagCache.updateTags(priorities);
        log.info("hotTagSchedule stop {}", new Date());
    }
}
