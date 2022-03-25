import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {

	public final static int SOCKET_PORT = 13267;      // you may change this
	public final static String SERVER = "127.0.0.1";  // localhost
	public static String
			FILE_TO_RECEIVED = "/Users/Mathis/I4Racine/test.pdf";
	public final static int FILE_SIZE = 6022386; // file size temporary hard coded
// should bigger than the file to be downloaded

	public static void main (String[] args) throws IOException {
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
					/*
					File f = new File(FILE_TO_RECEIVED);
					while(f.exists() && !f.isDirectory()) {
						System.out.println("Le fichier existe déja");
						FILE_TO_RECEIVED = FILE_TO_RECEIVED+"(" + numfile+ ")";
						numfile++;
					}
					*/

					String nomfichier = his.readUTF();
					long taillefichier = his.readLong();

					System.out.println("Server : " + nomfichier + "& Taille = " + taillefichier);


					int n = 0;

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
					}while (current != taillefichier);
					bos.write(mybytearray, 0, current);
					bos.flush();
					fos.close();
					System.out.println("File " + FILE_TO_RECEIVED + " downloaded (" + current + " bytes read)");
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