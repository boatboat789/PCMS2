package th.co.wacoal.atech.pcms2.config;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.in.totemplate.core.sql.Database;

@Configuration
public class DatabaseConfig { 
//	 @Bean
//	    public BGJobApiService bgJobApiService() {
//	        return new BGJobApiService();
//	    }
//    @Bean
//    @Qualifier("orgatexDatabase")
//    public Database orgatexDatabase() throws ClassNotFoundException, SQLException  {
//        return new Database(OrgatexSqlPCMSInfo.getInstance()); // Return the Orgatex SQL configuration
//    }
//
//    @Bean
//    @Qualifier("ppmmDatabase")
//    public Database ppmmDatabase()  throws ClassNotFoundException, SQLException {
//        return new Database(SqlPPMMInfo.getInstance()); // Return the Test SQL configuration
//    }
//
//    @Bean
//    @Qualifier("inspectDatabase")
//    public Database inspectDatabase() throws ClassNotFoundException, SQLException  {
//        return new Database(SqlInspectSystemInfo.getInstance()); // Return the Test SQL configuration
//    }

    @Bean
    @Qualifier("pcmsDatabase")
    public Database pcmsDatabase() throws ClassNotFoundException, SQLException {
        return new Database(SqlPCMSInfo.getInstance()); // Return the Test SQL configuration
    }  
}
