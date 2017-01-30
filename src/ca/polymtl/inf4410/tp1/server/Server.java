package ca.polymtl.inf4410.tp1.server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

public class Server implements ServerInterface  {

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

	public Server() {
		super();
	}

	private void run() {
		//if (System.getSecurityManager() == null) {
			//System.setSecurityManager(new SecurityManager());
		//}

		try {
			ServerInterface stub = (ServerInterface) UnicastRemoteObject
					.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("server", stub);
			System.out.println("Server ready..........");
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
		System.out.println(uniqueId);
		return uniqueId.getBytes();
	}

	public int create(String fileName) throws IOException {
		String currentDir = Paths.get("").toAbsolutePath().toString();
		File file = new File(currentDir + "/files/" + fileName);
		File lockFile = new File(currentDir + "/files/locksList.csv");
	
		if (!file.exists()) {
			// Create file
			file.createNewFile();
			// Add to CSV with clientid = 0
			//PrintWriter pw = new PrintWriter(lockFile);
			if (!lockFile.exists()) {
				lockFile.createNewFile();
			}
		
			try {
			//On ecrit dans le fichier locklist
			FileWriter fw = new FileWriter(lockFile, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
		    out.println(fileName + ";0");
		    out.close();
			}
			catch(IOException e){
				      e.printStackTrace();
			}
			
		}

		return 0;
	}

	public ArrayList<FileStruct> list() throws IOException {
		// TODO Auto-generated method stub
		
		String currentDir = Paths.get("").toAbsolutePath().toString() + "/files/";
		System.out.println("path ="+ currentDir);
		File file = new File(currentDir + "/locksList.csv");
	
		ArrayList<String> files = (ArrayList<String>) Files.readAllLines(file
				.toPath());

		ArrayList<FileStruct> filesList = new ArrayList<FileStruct>();
		for (int i = 0; i < files.size(); i++) {
			String[] vals = files.get(i).split(";");
			filesList.add(new FileStruct(vals[0], null, vals[1].getBytes()));
			
			//System.out.println(filesList.get(i).fileName);
		}

		return filesList;
	}

	public ArrayList<FileStruct> syncLocalDir() throws IOException {
		String currentDir = Paths.get("").toAbsolutePath().toString()
				+ "/files";
		File folder = new File(currentDir);
		File[] files = folder.listFiles();

		ArrayList<FileStruct> filesList = new ArrayList<FileStruct>();
		

		for (int i = 0; i < files.length; i++) {
			if(files[i].getName().equals("locksList.csv")){
				continue;
			}
			byte[] fileContent = Files.readAllBytes(files[i].toPath());
			
			FileStruct fileStruct = new FileStruct(files[i].getName(),
					fileContent, null);
			filesList.add(fileStruct);
		
		}

		return filesList;
	}

	public FileStruct get(String name, long checksum) throws IOException {
		// TODO Auto-generated method stub
		String currentDir = Paths.get("").toAbsolutePath().toString();
		File file = new File(currentDir + "/files/" + name);
		
		if (file.exists()) {
			// if no calculate local checksum
			System.out.print("out");
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

	public FileStruct lock(String name, long checksum, byte[] clientId)
			throws IOException {
		// TODO Auto-generated method stub
				String currentDir = Paths.get("").toAbsolutePath().toString()+"/files/";
				File file = new File(currentDir+"/locksList.csv");
				ArrayList<String> fileContent = (ArrayList<String>) Files.readAllLines(file.toPath());
				
				for (int i = 0; i < fileContent.size(); i++){
					String[] vals = fileContent.get(i).split(";");	
					System.out.println(vals[0] + "  " + vals[1]);
					if(vals[0].equals(name) && vals[1].equals("0")) {
						// Update locks
						
						vals[1] = String.valueOf(clientId);	
						String str;
						System.out.println("hgjh" + vals[1]);
						
						str = new String(clientId, StandardCharsets.UTF_8);
						System.out.println(str);
						fileContent.set(i,name + ";"+ str);
						Files.write(file.toPath(), fileContent);
                        
						// Load file
						file = new File(currentDir + name);
						
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
					else if (vals[0].equals(name) && !(vals[1].equals("0"))){
						return new FileStruct(file.getName(), null, vals[1].getBytes());
						
					}
				}		
				
				return null;
		}


	/*Envoie une nouvelle version du fichier sp�cifi� au
	serveur. L'op�ration �choue si le fichier n'avait pas
	�t� verrouill� par le client pr�alablement. Si le
	push r�ussit, le contenu envoy� par le client
	remplace le contenu qui �tait sur le serveur
	auparavant et le fichier est d�verrouill�.*/
	
	public int push(String name, byte[] content, byte[] clientId)
			throws IOException {
		// TODO Auto-generated method stub
		
		String cl;
	
		//on trouve fichier 
		String currentDir = Paths.get("").toAbsolutePath().toString();
		File file = new File(currentDir + "/files/" + name); 
		FileOutputStream Filestream = new FileOutputStream(file, false);
		
		File fileList = new File(currentDir+ "/files/locksList.csv");
		ArrayList<String> List_files = (ArrayList<String>) Files.readAllLines(fileList.toPath());
		
		//fichier existe sur serveur ?
		if (file.exists()) {
			System.out.println("fichier : " + name + "trouve");	
			//fichier a ete verouille ce client?
			for (int i = 0; i < List_files.size(); i++){
				String[] vals = List_files.get(i).split(";");	
				cl = new String(clientId, StandardCharsets.UTF_8);
				System.out.println(vals[1].equals(cl));
				System.out.println(vals[1]);
				System.out.println(cl);
				if(vals[0].equals(name) && vals[1] == cl) {
					System.out.println(cl + "a bien verouille le fichier" + name);
					//on remplace le contenu du fichier
				
					Filestream.write(content);
					Filestream.close();
					System.out.println("le contenu du fichier" + name + "a ete mis a jour" );
					//deverouille le fichier
					// Update locks
					vals[1] = "0";
					List_files.set(i,name + ";"+ vals[1]);
					Files.write(file.toPath(), List_files);
					System.out.println("le fichier" + name + "a ete deverouille" );
					return 0;
				}
				else if (vals[0].equals(name)  && !(vals[1].equals(cl)) && !(vals[1].equals("0"))){
				    //fichier verrouille par un autre
					System.out.println("le fichier " + name + " est verouille par un autre client");
					return 0;
				}
				else if (vals[0].equals(name) && vals[1].equals("0")){
					System.out.println("Verouillez le fichier " + name + " avant de le modifier");
					return 0;
				}		
		
			}
			
		}
		else {
			System.out.println("le fichier" + name + "nest pas sur le serveur");
			return 0;
		}
		return 0;
	}

	public long calculateChecksum(String fileName) throws IOException {
		String currentDir2 = Paths.get("").toAbsolutePath().toString();
		File file2 = new File(currentDir2 + "/files/" + fileName);
		byte[] content = Files.readAllBytes(file2.toPath());//a modifier
		Checksum checksum = new CRC32();
		checksum.update(content, 0, content.length);
		return checksum.getValue();
	}
}
