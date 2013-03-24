import java.util.ArrayList;


public class MainTest {
	public static void main(String[] args)
	{
		new Directory("ok");
		File FileB = new File("B",FileType.TXT);
		File FileC = new File("C",FileType.TXT);
		File FileD = new File("D",FileType.TXT);
		File FileE = new File("E",FileType.TXT);
		File FileF = new File("F",FileType.TXT);
		Directory D = new Directory("ok");
		FileB.move(D);
		FileC.move(D);
		FileD.move(D);
		FileE.move(D);
		FileF.move(D);
		System.out.println(D.getNbItems());
		//System.out.println(D.getItem("F"));;
		
		
	}
}
