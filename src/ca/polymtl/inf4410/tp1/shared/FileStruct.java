package ca.polymtl.inf4410.tp1.shared;
import java.io.Serializable;

public class FileStruct implements Serializable{
	
	 public FileStruct(String fileName, byte[] content, byte[] idClient) {
		super();
		this.fileName = fileName;
		this.content = content;
		this.idClient = idClient;
	}
	public String fileName;
	public byte[] content;
	public byte[] idClient;
	 

}
