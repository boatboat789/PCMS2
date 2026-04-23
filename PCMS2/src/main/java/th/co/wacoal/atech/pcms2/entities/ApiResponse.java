package th.co.wacoal.atech.pcms2.entities;

import javax.persistence.Entity;

@Entity
public class ApiResponse<T> {
    private String status;     // "success" หรือ "error"
    private String message;    // รายละเอียดข้อความ
    private T data;            // อะไรก็ได้ (List, Object, null)

    public ApiResponse() {}

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public T getData()
	{
		return data;
	}

	public void setData(T data)
	{
		this.data = data;
	}

    // Getters & Setters
}