package entities;

public class EncryptedDetail {
	private String text;
	private String encrypted;
	private String decrypted;

	public EncryptedDetail() {
		super();
	}

	public EncryptedDetail(String text, String encrypted, String decrypted) {
		super();
		this.text = text;
		this.encrypted = encrypted;
		this.decrypted = decrypted;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}

	public String getDecrypted() {
		return decrypted;
	}

	public void setDecrypted(String decrypted) {
		this.decrypted = decrypted;
	}

}
