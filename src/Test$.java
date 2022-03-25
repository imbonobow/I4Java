import java.io.File;

public class Test$
{
	public static void main (String[] args)
	{
		File f = new File("/Users/Mathis/I4Racine/test.pdf");
		System.out.println("Path-->" + f.getParent());
		System.out.println("file--->" + f.getName());
		int idx = f.getName().lastIndexOf('.');
		System.out.println("extension--->" + ((idx > 0) ? f.getName().substring(idx) : "") );


							/*
					File f = new File(FILE_TO_RECEIVED);
					while(f.exists() && !f.isDirectory()) {
						System.out.println("Le fichier existe déja");
						FILE_TO_RECEIVED = FILE_TO_RECEIVED+"(" + numfile+ ")";
						numfile++;
					}
					*/
	}
}
