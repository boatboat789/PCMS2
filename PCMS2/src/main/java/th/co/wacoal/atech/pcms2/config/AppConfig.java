package th.co.wacoal.atech.pcms2.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class AppConfig {
	// Use this method because it add jar file from another project in not under classpath
	// Normal case just @Bean , @Component , @Service ,@Respository Work
//	@Bean
//	public BGJobApiService bGJobApiService()
//	{
//		return new BGJobApiService();
//	}
//// 
//	@Bean
//	public MultipartResolver multipartResolver() {
//	    return new StandardServletMultipartResolver();
//	}
}
