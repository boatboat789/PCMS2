package th.co.wacoal.atech.pcms2.utilities;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import th.co.wacoal.atech.pcms2.entities.ChangeSettingLogDetail;


public class HandlerListLog {

	private static final ThreadLocal<DecimalFormat> df =
			ThreadLocal.withInitial(() -> new DecimalFormat("#.##")); // 2 decimal places — ไม่ซ้ำ FormatUtils (format ต่างกัน)
    public static <T> ArrayList<ChangeSettingLogDetail> compareAndLogChanges(
            T oldObj,
            T newObj,
            Map<String, String> fieldToMscIdMap,
            String recordId,
            String userId,
            String remarkAction,
            String newRemark
        ) {
            ArrayList<ChangeSettingLogDetail> logList = new ArrayList<>();

            Field[] fields = oldObj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object oldVal = field.get(oldObj);
                    Object newVal = field.get(newObj);

                    String fieldName = field.getName();
                    fieldName = CapitalizeFirstLetter.capitalize(fieldName); 
                    String mscId = fieldToMscIdMap.get(fieldName);

                    if (mscId != null) {
                    	String remark = remarkAction;
                    	if(newRemark.equals("")) {
                    		remark += " ( " + newRemark + " ) ";
                    	}
                        logList = HandlerListLog.handlerListLog(
                            logList,
                            oldVal,
                            newVal,
                            mscId,
                            recordId,
                            userId,
                            fieldName,
                            remark
                        );
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // หรือ log
                }
            }

            return logList;
        } 
	public static ArrayList<ChangeSettingLogDetail> handlerListLog(ArrayList<ChangeSettingLogDetail> list,
	        Object oldVal, Object newVal, String mscId, String no, String changeBy, String fieldName, String remark) {
	    
	    if (!isEqual(oldVal, newVal)) {
	        ChangeSettingLogDetail beanObj = new ChangeSettingLogDetail();
	        beanObj.setMasterSettingChangeId(mscId);
	        beanObj.setFieldId(no);
	        beanObj.setFieldName(fieldName);
	        beanObj.setNewValue(formatValue(newVal));
	        beanObj.setOldValue(formatValue(oldVal));
	        beanObj.setCreateBy(changeBy);
	        beanObj.setRemark(remark);
	        list.add(beanObj);
	    }
	    return list;
	}

	private static boolean isEqual(Object oldVal, Object newVal) {
	    if (oldVal == newVal) {
	        return true;
	    }
	    if (oldVal == null || newVal == null) {
	        return false;
	    }
	    if (oldVal instanceof Number && newVal instanceof Number) {
	        return ((Number) oldVal).doubleValue() == ((Number) newVal).doubleValue();
	    }
	    if (oldVal instanceof Date && newVal instanceof Date) {
	        return ((Date) oldVal).getTime() == ((Date) newVal).getTime();
	    }
	    return oldVal.equals(newVal);
	}

	private static String formatValue(Object value) {
	    if (value == null) {
	        return "";
	    }
	    if (value instanceof Double) {
	        return df.get().format((Double) value);
	    }
	    if (value instanceof Number) {
	        return Integer.toString(((Number) value).intValue());
	    }
	    if (value instanceof Date) {
	        return FormatUtils.DAY_MONTH_YEAR_TIME_FORMAT.get().format((Date) value);
	    }
	    return value.toString();
	}
//	public static ArrayList<ChangeSettingLogDetail> handlerListLog(ArrayList<ChangeSettingLogDetail> list,
//			String oldVal, String newdVal, String mscId, String no, String changeBy, String fieldName, String remark)
//	{
//		if(oldVal == null) {
//			oldVal = "";
//		}
//		if(newdVal == null) {
//			newdVal = "";
//		}
//		if ( ! oldVal.equals(newdVal)) {
//			ChangeSettingLogDetail beanObj = new ChangeSettingLogDetail();
//			beanObj.setMasterSettingChangeId(mscId);
//			beanObj.setFieldId(no);
//			beanObj.setFieldName(fieldName);
//			beanObj.setNewValue(newdVal);
//			beanObj.setOldValue(oldVal);
//			beanObj.setCreateBy(changeBy);
//			beanObj.setRemark(remark);
//			list.add(beanObj);
//		}
//		return list;
//	}
//	public static ArrayList<ChangeSettingLogDetail> handlerListLog(ArrayList<ChangeSettingLogDetail> list,
//			double oldVal, double newdVal, String mscId, String no, String changeBy, String fieldName, String remark)
//	{
//
//		if ( oldVal != newdVal ) {
//			ChangeSettingLogDetail beanObj = new ChangeSettingLogDetail();
//			beanObj.setMasterSettingChangeId(mscId);
//			beanObj.setFieldId(no);
//			beanObj.setFieldName(fieldName);
//			beanObj.setNewValue(df.format(newdVal));
//			beanObj.setOldValue(df.format(oldVal));
//			beanObj.setCreateBy(changeBy);
//			beanObj.setRemark(remark);
//			list.add(beanObj);
//		}
//		return list;
//	}
//	public static ArrayList<ChangeSettingLogDetail> handlerListLog(ArrayList<ChangeSettingLogDetail> list,
//			short oldVal, short newdVal, String mscId, String no, String changeBy, String fieldName, String remark)
//	{
//
//		if ( oldVal != newdVal ) {
//			ChangeSettingLogDetail beanObj = new ChangeSettingLogDetail();
//			beanObj.setMasterSettingChangeId(mscId);
//			beanObj.setFieldId(no);
//			beanObj.setFieldName(fieldName);
//			beanObj.setNewValue(Integer.toString(newdVal));
//			beanObj.setOldValue(Integer.toString(oldVal));
//			beanObj.setCreateBy(changeBy);
//			beanObj.setRemark(remark);
//			list.add(beanObj);
//		}
//		return list;
//	}
//	public static ArrayList<ChangeSettingLogDetail> handlerListLog(ArrayList<ChangeSettingLogDetail> list,
//			int oldVal, int newdVal, String mscId, String no, String changeBy, String fieldName, String remark)
//	{
//
//		if ( oldVal != newdVal ) {
//			ChangeSettingLogDetail beanObj = new ChangeSettingLogDetail();
//			beanObj.setMasterSettingChangeId(mscId);
//			beanObj.setFieldId(no);
//			beanObj.setFieldName(fieldName);
//			beanObj.setNewValue(Integer.toString(newdVal)) ;
//			beanObj.setOldValue(Integer.toString(oldVal));
//			beanObj.setCreateBy(changeBy);
//			beanObj.setRemark(remark);
//			list.add(beanObj);
//		}
//		return list;
//	}
//	public static ArrayList<ChangeSettingLogDetail> handlerListLog(ArrayList<ChangeSettingLogDetail> list,
//			Date oldVal, Date newdVal, String mscId, String no, String changeBy, String fieldName, String remark)
//	{
//
//		if ( oldVal != newdVal ) {
//			ChangeSettingLogDetail beanObj = new ChangeSettingLogDetail();
//			beanObj.setMasterSettingChangeId(mscId);
//			beanObj.setFieldId(no);
//			beanObj.setFieldName(fieldName);
//			beanObj.setNewValue(SDF_DDMMYYY_HHMMSS_1.format(newdVal));
//			beanObj.setOldValue(SDF_DDMMYYY_HHMMSS_1.format(oldVal));
//			beanObj.setCreateBy(changeBy);
//			beanObj.setRemark(remark);
//			list.add(beanObj);
//		}
//		return list;
//	}
//	private static boolean isEqual(Object oldVal, Object newVal) {
//	    if (oldVal == newVal) {
//	        return true;
//	    }
//	    if (oldVal == null || newVal == null) {
//	        return false;
//	    }
//	    if (oldVal instanceof Number && newVal instanceof Number) {
//	        return ((Number) oldVal).doubleValue() == ((Number) newVal).doubleValue();
//	    }
//	    if (oldVal instanceof Date && newVal instanceof Date) {
//	        return ((Date) oldVal).getTime() == ((Date) newVal).getTime();
//	    }
//	    return oldVal.equals(newVal);
//	}
//	private static String formatValue(Object value) {
//	    if (value == null) {
//	        return "";
//	    }
//	    if (value instanceof Double) {
//	        return df.format((Double) value);
//	    }
//	    if (value instanceof Number) {
//	        return Integer.toString(((Number) value).intValue());
//	    }
//	    if (value instanceof Date) {
//	        return SDF_DDMMYYY_HHMMSS_1.format((Date) value);
//	    }
//	    return value.toString();
//	}
}
