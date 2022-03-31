package entities;

public class EncryptedDetail {
private String Text;
private String Encrypted;
private String Decrypted;

public EncryptedDetail() {
	super();
}
public EncryptedDetail(String text, String encrypted, String decrypted) {
	super();
	Text = text;
	Encrypted = encrypted;
	Decrypted = decrypted;
}
public String getText() {
	return Text;
}
public void setText(String text) {
	Text = text;
}
public String getEncrypted() {
	return Encrypted;
}
public void setEncrypted(String encrypted) {
	Encrypted = encrypted;
}
public String getDecrypted() {
	return Decrypted;
}
public void setDecrypted(String decrypted) {
	Decrypted = decrypted;
}

}
