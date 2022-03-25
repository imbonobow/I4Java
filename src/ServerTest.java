import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

	public final static int SOCKET_PORT = 13267;  // you may change this
	public final static String FILE_TO_SEND = "/Users/Mathis/Desktop/E4ESIEE/FiwARE/IntroductionFiWare_v19.1.pdf";  // you may change this

	public static void main (String [] args ) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		ServerSocket servsock = null;
		Socket sock = null;
		String confirmation = "Ended";
		try
		{
			servsock = new ServerSocket(SOCKET_PORT);
			boolean done = false;
			System.out.println("Waiting...");
			try
			{
				sock = servsock.accept();
				System.out.println("Accepted connection : " + sock);
				DataInputStream dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
				DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
				DataInputStream his = new DataInputStream(System.in);
				while (true)
				{
					String line = dis.readUTF();
					System.out.println(line);
					System.out.println("Client : " + line);

					if (line.equals("/transfert"))
					{
						// send file
						File myFile = new File(FILE_TO_SEND);
						byte[] mybytearray = new byte[(int) myFile.length()];
						fis = new FileInputStream(myFile);
						bis = new BufferedInputStream(fis);
						bis.read(mybytearray, 0, mybytearray.length);
						os = sock.getOutputStream();
						System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
						os.write(mybytearray, 0, mybytearray.length);
						os.flush();
						System.out.println("Done.");
						break;

					}
				}
			}
			finally
			{
				if (bis != null) bis.close();
				if (os != null) os.close();
				if (sock != null) sock.close();

			}
		}
		finally {
			if (servsock != null) servsock.close();
		}
	}
}