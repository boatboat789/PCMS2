//package config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.jmx.export.annotation.ManagedResource;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//@Configuration
//@ManagedResource
//public class ExecutorConfig {
//
//    @Bean(name = "pcms2BGJob")
//    public TaskExecutor ExecutorConfig() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(25);
//        executor.setThreadNamePrefix("pcms2BGJob");
//        executor.initialize();
//        return executor;
//    }
//} 
