package su.stl.daar.egrep.regex;

import su.stl.daar.egrep.consts.Consts;
import su.stl.daar.egrep.itf.Command;
import su.stl.daar.egrep.model.Automata;
import su.stl.daar.egrep.model.RegExTree;

public class Regex implements Command {

	private Automata a = null;

	public Regex() {
	}
	
	public void execute(String text, String regex) {

		if (a == null) {
			a = makeAutomata(regex);
		}

		if (exist(text, a)) {
			System.out.println(text);
		}

	}

	/**
	 * Tester si le pattern existe dans line
	 * @param line
	 * @param a
	 * @return
	 */
	private boolean exist(String line, Automata a) {

		int i = 0;
		int nextState = a.initialState;

		while (i < line.length()) {

			if ((int) line.charAt(i) > 255) {
				nextState = a.initialState;
			} else {

				if (a.transitions[nextState][line.charAt(i)].size() > 0) {
					nextState = a.transitions[nextState][line.charAt(i)].get(0);
				} else {
					nextState = a.initialState;
				}
			}

			if (a.isFinalState[nextState]) {
				return true;
			}

			i++;

		}

		return false;
	}

	/**
	 * Créer un nouvel automate à partir de l'AST étape par étape
	 * @param regex
	 * @return automate déterministe minimum
	 */
	private Automata makeAutomata(String regex) {
		try {
			RegExTree tree = RegExTree.parse(regex);

			Automata nfa = makeNFA(tree);

			Automata nfa2 = nfa.eliminateEpsilon();

			Automata dfa = nfa2.determinize();

			Automata dfa_com = dfa.complete();

			Automata dfa_min = dfa_com.minimization();

			return dfa_min;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/* Begin Make NFA */
	private Automata makeNFA(RegExTree tree) {
		switch (tree.root) {
		case Consts.ALTERN:
			return makeAltNFA(tree);
		case Consts.ETOILE:
			return makeClosNFA(tree);
		case Consts.CONCAT:
			return makeConcNFA(tree);
		default:
			return makeAtomNFA(tree);
		}
	}

	private Automata makeAltNFA(RegExTree tree) {
		Automata a0 = makeNFA(tree.subTrees.get(0));
		Automata a1 = makeNFA(tree.subTrees.get(1));
		return a0.union(a1);
	}

	private Automata makeClosNFA(RegExTree tree) {
		Automata a = makeNFA(tree.subTrees.get(0));
		return a.closure();
	}

	private Automata makeConcNFA(RegExTree tree) {
		Automata a0 = makeNFA(tree.subTrees.get(0));
		Automata a1 = makeNFA(tree.subTrees.get(1));
		return a0.concat(a1);
	}

	private Automata makeAtomNFA(RegExTree tree) {
		return Automata.simpleTemplateAutomata(tree.root);
	}
	/* End make NFA */

}
