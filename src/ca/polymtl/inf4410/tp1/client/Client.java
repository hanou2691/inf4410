package ca.polymtl.inf4410.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
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
		Client client = new Client(ip);
		ArrayList<FileStruct> files= new ArrayList<FileStruct>() ;
		FileStruct file = new FileStruct();
		
		ServerInterface localServerStub = loadServerStub("127.0.0.1");
		byte[] clientId = null;
		long checksum = calculateChecksum(args[1]);
		switch(args[0]){
		case "create":
			localServerStub.create(args[1]);
			break;
		case "list":
			files = localServerStub.list();
			break;
		case "push":
			// Load content
			byte[] content = Files.readAllBytes(Paths.get(args[1]));
			// Load client id
			clientId = getClientId(idFile);
			
			localServerStub.push(args[1], content, clientId);
			break;
		case "lock":
			file=localServerStub.lock(args[1], checksum, clientId);
			 
			break;
		case "get":
			file=localServerStub.get(args[1], checksum);
			break;
		case "syncLocalDir":
			
			files = localServerStub.syncLocalDir();
			break;
		}
		
	}
	
	static long calculateChecksum(String fileName) throws IOException{
		byte[] content = Files.readAllBytes(Paths.get(fileName));
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
	
	private static final String idFile = "__cliendId.txt";
	
	public Client(String distantServerHostname) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

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
		long start = System.nanoTime();
		//int result = localServer.execute(4, 7);
		localServer.execute(arr);
		long end = System.nanoTime();

		System.out.println("Temps écoulé appel normal: " + (end - start)
				+ " ns");
		//System.out.println("Résultat appel normal: " + result);
	}

	private void appelRMILocal() {
		try {
			long start = System.nanoTime();
			//int result = localServerStub.execute(4, 7);
			localServerStub.execute(arr);
			long end = System.nanoTime();

			System.out.println("Temps écoulé appel RMI local: " + (end - start)
					+ " ns");
			//System.out.println("Résultat appel RMI local: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	private void appelRMIDistant() {
		try {
			long start = System.nanoTime();
			//int result = distantServerStub.execute(4, 7);
			distantServerStub.cre(obj)xecute(arr);
			long end = System.nanoTime();

			System.out.println("Temps écoulé appel RMI distant: "
					+ (end - start) + " ns");
			//System.out.println("Résultat appel RMI distant: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}
}
