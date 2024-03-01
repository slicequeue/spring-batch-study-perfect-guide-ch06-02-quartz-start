package com.slicequeue.springboot.batch.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 쿼츠 스케쥴을 구성
 * -
 */
@Configuration
public class QuartzConfiguration {

    /**
     * 쿼츠가 해당 잡 정의를 삭제하지 않도록 JobDetail 을 생성
     * - 트리거에 의해 쿼츠에게 잡 실행시 잡의 개별 실행을 정의하는 jobDetails 객체가 생성됨
     * @return JobDetail
     */
    @Bean
    public JobDetail quartzJobDetail() {
        // 쿼츠잡 정의한 BatchScheduledJob 클래스를 JobBuilder 통해 전달
        return JobBuilder.newJob(BatchScheduledJob.class)
                .storeDurably()
                .build();
        // 매게변수가 필요한 경우라면 setJobData 로 지정하는 것 같다.
    }

    /**
     * 트리거를 만들어 스케쥴링 진행
     * @return 5초에 한번씩 4번 동작하는 트리거
     */
    @Bean
    public Trigger jobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5).withRepeatCount(4);

        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule(scheduleBuilder)
                .build();
    }

}
