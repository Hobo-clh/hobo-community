package com.ccsu.community.schedule;

import com.ccsu.community.cache.HotTagCache;
import com.ccsu.community.mapper.QuestionMapper;
import com.ccsu.community.model.Question;
import com.ccsu.community.model.QuestionExample;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@Data
public class HotTagTask {

    @Autowired
    private HotTagCache hotTagCache;
    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 24小时定时
     */
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void hotTagSchedule() {
        int offset = 0;
        int limit = 10;
        log.info("HotTagSchedule is now {}", LocalDateTime.now());
        List<Question> questionList = new ArrayList<>();

        HashMap<String, Integer> prioritiesMap = new HashMap<>();
        //当offset=0时开始，之后就是判断list.size() 如果小于limit 就结束循环
        while (offset == 0 || questionList.size() == limit) {
            questionList = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, limit));
            for (Question question : questionList) {

                String[] tags = StringUtils.split(question.getTag(), ",");
                for (String tag : tags) {
                    Integer priority = prioritiesMap.get(tag);
                    if (priority != null) {
                        //热度算法：20*question_count+4*comment_count+view_count
                        prioritiesMap.put(tag, (int) (priority + 20 + 4 * question.getCommentCount() + question.getViewCount()));
                    } else {
                        prioritiesMap.put(tag, (int) (20 + 4 * question.getCommentCount() + question.getViewCount()));
                    }
                }
                log.info("questionList  question : {}", question.getId());
            }
            offset += limit;
        }
        hotTagCache.updateTags(prioritiesMap);
        prioritiesMap.forEach(
                (k, v) -> {
                    System.out.print(k + ":" + v);
                    System.out.println();
                }
        );
        log.info("HotTagSchedule is stop {}", LocalDateTime.now());
    }
}
