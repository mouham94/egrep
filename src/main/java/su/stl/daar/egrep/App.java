package su.stl.daar.egrep;

import java.io.File;
import java.util.Scanner;

import su.stl.daar.egrep.itf.Command;
import su.stl.daar.egrep.kmp.KMP;
import su.stl.daar.egrep.naive.Naive;
import su.stl.daar.egrep.regex.Regex;

public class App {

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println("Arguments : (naive|regex|kmp) (File_name) (Pattern)");
			System.exit(1);
		}
		
		Scanner sc = new Scanner(new File(args[1]));
		
		Command algo = null;
		
		switch (args[0]) {
		case "naive":
			algo = new Naive();
			break;
		case "regex":
			algo = new Regex();
			break;
		case "kmp":
			algo = new KMP();
			break;
			default:
				System.out.println(args[0] + ": Cette command n'existe\nVeuillez choisir entre (naive|regex|kmp)");
				System.exit(1);
		}
		
		while (sc.hasNext()) {
			String text = sc.nextLine();
			algo.execute(text, args[2]);
		}
		
		sc.close();

	}
}

/* Permet de tester les performances de chaque algorithme
    
    Scanner sc = new Scanner(new File(args[0]));

	ArrayList<String> data = new ArrayList<String>();
  
 	while (sc.hasNext()) {
		data.add(sc.nextLine());
	}

	PrintWriter writer = new PrintWriter("regex.data");

	for (int i = 0; i < data.size(); i++) {
		int size = 0;
		long begin = System.currentTimeMillis();
		for (int j = 0; j <= i; j++) {
			(Regex|KMP|Naive).execute(data.get(j), "abcdefghij");
			size += data.get(j).length();
		}
		long end = System.currentTimeMillis();
		writer.println(size + "\t" + ((double) (end - begin)));
	}
*/
