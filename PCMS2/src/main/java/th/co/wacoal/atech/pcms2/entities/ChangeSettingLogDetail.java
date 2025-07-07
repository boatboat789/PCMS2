package th.co.wacoal.atech.pcms2.entities;

public class ChangeSettingLogDetail {
	private String id;
	private String masterSettingChangeId;
	private String fieldId;
//	private String tableName;
	private String fieldName;
	private String oldValue;
	private String newValue;
	private String remark;
	private String createBy;

	public ChangeSettingLogDetail() {
		super();
	}

	public ChangeSettingLogDetail(String id, String masterSettingChangeId, String fieldId
//			, String tableName
			, String fieldName, String oldValue, String newValue, String createBy, String remark) {
		super();
		this.id = id;
		this.remark = remark;
		this.fieldId = fieldId;
		this.masterSettingChangeId = masterSettingChangeId;
//		this.tableName = tableName;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.createBy = createBy;
	}

	public String getCreateBy() {
		return createBy;
	}

	public String getFieldId() {
		return fieldId;
	}

	// public String getTableName() {
//		return tableName;
//	}
//	public void setTableName(String tableName) {
//		this.tableName = tableName;
//	}
	public String getFieldName() {
		return fieldName;
	}

	public String getId() {
		return id;
	}

	public String getMasterSettingChangeId() {
		return masterSettingChangeId;
	}

	public String getNewValue() {
		return newValue;
	}

	public String getOldValue() {
		return oldValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMasterSettingChangeId(String masterSettingChangeId) {
		this.masterSettingChangeId = masterSettingChangeId;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
