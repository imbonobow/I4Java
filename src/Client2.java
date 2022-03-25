import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;


public class Client2 {

	public final static int SOCKET_PORT = 13267;      // you may change this
	public final static String SERVER = "127.0.0.1";  // localhost
	public static String FILE_TO_RECEIVED = "/Users/Mathis/I4Racine/";
	public static String FILE_TO_RECEIVED_INIT = "/Users/Mathis/I4Racine/";
	public final static int FILE_SIZE = 6022386; // file size temporary hard coded
// should bigger than the file to be downloaded

	public static void main (String[] args) throws IOException
	{
		int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		Socket sock = null;
		String line = "";
		int numfile = 0;
		try
		{
			sock = new Socket(SERVER, SOCKET_PORT);
			System.out.println("Connecting...");
			DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
			DataInputStream his = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
			DataInputStream dis = new DataInputStream(System.in);

			while (true)
			{
				line = dis.readLine();
				dos.writeUTF(line);
				dos.flush();
				if (line.equals("/transfert"))
				{
					// receive file
					byte[] mybytearray = new byte[FILE_SIZE];
					InputStream is = sock.getInputStream();

					String nomfichier = his.readUTF();
					long taillefichier = his.readLong();

					//Derniére date de modification du fichier
					Path file = Paths.get(nomfichier);
					BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

					System.out.println();
					System.out.println(" !!!!! INFORMATION !!!!! ");
					//Le serveur indique les informations du fichier et la taille au client
					System.out.println("Nom du fichier : " + nomfichier);
					System.out.println("Taille = " + taillefichier);
					System.out.println("lastModifiedTime: " + attr.lastModifiedTime());

					//Ici je parse le fichier à télécharger : Path, nom du fichier, extension du fichier
					File f = new File(nomfichier);
					System.out.println("F : "+f);
					//path du fichier parent
					System.out.println("Path-->" + f.getParent());
					//nom du fichier
					System.out.println("file--->" + f.getName());
					//extension
					int idx = f.getName().lastIndexOf('.');
					System.out.println("extension--->" + ((idx > 0) ? f.getName().substring(idx) : "") );
					//File without extension
					String fileNameWithOutExt = f.getName().replaceFirst("[.][^.]+$", "");
					System.out.println(fileNameWithOutExt);

					//Endroit ou je te télécharge le fichier (racine + nom du fichier)
					FILE_TO_RECEIVED=FILE_TO_RECEIVED+f.getName();
					System.out.println("File_To_receive : "+FILE_TO_RECEIVED);

					//Si le nom du fichier existe déja alors je rajoute .1
					File z = new File(FILE_TO_RECEIVED);
					System.out.println("Z : "+z);
					while(z.exists() && !z.isDirectory()) {
						System.out.println("Le fichier existe déja");
						numfile++;
						System.out.println(numfile);
						FILE_TO_RECEIVED = fileNameWithOutExt+"."+numfile+f.getName().substring(idx);
						z = new File(FILE_TO_RECEIVED);
					}

					FILE_TO_RECEIVED=FILE_TO_RECEIVED_INIT+FILE_TO_RECEIVED;

					fos = new FileOutputStream(FILE_TO_RECEIVED);
					bos = new BufferedOutputStream(fos);
					bytesRead = is.read(mybytearray, 0, mybytearray.length);
					current = bytesRead;

					do
					{
						bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
						if (bytesRead >= 0) {
							current += bytesRead;
						}
						System.out.println(current);
						FILE_TO_RECEIVED = FILE_TO_RECEIVED_INIT;
					}while (current != taillefichier);
					bos.write(mybytearray, 0, current);
					bos.flush();
					fos.close();
					System.out.println("File " + FILE_TO_RECEIVED + " downloaded (" + current + " bytes read)");
					System.out.println();
				}
			}
		}

		finally {
			if (fos != null) fos.close();
			if (bos != null) bos.close();
			if (sock != null) sock.close();
		}
	}

}