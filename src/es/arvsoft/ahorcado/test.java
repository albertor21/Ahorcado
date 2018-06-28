package es.arvsoft.ahorcado;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class test {

	public static void main(String[] args) {
		Ahorcado juego = new Ahorcado("balada");
		String path = "Esp.txt";
		juego.setDictionaryPath(path);
		try {
			juego.InitializeDictionary();
		} catch (FileNotFoundException e) {
			System.out.println("no encuentro el diccionario");
			e.printStackTrace();
		}
		System.out.println(juego.getLength());
		boolean seguir = true;
		while (seguir) {
			Character car = juego.getNextGuess();
			
			if (car == null) {
				System.out
						.println("No conozco ninguna palabra con estas letras");
				System.out.println("TU GANAS");
				System.out.println("Pues era " + juego.getWord());
				seguir = false;
				break;
			}
			System.out.print("tiene una ");
			System.out.println(car + "?");
			juego.guessLetter(car);
			System.out.println(juego.getGuess());
			HashSet<Character> letrasFalladas = juego.getFails();
			Iterator<Character> it = letrasFalladas.iterator();
			System.out.print("letras falladas: ");
			while (it.hasNext()) {
				Character letra = it.next();
				System.out.print(letra + ", ");
			}
			System.out.println(juego.getNumFails());
			if (juego.getWord().equalsIgnoreCase(juego.getGuess())) {
				seguir = false;
				System.out.println("¡¡¡¡ACERTE!!!!");
				System.out
						.println("la palabra escondida es " + juego.getWord());
				break;
			}
			ArrayList<Query> lista = juego.getListOfSolutions(juego.getGuess(),5);
			if (juego.getNumFails() > 5) {
				// se la juega a la primera palabra de listOfSolutions
				System.out.println("me la juego, es:");
				System.out.println(lista.get(0).getText());
				if (lista.get(0).getText()
						.equalsIgnoreCase(juego.getWord())) {
					 System.out.print("ACIERTO,");
				} else {
					System.out.print("FALLO,");
				}
				break;
			}
			if (lista.size() == 1) {
				System.out.println("Ya sé la palabra.");
				System.out.println("Es " + lista.get(0).getText());
				seguir = false;
				break;
			}
			if (lista.size() == 2) {
				System.out.println("Estoy dudando entre");
				System.out.println(lista.get(0).getText() + " y "
						+ lista.get(1).getText());
			}
		}
		System.out.println("He realizado " + juego.getTries() + " intentos");
	}

}
