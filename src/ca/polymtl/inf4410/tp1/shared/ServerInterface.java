package ca.polymtl.inf4410.tp1.shared;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
	byte[] generateClienId() throws RemoteException;
	int create(String fileName) throws IOException;
	ArrayList<FileStruct> list() throws IOException;
	ArrayList<FileStruct> syncLocalDir() throws IOException;
	FileStruct get(String name,long checksum) throws IOException;
	FileStruct lock(String name,long checksum,byte[] clientId) throws IOException;
	int push(String name,byte[] content,byte[] clientId) throws IOException;
}
