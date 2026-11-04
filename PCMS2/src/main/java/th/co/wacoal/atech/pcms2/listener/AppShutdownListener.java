package th.co.wacoal.atech.pcms2.listener;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        sweepThreadLocals();

        try { kong.unirest.Unirest.shutDown(); } catch (Exception ignored) {}

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                try { DriverManager.deregisterDriver(driver); } catch (SQLException ignored) {}
            }
        }
    }

    /**
     * กวาด ThreadLocal ที่ key/value class โหลดโดย webapp classloader ออกจากทุก thread —
     * แก้ classloader leak จาก mssql-jdbc ActivityCorrelator (private static ThreadLocal ภายใน driver
     * เอง ไม่มี public API ให้ล้าง) ที่ผูกกับ Tomcat's shared exec thread pool ยืนยันด้วย heap dump ใน
     * DyeingRecord 2026-07-17 — เพิ่มไว้ป้องกันแม้ยังไม่เจอปัญหาจริงในระบบนี้ (Java 8 ไม่ต้อง
     * --add-opens ต้นทุนแทบเป็นศูนย์)
     */
    private static void sweepThreadLocals() {
        ClassLoader webappCl = AppShutdownListener.class.getClassLoader();
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            try {
                sweepMap(t, "threadLocals", webappCl);
                sweepMap(t, "inheritableThreadLocals", webappCl);
            } catch (Throwable ignored) {}
        }
    }

    private static void sweepMap(Thread t, String fieldName, ClassLoader cl) throws Exception {
        Field mapField = Thread.class.getDeclaredField(fieldName);
        mapField.setAccessible(true);
        Object map = mapField.get(t);
        if (map == null) return;
        Field tableField = map.getClass().getDeclaredField("table");
        tableField.setAccessible(true);
        Object table = tableField.get(map);
        if (table == null) return;
        Method removeMethod = null;
        int len = Array.getLength(table);
        for (int i = 0; i < len; i++) {
            Object entry = Array.get(table, i);
            if (entry == null) continue;
            ThreadLocal<?> key = (ThreadLocal<?>) ((java.lang.ref.Reference<?>) entry).get();
            Field valueField = entry.getClass().getDeclaredField("value");
            valueField.setAccessible(true);
            Object value = valueField.get(entry);
            boolean ownedByWebapp = (key != null && key.getClass().getClassLoader() == cl)
                    || (value != null && value.getClass().getClassLoader() == cl);
            if (!ownedByWebapp) continue;
            if (key != null) {
                if (removeMethod == null) {
                    removeMethod = map.getClass().getDeclaredMethod("remove", ThreadLocal.class);
                    removeMethod.setAccessible(true);
                }
                removeMethod.invoke(map, key);
            } else {
                valueField.set(entry, null);
            }
        }
    }
}
