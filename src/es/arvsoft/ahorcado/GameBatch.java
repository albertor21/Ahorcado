package es.arvsoft.ahorcado;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GameBatch {

	public static void main(String[] args) {

		StringBuilder sb = new StringBuilder();
		ArrayList<String> listaPrueba = new ArrayList<String>();
		try {
			listaPrueba = getPalabrasPrueba();
		} catch (IOException e) {
			System.out.println("no encuentro el diccionario");
			e.printStackTrace();
		}
		double t0 = System.nanoTime();
		System.out.println("Resultado,Palabra,intentos,fallos");
		for (int c = 0; c < listaPrueba.size(); c++) {
			Ahorcado juego = new Ahorcado(listaPrueba.get(c));
			String path = "Esp.txt";
			juego.setDictionaryPath(path);
			try {
				juego.InitializeDictionary();
			} catch (FileNotFoundException e) {
				System.out.println("no encuentro el diccionario");
				e.printStackTrace();
			}
			boolean seguir = true;
			boolean acierto = false;
			while (seguir) {
				Character car = juego.getNextGuess();
				if (car == null) {
					// System.out.println("No conozco ninguna palabra con estas letras");
					// System.out.println("TU GANAS");
					seguir = false;
					break;
				}
				juego.guessLetter(car);
				ArrayList<Query> lista = juego.getListOfSolutions(
						juego.getGuess(), 5);
				if (juego.getNumFails() > 5) {
					// se la juega a la primera palabra de listOfSolutions
					if (lista.get(0).getText()
							.equalsIgnoreCase(juego.getWord())) {
						// System.out.print("ACIERTO,");
						acierto = true;
					} else {
						// (System.out.print("FALLO,");
					}
					break;
				}
				if (juego.getWord().equalsIgnoreCase(juego.getGuess())) {
					seguir = false;
					// System.out.print("ACIERTO,");
					acierto = true;
					// System.out.print(juego.getWord());
					break;
				}
				if (lista.size() == 1) {
					seguir = false;
					if (lista.get(0).getText()
							.equalsIgnoreCase(juego.getWord())) {
						// System.out.print("ACIERTO,");
						acierto = true;
					} else {
						// (System.out.print("FALLO,");
					}
					break;
				}
			}
			if (juego.getNumFails() > 6)
				acierto = false;
			if (acierto) {
				sb.append("ACIERTO\t");
			} else {
				sb.append("FALLO\t");
			}
			sb.append(juego.getWord());
			System.out.println(juego.getWord());
			sb.append("\t" + juego.getTries());
			sb.append("\t" + juego.getNumFails());
			sb.append("\n");
		}// end for
		double t1 = System.nanoTime() - t0;
		t1 = t1 / 1000000000.0;
		sb.append("Tiempo(s)");
		sb.append(t1 + "\n");
		System.out.println(sb.toString());
	}

	public static ArrayList<String> getPalabrasPrueba() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				"15000_16000Esp.txt"));
		ArrayList<String> result = new ArrayList<String>();
		try {
			String line = br.readLine();
			result.add(line);
			while (line != null) {
				line = br.readLine();
				if (line != null)
					result.add(line);
			}
		} catch (IOException e) {
			System.out.println("no encuentro el diccionario");
			e.printStackTrace();
		} finally {
			br.close();
		}
		return result;
	}

}