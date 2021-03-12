package su.stl.daar.egrep.kmp;

import su.stl.daar.egrep.itf.Command;

/**
 * 
 * @author mouloud fouzia
 *
 */
public class KMP implements Command {

	public KMP() {
	}

	public void execute(String text, String pattern) {
		KMPSearch(pattern, text);
	}

	private void KMPSearch(String pat, String txt) {
		int M = pat.length();
		int N = txt.length();

		// créer lps [] qui contiendra le plus lent suffixe de préfixe pour le modèle
		int lps[] = new int[M];
		int j = 0; // index for pat[]

		// Calculer lps
		computeLPSArray(pat, M, lps);

		int i = 0;
		while (i < N) {
			if (pat.charAt(j) == txt.charAt(i)) {
				j++;
				i++;
			}
			if (j == M) {
				System.out.println(txt);
				j = lps[j - 1];
				return;
			}

			// Discordance après j matchs
			else if (i < N && pat.charAt(j) != txt.charAt(i)) {
				// Ne pas matcher lps[0..lps[j-1]] charactères,
				if (j != 0)
					j = lps[j - 1];
				else
					i = i + 1;
			}
		}
	}

	private void computeLPSArray(String pat, int M, int lps[]) {
		// longueur du suffixe de préfixe le plus long
		int len = 0;
		int i = 1;
		lps[0] = 0; // lps[0] est toujours égal à 0

		// Calculer lps[i] for i = 1 to M-1
		while (i < M) {
			if (pat.charAt(i) == pat.charAt(len)) {
				len++;
				lps[i] = len;
				i++;
			} else // (pat[i] != pat[len])
			{
				if (len != 0) {
					len = lps[len - 1];

				} else {
					// if (len == 0)
					lps[i] = len;
					i++;
				}
			}
		}
	}

}