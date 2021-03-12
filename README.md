egrep :

Implémentation de 2 algorithmes :
- Regex Search : Rechercher toutes les chaines de caractères respectant un regex 
- KMP : Recherche toutes les chaine de caractères égales à un pattern dans un texte

A. Regex Search :

	1) La recherche est faites en 5 étapes distictes :
		a. Parser le regex pour obtenir un AST
		b. Transformer l'AST en automate à état final avec des epsilon-transitions
		c. Eliminer les epsilons transition pour obtenir un automate sans epsilon-transitions
		d. Minimiser l'automate obtenu après l'étape c
		e. Utiliser l'automate de l'étape d pour faire la recherche dans le texte
		
	2) Complexité temporelle :
		Cet algorithme fonctionne en deux temps :
		- Une premiere étape de pré-traitement: permet de construire un automate qui reconnait le langage exprimé par le regex
		- Une deuxième étape de traitemet : trouver toutes les occurences de chaines de caractères respectant le regex
		
		=> La complexité de la premiere partie est à négliger si la taille du regex en nombre de caractères est suffisament petit
		=> La complexitée de la deuxièmre étape est O(n), car chaque caractère du texte est traiter une et une seule fois en O(1)


B. KMP :
	https://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm
