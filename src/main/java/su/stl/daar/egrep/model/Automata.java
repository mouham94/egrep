package su.stl.daar.egrep.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import su.stl.daar.egrep.consts.Consts;

/**
 * 
 * @author mouloud Fouzia
 *
 */
public class Automata {

	/**
	 * Attributs d'un automate
	 */
	public static int counter;
	public int identifier;
	public int nbreStates;
	public int initialState;
	public boolean[] isFinalState;
	public StatesList[][] transitions;

	public Automata(int nbreStates, int initialState, boolean[] isFinalState, StatesList[][] transitions) {
		this.nbreStates = nbreStates;
		this.identifier = Automata.counter++;
		this.initialState = initialState;
		this.isFinalState = isFinalState;
		this.transitions = transitions;
	}

	/**
	 * Produire un automate simple
	 * @param label
	 * @return [ 0 --label--> 1 ]
	 */
	public static Automata simpleTemplateAutomata(int label) {
		int nbreStates = 2;
		int initialState = 0;
		boolean[] isFinalState = { false, true };
		StatesList[][] transitions = new StatesList[nbreStates][Consts.CARD_ASCII];
		for (int i = 0; i < nbreStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				transitions[i][j] = new StatesList();
			}
		}
		transitions[0][label].add(1);
		return new Automata(nbreStates, initialState, isFinalState, transitions);
	}

	/**
	 * Union de deux automates
	 * @param a
	 * @return this U a
	 */
	public Automata union(Automata a) {

		// Number Of States
		int numberOfStates = this.nbreStates + a.nbreStates + 2;

		// Initial State
		int initialState = 0;

		// Final States
		boolean[] isFinalState = new boolean[numberOfStates];
		for (int i = 0; i < numberOfStates; i++) {
			isFinalState[i] = false;
		}
		isFinalState[numberOfStates - 1] = true;

		// Transitions
		StatesList[][] transitions = new StatesList[numberOfStates][Consts.CARD_ASCII];
		for (int i = 0; i < numberOfStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				transitions[i][j] = new StatesList();
			}
		}

		// Copie left automata transitions to transitions
		for (int i = 0; i < this.nbreStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				for (int k = 0; k < this.transitions[i][j].size(); k++) {
					transitions[i + 1][j].add(this.transitions[i][j].get(k) + 1);
				}
			}
		}

		// Copie right automata transitions to transitions
		for (int i = 0; i < a.nbreStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				for (int k = 0; k < a.transitions[i][j].size(); k++) {
					transitions[i + this.nbreStates + 1][j].add(a.transitions[i][j].get(k) + this.nbreStates + 1);
				}
			}
		}

		// Ajouter les epsilons transitions
		transitions[0][0].add(1);
		transitions[0][0].add(this.nbreStates + 1);

		for (int i = 0; i < this.nbreStates; i++) {
			if (this.isFinalState[i]) {
				transitions[i + 1][0].add(numberOfStates - 1);
			}
		}
		for (int i = 0; i < a.nbreStates; i++) {
			if (a.isFinalState[i]) {
				transitions[i + this.nbreStates + 1][0].add(numberOfStates - 1);
			}
		}

		return new Automata(numberOfStates, initialState, isFinalState, transitions);
	}

	/**
	 * Closure d'un automate
	 * @return this*
	 */
	public Automata closure() {

		// Number Of States
		int numberOfStates = this.nbreStates + 2;

		// Initial State
		int initialState = 0;

		// Final States
		boolean[] isFinalState = new boolean[numberOfStates];
		for (int i = 0; i < numberOfStates; i++) {
			isFinalState[i] = false;
		}
		isFinalState[numberOfStates - 1] = true;

		// Transitions
		StatesList[][] transitions = new StatesList[numberOfStates][Consts.CARD_ASCII];
		for (int i = 0; i < numberOfStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				transitions[i][j] = new StatesList();
			}
		}

		// Copie automata transitions to transitions
		for (int i = 0; i < this.transitions.length; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				for (int k = 0; k < this.transitions[i][j].size(); k++) {
					transitions[i + 1][j].add(this.transitions[i][j].get(k) + 1);
				}
			}
		}

		// Add epsilon transitions
		transitions[0][0].add(1);
		transitions[0][0].add(numberOfStates - 1);
		for (int i = 0; i < this.nbreStates; i++) {
			if (this.isFinalState[i]) {
				transitions[i + 1][0].add(1);
				transitions[i + 1][0].add(numberOfStates - 1);
			}
		}

		return new Automata(numberOfStates, initialState, isFinalState, transitions);
	}

	/**
	 * Concaténer deux automates
	 * @param a
	 * @return this.a
	 */
	public Automata concat(Automata a) {

		// Number Of States
		int numberOfStates = this.nbreStates + a.nbreStates;

		// Initial State
		int initialState = 0;

		// Final States
		boolean[] isFinalState = new boolean[numberOfStates];
		for (int i = 0; i < numberOfStates; i++) {
			isFinalState[i] = false;
		}
		isFinalState[numberOfStates - 1] = true;

		// Transitions
		StatesList[][] transitions = new StatesList[numberOfStates][Consts.CARD_ASCII];
		for (int i = 0; i < numberOfStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				transitions[i][j] = new StatesList();
			}
		}

		// Copie left automata transitions to transitions
		for (int i = 0; i < this.nbreStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				transitions[i][j].addAll((StatesList) this.transitions[i][j].clone());
			}
		}

		// Copie right automata transitions to transitions
		for (int i = 0; i < a.nbreStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				for (int k = 0; k < a.transitions[i][j].size(); k++) {
					transitions[i + this.nbreStates][j].add(a.transitions[i][j].get(k) + this.nbreStates);
				}
			}
		}

		// Add epsilon transtions
		for (int i = 0; i < this.nbreStates; i++) {
			if (this.isFinalState[i]) {
				transitions[i][0].add(a.initialState + this.nbreStates);
			}
		}

		return new Automata(numberOfStates, initialState, isFinalState, transitions);
	}

	/**
	 * Eliminer les epsilons transitions
	 * @return NFA sans epsilons transitions
	 */
	public Automata eliminateEpsilon() {

		// Calculate epsilon transitions for all states
		StatesList[] epsTrans = new StatesList[this.nbreStates];
		for (int i = 0; i < this.nbreStates; i++) {
			epsTrans[i] = new StatesList();
		}
		boolean[] mark = new boolean[this.nbreStates];
		for (int i = 0; i < this.nbreStates; i++) {
			Queue<Integer> queue = new LinkedList<Integer>();
			for (int j = 0; j < this.nbreStates; j++) {
				mark[j] = false;
			}
			queue.add(i);
			mark[i] = true;
			while (!queue.isEmpty()) {
				int v = (Integer) queue.remove();
				epsTrans[i].add(v);
				for (Integer j : this.transitions[v][0]) {
					if (!mark[j]) {
						queue.add(j);
						mark[j] = true;
					}
				}
			}
		}

		// Générer le nouvel automate 
		int nbreStates = this.nbreStates;
		int initialState = this.initialState;
		boolean[] isFinalState = new boolean[nbreStates];
		for (int i = 0; i < nbreStates; i++) {
			if (this.isFinalState[i]) {
				isFinalState[i] = true;
			} else {
				boolean isFinal = false;
				for (Integer j : epsTrans[i]) {
					if (this.isFinalState[j]) {
						isFinal = true;
						break;
					}
				}
				isFinalState[i] = isFinal;
			}
		}

		StatesList[][] transitions = new StatesList[nbreStates][Consts.CARD_ASCII];
		for (int i = 0; i < nbreStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				transitions[i][j] = new StatesList();
			}
		}

		for (int i = 0; i < nbreStates; i++) {
			for (int j = 1; j < Consts.CARD_ASCII; j++) {
				for (Integer k : epsTrans[i]) {
					transitions[i][j].removeAll((StatesList) this.transitions[k][j].clone());
					transitions[i][j].addAll((StatesList) this.transitions[k][j].clone());
				}
			}
		}

		// Delete state with null negative degree

		return new Automata(nbreStates, initialState, isFinalState, transitions);
	}

	/**
	 * Déterminier un automate
	 * @return DFA
	 */
	public Automata determinize() {

		Queue<String> queue = new LinkedList<String>();
		HashMap<String, Integer> map = new HashMap<>();
		ArrayList<StatesList[]> trans = new ArrayList<StatesList[]>();
		boolean[] isFinalState = new boolean[nbreStates];
		for (int i = 0; i < isFinalState.length; i++) {
			isFinalState[i] = false;
		}

		int id = 0;
		queue.add("0");
		map.put("0", id++);

		while (!queue.isEmpty()) {
			String str = queue.remove();

			int s = map.get(str);

			trans.add(new StatesList[Consts.CARD_ASCII]);

			for (int i = 0; i < Consts.CARD_ASCII; i++) {
				trans.get(s)[i] = new StatesList();
			}

			StatesList states = stringToListStates(str);
			for (int i = 0; i < Consts.CARD_ASCII; i++) {
				StatesList next = new StatesList();
				for (Integer state : states) {
					next.removeAll((StatesList) this.transitions[state][i].clone());
					next.addAll((StatesList) this.transitions[state][i].clone());
				}
				if (next.size() == 0) {
					trans.get(s)[i] = new StatesList();
					continue;
				}
				next.sort(new Comparator<Integer>() {
					@Override
					public int compare(Integer arg0, Integer arg1) {
						if (arg0 < arg1)
							return -1;
						if (arg0 > arg1)
							return 1;
						return 0;
					}
				});
				String nextState = listStatesToString(next);
				if (!map.containsKey(nextState)) {
					map.put(nextState, id);
					queue.add(nextState);
					boolean isFinal = false;
					for (int j = 0; j < next.size(); j++) {
						if (this.isFinalState[next.get(j)]) {
							isFinal = true;
							break;
						}
					}
					isFinalState[id] = isFinal;
					id++;
				}
				int nxtState = map.get(nextState);
				trans.get(s)[i] = new StatesList();
				trans.get(s)[i].add(nxtState);
			}
		}
		StatesList[][] trans2 = new StatesList[trans.size()][Consts.CARD_ASCII];
		for (int i = 0; i < trans2.length; i++) {
			trans2[i] = trans.get(i);
		}

		boolean[] isFinalState2 = new boolean[trans2.length];
		for (int i = 0; i < isFinalState2.length; i++) {
			isFinalState2[i] = isFinalState[i];
		}

		return new Automata(trans2.length, 0, isFinalState2, trans2);
	}

	/**
	 * Completer l'automate (En ajoutant un état puits)
	 * @return Automate complet
	 */
	public Automata complete() {
		// Number of states
		int nbreStates = this.nbreStates + 1;

		// Etat puits
		int well = nbreStates - 1;

		// Final states
		boolean[] isFinalState = new boolean[nbreStates];
		for (int i = 0; i < this.isFinalState.length; i++) {
			isFinalState[i] = this.isFinalState[i];
		}
		isFinalState[well] = false;

		// Transitions
		StatesList[][] transitions = new StatesList[nbreStates][Consts.CARD_ASCII];
		for (int i = 0; i < nbreStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				transitions[i][j] = new StatesList();
			}
		}

		for (int i = 0; i < this.nbreStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {

				if (this.transitions[i][j].size() == 0) {
					transitions[i][j].add(well);
				} else {
					transitions[i][j].addAll((StatesList) this.transitions[i][j].clone());
				}

			}
		}

		for (int j = 0; j < Consts.CARD_ASCII; j++) {
			transitions[well][j].add(well);
		}

		return new Automata(nbreStates, this.initialState, isFinalState, transitions);
	}

	/**
	 * Minimiser un automate
	 * @return Automate minimum
	 */
	public Automata minimization() {

		int[] states = new int[this.nbreStates];
		ArrayList<StatesList> ens = new ArrayList<StatesList>();
		ens.add(new StatesList());
		ens.add(new StatesList());

		// Diviser en deux sous ensembles : Finaux et non finaux
		for (int i = 0; i < this.nbreStates; i++) {
			if (this.isFinalState[i]) {
				states[i] = 1;
				ens.get(1).add(i);
			} else {
				states[i] = 0;
				ens.get(0).add(i);
			}
		}

		boolean dec = true;

		while (dec) {

			dec = false;

			for (int i = 0; i < ens.size(); i++) {
				for (int c = 0; c < Consts.CARD_ASCII; c++) {
					StatesList nextEns = new StatesList();
					for (Integer state : ens.get(i)) {
						int next = this.transitions[state][c].get(0);
						nextEns.add(states[next]);
					}
					if (!estHomogene(nextEns)) {
						decomposer(ens, i, states, nextEns);
						dec = true;
						break;
					}
				}
			}

		}

		// Construire le nouvel automate minimal
		// Number of states
		int nbreStates = ens.size();

		// Initial state
		int initialState = states[this.initialState];

		// Final States
		boolean[] isFinalState = new boolean[nbreStates];
		for (int i = 0; i < isFinalState.length; i++) {
			isFinalState[i] = false;
		}
		for (int i = 0; i < this.isFinalState.length; i++) {
			isFinalState[states[i]] = this.isFinalState[i];
		}

		// Transitions
		StatesList[][] transitions = new StatesList[nbreStates][Consts.CARD_ASCII];
		for (int i = 0; i < nbreStates; i++) {
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				transitions[i][j] = new StatesList();
			}
		}

		for (int i = 0; i < ens.size(); i++) {
			for (int c = 0; c < Consts.CARD_ASCII; c++) {
				if (this.transitions[ens.get(i).get(0)][c].size() > 0) {
					transitions[i][c].add(states[this.transitions[ens.get(i).get(0)][c].get(0)]);
				}
			}
		}

		// Trouver l'état puit
		int well = 0;
		for (int i = 0; i < nbreStates; i++) {
			if (initialState != i && !isFinalState[i]) {
				boolean isWell = true;
				int nextState = transitions[i][0].get(0);
				if (nextState != i) {
					isWell = false;
				} else {
					for (int c = 1; c < Consts.CARD_ASCII; c++) {
						if (nextState != transitions[i][c].get(0)) {
							isWell = false;
							break;
						}
					}
				}
				if (isWell) {
					well = nextState;
					break;
				}
			}
		}

		// Supprimer l'état puit
		for (int i = 0; i < nbreStates; i++) {
			for (int c = 0; c < Consts.CARD_ASCII; c++) {
				if (transitions[i][c].get(0) == well) {
					transitions[i][c] = new StatesList();
				}
			}
		}

		return new Automata(nbreStates, initialState, isFinalState, transitions);
	}

	/**
	 * Décomposer un ensemble d'états en plusieurs sous-ensembles
	 * @param ens
	 * @param k
	 * @param states
	 * @param nextEns
	 */
	private void decomposer(ArrayList<StatesList> ens, int k, int[] states, StatesList nextEns) {

		// Calculer le groupe d'indice maximum
		int max = Collections.max(nextEns);

		// Créer groups de taille max + 1
		StatesList[] groups = new StatesList[max + 1];
		for (int i = 0; i < groups.length; i++) {
			groups[i] = new StatesList();
		}

		// Décomposer en sous-ensembles
		for (int i = 0; i < ens.get(k).size(); i++) {
			groups[nextEns.get(i)].add(ens.get(k).get(i));
		}

		// Copier les groupes dans newEns
		ArrayList<StatesList> newEns = new ArrayList<StatesList>();
		for (int i = 0; i < groups.length; i++) {
			if (groups[i].size() > 0) {
				newEns.add(groups[i]);
			}
		}

		// Fusionner newEns et ens
		ens.set(k, newEns.get(0));
		ens.addAll(newEns);

		// Mettre à jour states
		for (int i = 0; i < ens.size(); i++) {
			for (int j = 0; j < ens.get(i).size(); j++) {
				states[ens.get(i).get(j)] = i;
			}
		}

	}

	/**
	 * Tester si un tableau est constant
	 * @param list
	 * @return true si le tableau est constant, faux sinon
	 */
	private boolean estHomogene(StatesList list) {
		if (list.size() > 0) {
			int first = list.get(0);
			for (int i = 1; i < list.size(); i++) {
				if (first != list.get(i)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * String to liste d'états
	 * @param string
	 * @return
	 */
	private StatesList stringToListStates(String string) {
		String[] tab = string.split(",");
		StatesList states = new StatesList();
		for (String str : tab) {
			states.add(Integer.parseInt(str));
		}
		return states;
	}

	/**
	 * Liste d'état to string
	 * @param states
	 * @return
	 */
	public String listStatesToString(StatesList states) {
		String str = states.get(0) + "";
		for (int i = 1; i < states.size(); i++) {
			str += "," + states.get(i);
		}
		return str;
	}

	@Override
	public String toString() {
		String str = "S" + this.identifier + " = \n";
		str += " N = " + this.nbreStates + "\n";
		str += " s0 = " + this.initialState + "\n";
		str += " F = { ";
		for (int i = 0; i < this.nbreStates; i++) {
			if (this.isFinalState[i]) {
				str += i + " ";
			}
		}
		str += "}\n";
		str += " T = ";
		for (int i = 0; i < this.nbreStates; i++) {
			String[] labels = new String[this.nbreStates];
			for (int j = 0; j < Consts.CARD_ASCII; j++) {
				for (int k = 0; k < this.transitions[i][j].size(); k++) {
					if (labels[this.transitions[i][j].get(k)] == null) {
						labels[this.transitions[i][j].get(k)] = j + "";
					} else {
						labels[this.transitions[i][j].get(k)] += "," + j;
					}
				}
			}
			str += "\t";
			for (int j = 0; j < this.nbreStates; j++) {
				if (labels[j] == null) {
					str += "--\t";
				} else {
					str += labels[j] + "\t";
				}
			}
			str += "\n";
		}
		return str;
	}

}