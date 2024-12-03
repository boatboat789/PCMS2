package entities.PPMM;

public class InspectOrdersDetail {
	private String prdNumber; 
	private String inspectNote;
	private String rollupNote;
	private String machineInspect;
	private String machineRollup;  
	public InspectOrdersDetail(String prdNumber, String inspectNote, String rollupNote, String machineInspect,
			String machineRollup) {
		super();
		this.prdNumber = prdNumber;
		this.inspectNote = inspectNote;
		this.rollupNote = rollupNote;
		this.machineInspect = machineInspect;
		this.machineRollup = machineRollup;
	}
	public String getMachineInspect()
	{
		return machineInspect;
	}
	public void setMachineInspect(String machineInspect)
	{
		this.machineInspect = machineInspect;
	}
	public String getMachineRollup()
	{
		return machineRollup;
	}
	public void setMachineRollup(String machineRollup)
	{
		this.machineRollup = machineRollup;
	}
	public String getPrdNumber()
	{
		return prdNumber;
	}
	public void setPrdNumber(String prdNumber)
	{
		this.prdNumber = prdNumber;
	}
	public String getRollupNote()
	{
		return rollupNote;
	}
	public void setRollupNote(String rollupNote)
	{
		this.rollupNote = rollupNote;
	}
	public String getInspectNote()
	{
		return inspectNote;
	}
	public void setInspectNote(String inspectNote)
	{
		this.inspectNote = inspectNote;
	}
	  
}
