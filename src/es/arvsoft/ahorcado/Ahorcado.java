package es.arvsoft.ahorcado;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ahorcado {

	private String word;
	private String guess;
	private int numLetters;
	private HashSet<Character> fails;
	private HashSet<Character> matches;
	private String dictPath; // path al diccionario de palabras
	private ArrayList<String> mostFrequent;
	BufferedReader br;

	Ahorcado() {
		fails = new HashSet<Character>();
		matches = new HashSet<Character>();
		mostFrequent = new ArrayList<String>();
	}

	Ahorcado(String word) {
		this();
		if (!(word == null)) {
			this.word = word;
			String guess = new String(new char[this.word.length()]).replace(
					'\0', '-');
			this.guess = guess;
		}
	}

	Ahorcado(int numLetters) {
		this();
		this.setNumLetters(numLetters);
	}

	public void setNumLetters(int numLetters) {
		this.numLetters = numLetters;
		String guess = new String(new char[numLetters]).replace('\0', '-');
		this.guess = guess;
	}

	public int getNumLetters() {
		return this.numLetters;
	}

	public String getWord() {
		return this.word;
	}

	public int getLength() {
		return this.word.length();
	}

	public void asignarLetra(char letra, int pos) {
		StringBuilder word = new StringBuilder(this.guess);
		word.setCharAt(pos, letra);
		this.guess = word.toString();
		matches.add(letra);
	}

	public String getDictionaryPath() {
		return this.dictPath;
	}

	public void setDictionaryPath(String path) {
		this.dictPath = path;
	}

	public String getGuess() {
		return this.guess;
	}

	public void guessLetter(char letter) {
		boolean found = false;
		for (int c = 0; c < this.word.length(); c++) {
			if (this.word.charAt(c) == letter) {
				asignarLetra(letter, c);
				found = true;
			}
		}
		if (!found)
			this.fails.add(letter);
	}

	public HashSet<Character> getMatches() {
		return matches;
	}

	public String getMatchesString() {
		String result = "";
		for (Character car : matches) {
			result = result + car;
		}
		return result;
	}

	public HashSet<Character> getFails() {
		return fails;
	}

	public String getFailsString() {
		String result = "";
		for (Character car : fails) {
			result = result + car;
		}
		return result;
	}

	public void setFails(Character letter) {
		fails.add(letter);
	}

	public void setFails(ArrayList<Character> letters) {
		fails.addAll(letters);
	}

	public int getNumFails() {
		return this.fails.size();
	}

	public int getNumMatches() {
		return this.matches.size();
	}

	public int getTries() {
		return getNumFails() + getNumMatches();
	}

	public void InitializeDictionary() throws FileNotFoundException {
		final int thFreq= 4;
		br = new BufferedReader(new FileReader(this.getDictionaryPath()));
		//se inicializa el array mostFrequent para albergar en orden aleatorio
		//las primeras thFreq letras mas frecuentes del diccionario
		//que estan guardadas en la tercera linea de dicho archivo
		try {
			br.readLine();
			br.readLine();
			String[] mostFreqArray = br.readLine().toLowerCase().split(",");
			for (int c = 0; c < thFreq; c++) {
				mostFrequent.add(mostFreqArray[c]);		
			}
		} catch (IOException e) {
			System.out.println("no encuentro el diccionario");
			e.printStackTrace();
		}
		Collections.shuffle(mostFrequent);
	}

	public String getRegExp(String guess) {
		String anyLetter = "[^";
		anyLetter = anyLetter + getMatchesString() + getFailsString();
		anyLetter = anyLetter + "]";
		String mod_search = guess.replace("-", ".");
		mod_search = mod_search.replace(".", anyLetter);
		mod_search = "^" + mod_search + "$";
		mod_search = mod_search.toLowerCase();
		return mod_search;
	}

	/*
	 * devuelve null si no encuentra ninguna palabra que cumpla el criteio o sea
	 * que no la tenga en el diccionario
	 */
	public ArrayList<Query> getListOfSolutions(String search, int maxHits) {
		ArrayList<Query> listOfSolutions = new ArrayList<Query>();
		String regExp = getRegExp(search);
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = null;
		int ocurr = 0;
		int lineNumber = 0;
		try {
			String line = br.readLine();
			lineNumber++;
			while (line != null) {
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					listOfSolutions.add(new Query(line, lineNumber));
					ocurr++;
					if (ocurr >= maxHits) {
						break;
					}
				}
				line = br.readLine();
				lineNumber++;
			}
		} catch (IOException e) {
			System.out.println("no encuentro el diccionario");
			e.printStackTrace();
		}
		return listOfSolutions;
	}

	public Character getNextGuess() {
		/*
		 * Primero se devuelven las thFreq primeras letras mas frecuentes del idioma
		 * seleccionado en orden aleatorio para dar idea de espontaneidad Se
		 * toman de la tercera linea del archivo del diccionario
		 */
		int n = getTries();
		if (n < mostFrequent.size()) {	
			Character result = mostFrequent.get(getTries()).charAt(0);
			return result;
		}
		/*
		 * A partir de aqui las siguientes sugerencias se obtienen asi: /*a
		 * partir de la lista de las N palabras que cumplen con el actual
		 * conjunto de Guesses (obtenidas con getListOfSolutions) se calcula la
		 * siguiente sugerencia de guess a partir de la frecuencia de letras de
		 * esta lista y NO de la frecuencia establecida para el idioma DEVUELVE
		 * Null si listOfSolutions is NULL
		 */
		final int MAX_HITS = 5;
		String search = this.getGuess();
		ArrayList<Query> list = getListOfSolutions(search, MAX_HITS);
		if (list.isEmpty())
			return null;
		// para cada palabra de la lista contar las ocurrencias de letras
		// excluyendo las letras ya acertadas que se obtienen con getmatches()
		ArrayList<Query> listOfChars = new ArrayList<Query>(); // reutilizo
		// Query
		for (Query q : list) {
			String word = q.getText();
			for (int c = 0; c < word.length(); c++) {
				Character character = word.charAt(c);
				HashSet<Character> matches = getMatches();
				// solo si es una letra no ya acertada contar letras
				if (!matches.contains(character)) {
					// si la letra esta ya en la lista aumentar su freq
					int pos = posCharinList(listOfChars, character);
					if (pos >= 0) {
						Query query = listOfChars.get(pos);
						int freq = query.getFreq();
						query.setFreq(++freq);
						listOfChars.remove(pos);
						listOfChars.add(query);

					} else { // no estaba en la lista añadirla con freq 1
						Query query = new Query(character.toString(), 1);
						listOfChars.add(query);
					}
				}
			}
		}
		if (list.size() == 2) {
			// solo conozco dos posibilidades
			// pruebo con una letra diferente
			// O SEA: coger la letra con menor frecuencia en el calculo que se
			// hace a continuacion
			// en vez de la de mas frecuencia
			Collections.sort(listOfChars, new Comparator<Query>() {
				@Override
				public int compare(Query q1, Query q2) {
					int result = 0;
					if (q1.getFreq() < q2.getFreq())
						result = -1;
					if (q1.getFreq() > q2.getFreq())
						result = 1;
					return result;
				}
			});
			return listOfChars.get(0).getText().charAt(0);
		}

		if (listOfChars.isEmpty())
			return null;
		Collections.sort(listOfChars, new Comparator<Query>() {
			@Override
			public int compare(Query q1, Query q2) {
				int result = 0;
				if (q1.getFreq() > q2.getFreq())
					result = -1;
				if (q1.getFreq() < q2.getFreq())
					result = 1;
				return result;
			}
		});
		return listOfChars.get(0).getText().charAt(0);
	}

	private int posCharinList(ArrayList<Query> list, char character) {
		for (int c = 0; c < list.size(); c++) {
			Query q = list.get(c);
			if (q.getText().charAt(0) == character)
				return c;
		}
		return -1;
	}


}
	

