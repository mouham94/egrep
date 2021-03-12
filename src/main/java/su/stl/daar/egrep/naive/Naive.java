package su.stl.daar.egrep.naive;

import su.stl.daar.egrep.itf.Command;

/**
 * 
 * @author mouloud fouzia
 *
 */
public class Naive implements Command {

	/**
	 * A chaque décalage, on test si le pattern = T[s, s+m]
	 * @param text
	 * @param pattern
	 */
	public void execute(String text, String pattern) {

		for (int i = 0; i < text.length() - pattern.length(); i++) {
			if (text.substring(i, i + pattern.length()).equals(pattern)) {
				System.out.println(text);
				return;
			}
		}

	}

	public Naive() {
	}

}
