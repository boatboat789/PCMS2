package th.co.wacoal.atech.pcms2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Bean(destroyMethod = "shutdown")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // จำกัด
        scheduler.setThreadNamePrefix("schedule-pcms2");
        scheduler.setDaemon(true); // daemon thread ไม่บัง classloader GC เมื่อ undeploy
        scheduler.setWaitForTasksToCompleteOnShutdown(false);
        scheduler.setAwaitTerminationSeconds(5);
        // Spring calls afterPropertiesSet() → initialize() automatically — do NOT call here (causes double thread pool)
        return scheduler;
    }
}
