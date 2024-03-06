package com.slicequeue.springboot.batch.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * BatchScheduledJob 클래스는 QuartzJobBean 를 상속하여
 * 쿼츠에서의 잡을 정의한다.
 */
public class BatchScheduledJob extends QuartzJobBean {

    @Autowired
    private Job batchJob; // 이 주입은 BasicBatchJobConfiguration 클래스 에서 batchJob 으로 빈등록이 되어 있기에 주입 가능한 것!

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobLauncher jobLauncher;

    /**
     * QuartzJobBean 의 상속 구현 executeInternal 메서드 오버라이딩을 통해
     * 위 주입받은 스프링배치의 batchJob을 실행하도록 정의한다.
     * - 이 메서드는 스케쥴링된 이벤트가 발생할 때마다 한번씩 호출한다.
     * @param context
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
                .getNextJobParameters(this.batchJob)
                .toJobParameters();

        try {
            // 잡 런처를 통해 실행함
            this.jobLauncher.run(this.batchJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
