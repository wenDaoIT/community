package top.tom666.community.config;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import top.tom666.community.quartz.AlphaJob;
import top.tom666.community.quartz.PostScoreRefreshJob;

/**
 * @author: liujisen
 * @date： 2022-09-26
 */
@Configuration
public class QuartzConfig {

    /**
     * @return 配置jobdetail
     */
//    @Bean
    public JobDetailFactoryBean alphaJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphajob");
        factoryBean.setGroup("alphaJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }


//    @Bean
    public   SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail){
        final SimpleTriggerFactoryBean triggerFactoryBean = new SimpleTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(alphaJobDetail);
        triggerFactoryBean.setName("alphaTrigger");
        triggerFactoryBean.setGroup("alpahaTiggerGroup");
        triggerFactoryBean.setRepeatInterval(3000);
        triggerFactoryBean.setJobDataMap(new JobDataMap());
        return triggerFactoryBean;
    }

        @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("PostScoreRefreshJob");
        factoryBean.setGroup("CommunityJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

        @Bean
    public   SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail){
        final SimpleTriggerFactoryBean triggerFactoryBean = new SimpleTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(postScoreRefreshJobDetail);
        triggerFactoryBean.setName("postScoreRefreshTrigger");
        triggerFactoryBean.setGroup("CommunityTiggerGroup");
        triggerFactoryBean.setRepeatInterval(1000 * 60 * 5);
        triggerFactoryBean.setJobDataMap(new JobDataMap());
        return triggerFactoryBean;
    }

}
