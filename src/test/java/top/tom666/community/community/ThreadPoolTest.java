package top.tom666.community.community;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.tom666.community.CommunityApplication;

import javax.annotation.Resource;

/**
 * @author: liujisen
 * @date： 2022-09-26
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTest {
    @Resource
    private Scheduler scheduler;
    @Test
    public void testDeleteJob(){
        try {
            boolean result =scheduler.deleteJob(new JobKey("alphaJob","alphaJobGroup"));
            System.out.println(result);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
