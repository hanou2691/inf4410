package ca.polymtl.inf4410.tp1.shared;

public class FileStruct {
	
	 public FileStruct(String fileName, byte[] content, int idClient) {
		super();
		this.fileName = fileName;
		this.content = content;
		this.idClient = idClient;
	}
	public String fileName;
	public byte[] content;
	public int idClient;
	 

}
