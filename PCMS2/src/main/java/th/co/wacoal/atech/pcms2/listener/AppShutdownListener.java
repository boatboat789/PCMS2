package th.co.wacoal.atech.pcms2.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppShutdownListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {}

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try { DriverManager.deregisterDriver(drivers.nextElement()); } catch (SQLException ignored) {}
        }
    }
}
