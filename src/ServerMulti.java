// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class ServerMulti
{

	// Vector to store active clients
	static Vector<ClientHandler> ar = new Vector<>();

	// counter for clients
	static int i = 0;

	public static void main(String[] args) throws IOException
	{
		// server is listening on port 1234
		ServerSocket ss = new ServerSocket(13267);

		Socket s;

		// running infinite loop for getting
		// client request
		while (true)
		{
			// Accept the incoming request
			s = ss.accept();

			System.out.println("New client request received : " + s);

			// obtain input and output streams
			DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());


			System.out.println("Creating a new handler for this client...");

			// Create a new handler object for handling this request.
			ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos);

			// Create a new Thread with this object.
			Thread t = new Thread(mtch);

			System.out.println("Adding this client to active client list");

			// add this client to active clients list
			ar.add(mtch);

			// start the thread.
			t.start();

			// increment i for new client.
			// i is used for naming only, and can be replaced
			// by any naming scheme
			i++;

		}
	}
}

// ClientHandler class
class ClientHandler implements Runnable
{
	public final static String FILE_TO_SEND = "/Users/Mathis/Desktop/E4ESIEE/FiwARE/IntroductionFiWare_v19.1.pdf";
	Scanner scn = new Scanner(System.in);
	private String name;
	final DataInputStream dis;
	final DataOutputStream dos;
	FileInputStream fis = null;
	BufferedInputStream bis = null;
	OutputStream os = null;
	Socket s;
	boolean isloggedin;

	// constructor
	public ClientHandler(Socket s, String name,
						 DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.name = name;
		this.s = s;
		this.isloggedin=true;
	}

	@Override
	public void run() {
		String received;
		try
		{
			while (true)
			{
				String line = dis.readUTF();
				System.out.println(s.getInetAddress().toString() +" : "+ line);

				if (line.equals("/transfert"))
				{

					// send file
					File myFile = new File(FILE_TO_SEND);
					byte[] mybytearray = new byte[(int) myFile.length()];

					//envoi de la taille du fichier
					dos.writeUTF(FILE_TO_SEND);
					dos.writeLong(mybytearray.length);

					fis = new FileInputStream(myFile);
					bis = new BufferedInputStream(fis);
					bis.read(mybytearray, 0, mybytearray.length);
					os = s.getOutputStream();
					System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
					os.write(mybytearray, 0, mybytearray.length);
					os.flush();
					System.out.println("Done.");
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
		try
		{
			// closing resources
			this.dis.close();
			this.dos.close();

		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
