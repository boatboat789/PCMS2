package th.co.wacoal.atech.pcms2.config;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import th.co.wacoal.atech.pcms2.info.SqlAtechERPInfo;
import th.co.wacoal.atech.pcms2.info.SqlPCMSInfo;
import th.co.wacoal.atech.pcms2.info.SqlPPMMInfo;
import th.co.wacoal.atech.pcms2.info.SqlSORInfo;
import th.in.totemplate.core.sql.Database;

@Configuration
public class DatabaseConfig {  
    @Bean 
    @Qualifier("ppmmDatabase")
    public Database ppmmDatabase()  throws ClassNotFoundException, SQLException {
        return new Database(SqlPPMMInfo.getInstance()); // Return the Test SQL configuration
    }
    @Bean
    @Primary                  // ← เพิ่มบรรทัดนี้
    @Qualifier("pcmsDatabase")
    public Database pcmsDatabase() throws ClassNotFoundException, SQLException {
        return new Database(SqlPCMSInfo.getInstance()); // Return the Test SQL configuration
    }  
    @Bean 
    @Qualifier("sorDatabase")
    public Database sorDatabase()  throws ClassNotFoundException, SQLException {
        return new Database(SqlSORInfo.getInstance()); // Return the Test SQL configuration
    }
    @Bean 
    @Qualifier("erpDatabase")
    public Database atechERPDatabase()  throws ClassNotFoundException, SQLException {
        return new Database(SqlAtechERPInfo.getInstance()); // Return the Test SQL configuration
    }
}
