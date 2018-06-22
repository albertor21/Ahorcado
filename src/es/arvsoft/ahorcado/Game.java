package es.arvsoft.ahorcado;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
	
private static Scanner entrada;

	public static void main(String[] args) {
		entrada = new Scanner (System.in);
		System.out.println("Elige castellano o English");
		System.out.println("0-Castellano   1-Inglés");
		int op = entrada.nextInt();
		entrada.nextLine(); //para vaciar el buffer del scanner
		char [] letters;
		String path;
		Ahorcado juego = new Ahorcado();
		if (op==0){
			path = "./Esp.txt";
			juego.setDictionaryPath(path);
			letters = new char[]  {'E', 'A', 'O', 'S', 'N', 'R', 'I', 'L','D'};		
		}
		else{
			path = "./Eng.txt";
			juego.setDictionaryPath(path);
			letters = new char[]  {'E', 'T', 'A', 'O', 'I', 'N', 'S', 'H','R', 'D', 'L','U'};
		}
		try {
			juego.InitializeDictionary();
		} catch (FileNotFoundException e) {
			System.out.println("no encuentro el diccionario");
			e.printStackTrace();
		}
		entrada = new Scanner (System.in);
		System.out.println("Piensa una palabra");
		System.out.println("Cuantas letras tiene?");
		int numLetras = entrada.nextInt();
		entrada.nextLine(); //para vaciar el buffer del scanner
		juego.setNumLetters(numLetras);
		System.out.println(juego.getGuess());
		for (int c = 0; c < letters.length; c++){	
			System.out.print("tiene una ");	
			//System.out.println(letters[c]);
			Character car = juego.getNextGuess();
			System.out.println(car);
			String answer =  entrada.nextLine(); 			
			if (answer.startsWith("0")){
				//juego.setFails(letters[c]);
				juego.setFails(car);
			} else {
				for (int ans: parsePositions(answer)){
					//juego.asignarLetra(letters[c], ans-1);
					juego.asignarLetra(car, ans-1);
				}		
			}
			System.out.print ("fallos: ");
			System.out.println(juego.getFails().toString());	
			String regexp = juego.getGuess();
			ArrayList<Query> lista = juego.getListOfSolutions(regexp, 5);
			if (lista.size()==1){
				String solution = lista.get(0).getText();
				System.out.println("la palabra es: " + solution.toUpperCase());
				return;
			}
			for (Query item : lista){
				System.out.println(item);
			}
			//System.out.println(juego.getRegExp(juego.getGuess()));
			System.out.println(juego.getGuess());
		}		
		ArrayList<Query> lista = juego.getListOfSolutions(juego.getGuess(), 1);
		String solution = lista.get(0).getText();
		System.out.println("la palabra es: " + solution.toUpperCase());	
		int op2 = entrada.nextInt();
		entrada.close();
	}
		
	private static int [] parsePositions(String answer){
		String [] answers = answer.split(" ");
		int[] result = new int[answers.length];
		for (int c= 0; c < answers.length; c++){
			result[c]= Integer.parseInt(answers[c]);		
		}	
		return result;
	}

}