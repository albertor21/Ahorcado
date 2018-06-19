package es.arvsoft.ahorcado;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game2 {
	
	private static Scanner entrada;

	public static void main(String[] args) {
		entrada = new Scanner (System.in);
		System.out.println("Elige castellano o English");
		System.out.println("0-Castellano   1-Inglés");
		int op = entrada.nextInt();
		entrada.nextLine(); //para vaciar el buffer del scanner
		String path;
		Ahorcado juego = new Ahorcado();
		if (op==0){
			path ="Esp.txt";
			juego.setDictionaryPath(path);
		}
		else {
			path = "Eng.txt";	
			juego.setDictionaryPath(path);
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
		for (int intento = 0; intento < 10; intento++){	
			System.out.print("tiene una ");	
			String letra = entrada.nextLine();
			//System.out.print("?");		
			String answer =  entrada.nextLine(); 			
			if (answer.startsWith("0")){
				System.out.print ("fallos: ");
				juego.setFails(letra.charAt(0));
				System.out.println(juego.getFails().toString());			
			} else {
				for (int ans: parsePositions(answer)){
					juego.asignarLetra(letra.charAt(0), ans-1);
				}		
			}
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
			System.out.println(juego.getRegExp(juego.getGuess()));
			System.out.println(juego.getGuess());
		}
		entrada.close();
		ArrayList<Query> lista = juego.getListOfSolutions(juego.getGuess(), 1);
		String solution = lista.get(0).getText();
		System.out.println("la palabra es: " + solution.toUpperCase());	

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
