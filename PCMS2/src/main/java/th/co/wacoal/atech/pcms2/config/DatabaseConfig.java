package th.co.wacoal.atech.pcms2.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:database.properties")
public class DatabaseConfig {

    @Value("${pcms.db.url}")      private String pcmsUrl;
    @Value("${pcms.db.username}") private String pcmsUser;
    @Value("${pcms.db.password}") private String pcmsPass;

    @Value("${ppmm.db.url}")      private String ppmmUrl;
    @Value("${ppmm.db.username}") private String ppmmUser;
    @Value("${ppmm.db.password}") private String ppmmPass;

    @Value("${sor.db.url}")       private String sorUrl;
    @Value("${sor.db.username}")  private String sorUser;
    @Value("${sor.db.password}")  private String sorPass;

    @Value("${erp.db.url}")       private String erpUrl;
    @Value("${erp.db.username}")  private String erpUser;
    @Value("${erp.db.password}")  private String erpPass;

    private HikariDataSource buildDataSource(String url, String user, String pass, String poolName) {
        HikariConfig cfg = new HikariConfig();
        cfg.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setPoolName(poolName);
        cfg.setMaximumPoolSize(10);
        cfg.setMinimumIdle(2);
        cfg.setConnectionTimeout(30000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(1800000);
        cfg.setKeepaliveTime(120000);
        cfg.setLeakDetectionThreshold(60000);
        return new HikariDataSource(cfg);
    }

    // --- PCMS (primary) ---
    @Bean(destroyMethod = "close")
    @Qualifier("pcmsDataSource")
    public DataSource pcmsDataSource() {
        return buildDataSource(pcmsUrl, pcmsUser, pcmsPass, "pcms-pool");
    }

    @Bean
    @Primary
    @Qualifier("pcmsDatabase")
    public JdbcTemplate pcmsJdbcTemplate(@Qualifier("pcmsDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    // --- PPMM ---
    @Bean(destroyMethod = "close")
    @Qualifier("ppmmDataSource")
    public DataSource ppmmDataSource() {
        return buildDataSource(ppmmUrl, ppmmUser, ppmmPass, "ppmm-pool");
    }

    @Bean
    @Qualifier("ppmmDatabase")
    public JdbcTemplate ppmmJdbcTemplate(@Qualifier("ppmmDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    // --- SOR ---
    @Bean(destroyMethod = "close")
    @Qualifier("sorDataSource")
    public DataSource sorDataSource() {
        return buildDataSource(sorUrl, sorUser, sorPass, "sor-pool");
    }

    @Bean
    @Qualifier("sorDatabase")
    public JdbcTemplate sorJdbcTemplate(@Qualifier("sorDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    // --- AtechERP ---
    @Bean(destroyMethod = "close")
    @Qualifier("erpDataSource")
    public DataSource erpDataSource() {
        return buildDataSource(erpUrl, erpUser, erpPass, "erp-pool");
    }

    @Bean
    @Qualifier("erpDatabase")
    public JdbcTemplate erpJdbcTemplate(@Qualifier("erpDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
