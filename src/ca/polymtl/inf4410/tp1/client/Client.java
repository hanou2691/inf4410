package ca.polymtl.inf4410.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Math;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import ca.polymtl.inf4410.tp1.shared.FileStruct;
import ca.polymtl.inf4410.tp1.shared.ServerInterface;

public class Client {
	
	
	public static void main(String[] args) throws IOException {
		
		 parseArgs(args);
		
	}
	public static void parseArgs(String[] args) throws IOException{
		String ip = "127.0.0.1";
		String idStr = "";
		Client client = new Client(ip);
		ArrayList<FileStruct> files= new ArrayList<FileStruct>() ;
		FileStruct file = new FileStruct(null,null,null);
		
		ServerInterface localServerStub = loadServerStub("127.0.0.1");
		byte[] clientId = null;
	
		String currentDir= Paths.get("").toAbsolutePath().toString();
		File clientFolder = new File(currentDir + "/ClientFolder" );
		if (!clientFolder.exists()) {
			clientFolder.mkdir();
		}
		File fileId= new File(currentDir + "/ClientFolder/" + idFile);
		
		
		if (!fileId.exists()) {
			fileId.createNewFile();	 
			clientId = localServerStub.generateClienId();
			idStr = new String(clientId, StandardCharsets.UTF_8);
			
			PrintWriter pwId = new PrintWriter(fileId);  
			pwId.println(idStr);	 
			pwId.close();
			
			
		}	    
	    
		//long checksum = calculateChecksum(args[1]); //calculateChecksum(args[0]);
		
		long checksum = -1;
		switch(args[0]){
		case "create":
			localServerStub.create(args[1]);
			break;
		case "list":
			files = localServerStub.list();
			String str = null;
			for (int i = 0; i < files.size(); i++) {
				str = new String(files.get(i).idClient, StandardCharsets.UTF_8);
				System.out.println(files.get(i).fileName + "ClientId : " + str);
			}
			
			break;
		case "push":
			// Load content
			//String currentDir2 = Paths.get("").toAbsolutePath().toString();
			//File file2 = new File(currentDir2 + "/files/" + args[1]);
			
			//byte[] content = Files.readAllBytes(file2.toPath());
			// Load client id
			//clientId = getClientId(idFile);
			
			//localServerStub.push(args[1], content, clientId);
			break;
		case "lock":
			file=localServerStub.lock(args[1], checksum, clientId);
			String str4;
			String Clientid;
		
			
			File fichier_ = new File(currentDir + "/ClientFolder/" + file.fileName);
			
			if (!fichier_.exists()) {
					fichier_.createNewFile();
			}	
			if (file.content != null){
			Clientid = new String(file.idClient, StandardCharsets.UTF_8);
			System.out.println("fichier : " + args[1] + " a ete verouille par  : " + Clientid );
				//On copie contenu dans le fichier (Remplacement)
			str4 = new String(file.content, StandardCharsets.UTF_8);
			PrintWriter pw = new PrintWriter(fichier_);  
			pw.println(str4);	 
			pw.close();
			}
			else{
				str4 = new String(file.idClient, StandardCharsets.UTF_8);
				System.out.println("fichier : " + args[1] + " a ete deja ete verouille par  : " + str4 );	
			}
			break;
		case "get":
			file=localServerStub.get(args[1], checksum);
			String str3 ;
			if (file==null){
				System.out.println("fichier est deja a jour ou nexiste pas ");
			}
			else{

			
				File fichier = new File(currentDir + "/ClientFolder/" + file.fileName);
					if (!fichier.exists()) {
						// Create file
						fichier.createNewFile();
						System.out.println("fichier : " + args[1] + " a ete creer");
					}
					else {
						System.out.println("fichier : " + args[1] + " a ete mis a jour");
					}
					//On copie contenu dans le fichier (Remplacement)
					str3 = new String(file.content, StandardCharsets.UTF_8);
				    PrintWriter pw2 = new PrintWriter(fichier);  
				    pw2.println(str3);	 
				    pw2.close();
				    
			}
			break;
		case "syncLocalDir":
			files = localServerStub.syncLocalDir();
			String str2 ;
			
			for (int i = 0; i < files.size(); i++) {			
				File fichier = new File(currentDir + "/ClientFolder/" + files.get(i).fileName);
				if (!fichier.exists()) {
					// Create file
					fichier.createNewFile();
				}
				
				//On copie contenu dans le fichier
				str2 = new String(files.get(i).content, StandardCharsets.UTF_8);
			    PrintWriter pw1 = new PrintWriter(fichier);  
			    pw1.println(str2);	 
			    pw1.close();
			}
			
			break;
		}
		
	}
	
	static long calculateChecksum(String fileName) throws IOException{
	
		String currentDir2 = Paths.get("").toAbsolutePath().toString();
		File file2 = new File(currentDir2 + "/ClientFolder/" + fileName);
		byte[] content = Files.readAllBytes(file2.toPath());//a modifier
		Checksum checksum = new CRC32();
		checksum.update(content, 0, content.length);
		return checksum.getValue();
	}
	
	static byte[] getClientId(String fileName) throws IOException{
		byte[] id= Files.readAllBytes(Paths.get(fileName));
		return id;
		
	}
	FakeServer localServer = null; // Pour tester la latence d'un appel de
									// fonction normal.
	private ServerInterface localServerStub = null;
	private ServerInterface distantServerStub = null;
	
	private static final String idFile = "__clientId.txt";
	
	public Client(String distantServerHostname) {
		super();

		//if (System.getSecurityManager() == null) {
		//	System.setSecurityManager(new SecurityManager());
		//}

		localServer = new FakeServer();
		localServerStub = loadServerStub("127.0.0.1");

		if (distantServerHostname != null) {
			distantServerStub = loadServerStub(distantServerHostname);
		}
	}

	private void run() {
		appelNormal();

		if (localServerStub != null) {
			appelRMILocal();
		}

		if (distantServerStub != null) {
			appelRMIDistant();
		}
	}

	private static ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup("server");
		} catch (NotBoundException e) {
			System.out.println("Erreur: Le nom '" + e.getMessage()
					+ "' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.out.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}

		return stub;
	}

	private void appelNormal() {
		/*long start = System.nanoTime();
		//int result = localServer.execute(4, 7);
		localServer.execute(arr);
		long end = System.nanoTime();

		System.out.println("Temps écoulé appel normal: " + (end - start)
				+ " ns");
		//System.out.println("Résultat appel normal: " + result);*/
	}

	private void appelRMILocal() {
		/*try {
			long start = System.nanoTime();
			//int result = localServerStub.execute(4, 7);
			//localServerStub.execute(arr);
			long end = System.nanoTime();

			System.out.println("Temps écoulé appel RMI local: " + (end - start)
					+ " ns");
			//System.out.println("Résultat appel RMI local: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}*/
	}

	private void appelRMIDistant() {
		/*try {
			long start = System.nanoTime();
			//int result = distantServerStub.execute(4, 7);
			distantServerStub.cre(obj)xecute(arr);
			long end = System.nanoTime();

			System.out.println("Temps écoulé appel RMI distant: "
					+ (end - start) + " ns");
			//System.out.println("Résultat appel RMI distant: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}*/
	}
}
