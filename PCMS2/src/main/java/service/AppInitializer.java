//package service;
//
//import javax.servlet.ServletRegistration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
// 
//public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer  {
//    @Override
//    public void customizeRegistration(ServletRegistration.Dynamic registration) {
//        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
//    }
//
//	@Override
//	protected Class<?>[] getRootConfigClasses() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected Class<?>[] getServletConfigClasses() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected String[] getServletMappings() {
//		// TODO Auto-generated method stub
//		return null;
//	} 
//}