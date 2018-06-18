package es.arvsoft.ahorcado;

public class Query implements Comparable<Query>{
	
	private String text;
	private int freq;
	/* Construye sin parámetros */  
	public Query(){
		this.text = null;
		this.freq = 0;
	}
	
	/* Construye una nueva query con el texto pasado como parámetro */  
	public Query(String text){
		this.text = text;
		this.freq = 1;		
	}
	
	public Query(String text, int freq) {
		this.text = text;
		this.freq = freq;		
	}

	/* Devuelve la frecuencia de una query */  
	 public int getFreq(){
		 return this.freq; 
	 }

	/* Modifica la frecuencia de la query */  
	public void setFreq(int freq){
		this.freq = freq;	
	}
	
	/* incrementa la frecuencia de la query */ 
	public void incFreq(){
		this.freq++ ;		
	}
	
	/* Devuelve el texto de una query */  
	public String getText(){
		return this.text;
	}
		 
	 @Override
	 public boolean equals(Object o){
		 Query query = (Query) o;
		 return (this.text.equals(query.text));
	 }
	 	    
	 @Override
	 //compara por frecuencia
	 public int compareTo(Query o)
	 {
	     int resultado;
	     if (this.freq < o.freq) {resultado = -1;}
	     else  { 
	    	 resultado = 1;
	    	 }
	     return resultado;
	}
	 
	@Override
	public String toString()
	{
		return this.text + ":" + this.freq;
	}
}
