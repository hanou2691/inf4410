package ca.polymtl.inf4410.tp1.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import ca.polymtl.inf4410.tp1.shared.FileStruct;
import ca.polymtl.inf4410.tp1.shared.ServerInterface;

public class Server implements ServerInterface {

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

	public Server() {
		super();
	}

	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			ServerInterface stub = (ServerInterface) UnicastRemoteObject
					.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("server", stub);
			System.out.println("Server ready.");
		} catch (ConnectException e) {
			System.err
					.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}

	/*
	 * Méthode accessible par RMI. Additionne les deux nombres passés en
	 * paramètre.
	 */
	public byte[] generateClienId() throws RemoteException {
		String uniqueId = UUID.randomUUID().toString();
		return uniqueId.getBytes();
	}

	@Override
	public int create(String fileName) throws IOException {
		String currentDir = Paths.get("").toAbsolutePath().toString();
		File file = new File(currentDir + "/files/" + fileName);
		File lockFile = new File(currentDir + "/files/locksList.csv");

		if (!file.exists()) {
			// Create file
			file.createNewFile();
			// Add to CSV with clientid = 0
			if (!lockFile.exists()) {
				lockFile.createNewFile();
				PrintWriter pw = new PrintWriter(lockFile);
				pw.write(fileName + ";0");
				pw.close();
			}
		}

		return 0;
	}

	@Override
	public ArrayList<FileStruct> list() throws IOException {
		// TODO Auto-generated method stub
		String currentDir = Paths.get("").toAbsolutePath().toString()
				+ "/files/";
		File file = new File(currentDir + "locksList.csv");

		ArrayList<String> files = (ArrayList<String>) Files.readAllLines(file
				.toPath());

		ArrayList<FileStruct> filesList = new ArrayList<FileStruct>();
		for (int i = 0; i < files.size(); i++) {
			String[] vals = files.get(i).split(";");
			filesList.add(new FileStruct(vals[0], null, vals[1].getBytes()));
		}

		return filesList;
	}

	@Override
	public ArrayList<FileStruct> syncLocalDir() throws IOException {
		String currentDir = Paths.get("").toAbsolutePath().toString()
				+ "/files";
		File folder = new File(currentDir);
		File[] files = folder.listFiles();

		ArrayList<FileStruct> filesList = new ArrayList<FileStruct>();

		for (int i = 0; i < files.length; i++) {
			byte[] fileContent = Files.readAllBytes(files[i].toPath());
			FileStruct fileStruct = new FileStruct(files[i].getName(),
					fileContent, null);
			filesList.add(fileStruct);
		}

		return filesList;
	}

	@Override
	public FileStruct get(String name, long checksum) throws IOException {
		// TODO Auto-generated method stub
		String currentDir = Paths.get("").toAbsolutePath().toString();
		File file = new File(currentDir + "/files/" + name);

		if (file.exists()) {
			// if no calculate local checksum
			long check = calculateChecksum(name);

			// if different checksums send file
			if (check != checksum || checksum == -1) {
				// send file
				byte[] fileContent = Files.readAllBytes(file.toPath());
				return new FileStruct(file.getName(), fileContent, null);
			}
		}

		return null;
	}

	@Override
	public FileStruct lock(String name, long checksum, byte[] clientId)
			throws IOException {
		// TODO Auto-generated method stub
		String currentDir = Paths.get("").toAbsolutePath().toString()+"/files/";
		File file = new File(currentDir+"locksList.csv");
		ArrayList<String> fileContent = (ArrayList<String>) Files.readAllLines(file.toPath());
		
		for (int i = 0; i < fileContent.size(); i++){
			String[] vals = fileContent.get(i).split(";");			
			if(vals[0] == name && vals[1] == "0") {
				// Update locks
				vals[1] = String.valueOf(clientId);
				fileContent.set(i,name + ";"+ vals[1]);
				Files.write(file.toPath(), fileContent);
				
				// Load file
				file = new File(currentDir+"/files/"+name);
				
				// Calculate checksum
				long check = calculateChecksum(name);
				
				// Send file to client	
				// if different checksums send file
				if(check != checksum || checksum == -1){
					// send file
					byte[] fc = Files.readAllBytes(file.toPath());
					return new FileStruct(file.getName(), fc, clientId);
				}
			}
		}		
		
		return null;
	}

	@Override
	public int push(String name, byte[] content, byte[] clientId)
			throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long calculateChecksum(String fileName) throws IOException {
		byte[] content = Files.readAllBytes(Paths.get(fileName));
		Checksum checksum = new CRC32();
		checksum.update(content, 0, content.length);
		return checksum.getValue();
	}
}
