package es.arvsoft.ahorcado;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameBatch {

	public static void main(String[] args) {
		
		ArrayList<String> listaPrueba = new ArrayList<String>();
		try{
			listaPrueba = getPalabrasPrueba();
		} catch (FileNotFoundException e) {
			System.out.println("no encuentro el diccionario");
			e.printStackTrace();
		}
		double t0 = System.nanoTime(); 
		System.out.println("Resultado,Palabra,intentos,fallos");
		for (int c = 0 ; c< listaPrueba.size();c++){
			
		Ahorcado juego = new Ahorcado(listaPrueba.get(c));
		String path = "Esp.txt";
		juego.setDictionaryPath(path);
		try{
			juego.InitializeDictionary();
		} catch (FileNotFoundException e) {
			System.out.println("no encuentro el diccionario");
			e.printStackTrace();
		}
		boolean seguir= true;
		boolean acierto = false;
		while (seguir){
			Character car = juego.getNextGuess();
			if (car==null){
				//System.out.println("No conozco ninguna palabra con estas letras");
				//System.out.println("TU GANAS");
				seguir = false;
				break;
			}
			juego.guessLetter(car);
			ArrayList<Query> lista = juego.getListOfSolutions(juego.getGuess(), 5);
			if (juego.getNumFails()>5){
				//se la juega a la primera palabra de listOfSolutions
				if (lista.get(0).getText().equalsIgnoreCase(juego.getWord())){
					//System.out.print("ACIERTO,");
					//System.out.print(lista.get(0).getText());
					acierto= true;
				}else{
					//(System.out.print("FALLO,");
					//System.out.print(juego.getWord());
				}
				break;
			}
			if (juego.getWord().equalsIgnoreCase(juego.getGuess())){
				seguir= false;
				//System.out.print("ACIERTO,");
				acierto = true;
				//System.out.print(juego.getWord());
				break;
			}
			if (lista.size()==1){
				seguir = false;
				if (lista.get(0).getText().equalsIgnoreCase(juego.getWord())){
					//System.out.print("ACIERTO,");
					//System.out.print(lista.get(0).getText());
					acierto= true;
				}else{
					//(System.out.print("FALLO,");
					//System.out.print(juego.getWord());
				}
				break;
			}	
		}
		if (juego.getNumFails()>6) acierto=false; 
		if (acierto){
			System.out.print("ACIERTO,");
		}else{
			System.out.print("FALLO,");
		}
		System.out.print(juego.getWord());
		System.out.print("," + juego.getTries());	
		System.out.println("," + juego.getNumFails());	
		
		}//end for
		double t1 = System.nanoTime()-t0;
		t1 = t1/1000000000.0;
		System.out.println("Tiempo(s): "+t1);
	}

	public static ArrayList<String> getPalabrasPrueba() throws FileNotFoundException{
		Scanner scanner = new Scanner(new File("listado1000Esp.txt"));
		ArrayList<String> result = new ArrayList<String>();
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			result.add(line);
		}
		scanner.close();
		return result;
	}

}
