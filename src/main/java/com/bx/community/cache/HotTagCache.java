package com.bx.community.cache;

import com.bx.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Component
@Data
public class HotTagCache {
    private List<String> hots;

    public void updateTags(Map<String, Integer> tags) {
        int max = 10;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        // 借助小顶堆，将 map 中的标签根据热度进行排序
        tags.forEach((name, priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            // 没到最大值直接放
            if (priorityQueue.size() < max) {
                priorityQueue.add(hotTagDTO);
            } else {
                // 否则将当前 name-priority 跟小顶堆中的最小值比较
                HotTagDTO minHot = priorityQueue.peek();
                // 若热度比它大，替换之
                if (hotTagDTO.compareTo(minHot) > 0) {
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });


        // 将小顶堆中的标签取出来，按头插法放进 list ，即实现倒序
        List<String> sortedTags = new ArrayList<>();

        HotTagDTO poll = priorityQueue.poll();
        while (poll != null) {
            sortedTags.add(0, poll.getName());
            poll = priorityQueue.poll();
        }
        hots = sortedTags;
    }
}
