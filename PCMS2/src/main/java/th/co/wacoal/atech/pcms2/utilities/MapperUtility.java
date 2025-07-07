package th.co.wacoal.atech.pcms2.utilities;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class MapperUtility {

    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String fieldName = entry.getKey();
                Object value = entry.getValue();

                try {
                    // แปลงชื่อคอลัมน์จาก Map (มักจะเป็น PascalCase) ให้เป็นชื่อ field ใน Java (camelCase)
                    // เช่น "ProductionOrder" -> "productionOrder"
                    String javaFieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

                    Field field = clazz.getDeclaredField(javaFieldName);
                    field.setAccessible(true); // อนุญาตให้เข้าถึง private fields

                    // จัดการ Type Casting
                    if (value == null) {
                        field.set(instance, null);
                    } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                        field.set(instance, ((Number) value).intValue());
                    } else if (field.getType().equals(String.class)) {
                        field.set(instance, (String) value);
                    } else if (field.getType().equals(Date.class)) {
                        field.set(instance, (Date) value);
                    } else if (field.getType().equals(BigDecimal.class)) {
                        field.set(instance, (BigDecimal) value);
                    } else if (field.getType().equals(Timestamp.class)) {
                        field.set(instance, (Timestamp) value);
                    } else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                        field.set(instance, ((Number) value).doubleValue());
                    } else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
                        field.set(instance, ((Number) value).floatValue());
                    } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                        field.set(instance, ((Number) value).longValue());
                    } else if (field.getType().equals(Short.class) || field.getType().equals(short.class)) {
                        field.set(instance, ((Number) value).shortValue());
                    } else if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                        field.set(instance, (Boolean) value);
                    }
                    // เพิ่มเงื่อนไขสำหรับ Data Type อื่นๆ ที่คุณมี หากจำเป็น
                    else {
                        // สำหรับ Type อื่นๆ ที่ไม่ได้ระบุเฉพาะเจาะจง
                        // พยายามตั้งค่าโดยตรง หรืออาจจะต้องจัดการเป็นพิเศษถ้ามีปัญหา
                        field.set(instance, value);
                    }
                } catch (NoSuchFieldException e) {
                    // หากไม่มี field ใน Class ที่ตรงกับ key ใน Map ให้ข้ามไป
//                    System.err.println("Warning: Field '" + fieldName + "' not found in " + clazz.getName());
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping map to object of type " + clazz.getName(), e);
        }
    }
}