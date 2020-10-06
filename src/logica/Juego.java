package logica;
import java.io.*;
import java.util.Random;

public class Juego {
	private Celda [][] tablero;
	private int cantFilas;
	private int cantCeldasAEliminar;
	private boolean solucionValida;
	
	public Juego() {
		this.cantCeldasAEliminar = 45;
		this.solucionValida = true;
		this.cantFilas = 9;
		this.tablero = new Celda[this.cantFilas][this.cantFilas];
		inicializarJuego();
		if (solucionValida && esSolucion()) {
		    solucionValida=true;
		   	eliminarCeldas(cantCeldasAEliminar);
		}
		else {
		    solucionValida=false;
		}
	}
	
	public void accionar(Celda c) {
		c.actualizar();
	}
	
	public Celda getCelda(int i, int j) {
		return this.tablero[i][j];
	}
	
	public int getCantFilas() {
		return this.cantFilas;
	}
	
	public boolean getSolucionValida() {
		return solucionValida;
	}
	
	private void inicializarJuego() {
		int fila=0,columna=0,valor;
		File archivo;
		FileReader fr=null;
		BufferedReader br=null;
		String linea;
		char caracter;
		try {
			archivo= new File ("C:\\Users\\aleja\\OneDrive\\Escritorio\\Proyecto TDP\\src\\logica\\archivo.txt");
			fr=new FileReader(archivo);
		    br=new BufferedReader(fr);
		    while((linea=br.readLine())!=null && solucionValida) {
			    for (int i=0;i<linea.length() && solucionValida;i++) {
			   		if (columna<cantFilas) {
				   		caracter=linea.charAt(i);	    		
				    	if (caracter!=' ') {
				    		if (caracter!='0' && Character.isDigit(caracter)) {
								tablero[fila][columna] = new Celda();
								valor = caracter - '0';//paso el char a int
								tablero[fila][columna].setValor(valor-1);//-1 pq el 1 es la posicion 0 del arreglo	
								columna++;
				    		}
				    		else {
				    			solucionValida=false;
				    		}
						}
			    	}
				   	else {//si el archivo tiene mas columnas que cantFilas
				    	solucionValida=false;
				   	}
				}
			   	columna=0;
			   	fila++;
		    }
		    br.close(); fr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			solucionValida=false;
		}
	}
	private boolean esSolucion() {
		boolean solucion=true;
		for (int f=0;f<cantFilas;f++) {
			for (int c=0;c<cantFilas;c++) {
				System.out.println("fila: "+f+" columna: "+c);
				//if (tablero[f][c].getValor()!=null) {
					solucion=cumpleReglaFila(f,c,tablero[f][c].getValor()) && cumpleReglaColumna(f,c,tablero[f][c].getValor()) && cumpleReglaPanel(f,c,tablero[f][c].getValor());
					if (!solucion) {
						break;
					}
				//}
			}
		}
		return solucion;
	}
	
	public boolean cumpleReglaFila(int fila, int columnaCelda, int valor) {
		int cont=0;
		for (int i=0;i<cantFilas;i++) {
			if (tablero[fila][i].getValor()!=null && tablero[fila][i].getValor()==valor) {
				cont++;
			}
		}
		if (cont>1) {//si la celda no cumple la regla
			tablero[fila][columnaCelda].setCumpleRegla(false);
		}
		else {//la celda cumple la regla
			tablero[fila][columnaCelda].setCumpleRegla(true);
		}
		return cont==1;
	}
	
	public boolean cumpleReglaColumna(int filaCelda, int columna, int valor) {
		int cont=0;
		for (int i=0;i<cantFilas;i++) {
			if (tablero[i][columna].getValor()!=null && tablero[i][columna].getValor()==valor) {
				cont++;
			}
		}
		if (cont>1) {//si la celda no cumple la regla
			tablero[filaCelda][columna].setCumpleRegla(false);
		}
		else {//la celda cumple la regla
			tablero[filaCelda][columna].setCumpleRegla(true);
		}
		return cont==1;
	}
	
	public boolean cumpleReglaPanel(int filaCelda, int columnaCelda, int valor) {
		boolean cumple=true;
		if (filaCelda>=0 && filaCelda<=2) {
			if (columnaCelda>=0 && columnaCelda<=2) {//panel superior izquierdo
				cumple=cumpleReglaPanelAux(filaCelda,columnaCelda,0,2,0,2,valor);
			}
			else {
				if (columnaCelda>=3 && columnaCelda<=5) {//panel superior medio
					cumple=cumpleReglaPanelAux(filaCelda,columnaCelda,0,2,3,5,valor);
				}
				else {//panel superior derecho
					cumple=cumpleReglaPanelAux(filaCelda,columnaCelda,0,2,6,8,valor);
				}
			}
		}
		else {
			if (filaCelda>=3 && filaCelda<=5) {
				if (columnaCelda>=0 && columnaCelda<=2) {//panel medio izquierdo
					cumple=cumpleReglaPanelAux(filaCelda,columnaCelda,3,5,0,2,valor);
				}
				else {
					if (columnaCelda>=3 && columnaCelda<=5) {//panel medio medio
						cumple=cumpleReglaPanelAux(filaCelda,columnaCelda,3,5,3,5,valor);
					}
					else {//panel medio derecho
						cumple=cumpleReglaPanelAux(filaCelda,columnaCelda,3,5,6,8,valor);
					}
				}
			}
			else {
				if (columnaCelda>=0 && columnaCelda<=2) {//panel inferior izquierdo
					cumple=cumpleReglaPanelAux(filaCelda,columnaCelda,6,8,0,2,valor);
				}
				else {
					if (columnaCelda>=3 && columnaCelda<=5) {//panel inferior medio
						cumple=cumpleReglaPanelAux(filaCelda,columnaCelda,6,8,3,5,valor);
					}
					else {//panel inferior derecho
						cumple=cumpleReglaPanelAux(filaCelda,columnaCelda,6,8,6,8,valor);
					}
				}
			}
		}
		if (cumple) {
			tablero[filaCelda][columnaCelda].setCumpleRegla(true);
		}
		else {
			tablero[filaCelda][columnaCelda].setCumpleRegla(false);
		}
		return cumple;
	}
	
	public boolean cumpleReglaPanelAux(int filaCelda, int columnaCelda, int minFila, int maxFila, int minColumna, int maxColumna, int valor) {
		int cont=0;
		for (int fila=minFila;fila<=maxFila;fila++) {
			for (int columna=minColumna;columna<=maxColumna;columna++) {
				if (tablero[fila][columna].getValor()!=null && tablero[fila][columna].getValor()==valor) {
					cont++;
				}
			}
		}
		return cont==1;
	}
	
	private void eliminarCeldas(int cant) {
		int cont=0;
		while (cont<cant) {
			Random rand = new Random();
			int fila = rand.nextInt(cantFilas);
			int columna = rand.nextInt(cantFilas);
			if (tablero[fila][columna].getValor()!=null) {//si la celda no fue eliminada
				tablero[fila][columna]=new Celda();//elimino la celda
				tablero[fila][columna].setCeldaFija(false);//la celda deja de ser una celda fija
				cont++;
			}
		}
		System.out.println("Se eliminaron "+cont);
		/*int cont=0;
		for (int f=0;f<cantFilas;f++) {
			for (int c=0;c<cantFilas;c++) {
				Random rand = new Random();
				int value = rand.nextInt(2);
				//System.out.println("value:"+value);
				//De acuerdo a value decidir si asignar un valor o no
				if (cont<cant && value == 1) {//si valor=1 borra la imagen de la celda
					tablero[f][c]=new Celda();
					cont++;
				}
				else {//si valor=0 la celda pasa a ser una celda fija
					celdasCumplenRegla++;
					tablero[f][c].setCeldaFija(true);//para que las celdas iniciales no puedan cambiarse
				}
			}
		}
		System.out.println("Se eliminaron "+cont+" Celdas cumplen: "+celdasCumplenRegla);
		Random rand = new Random();*/
	}
	
	public int cantCeldasCumplenRegla() {
		int cont=0;
		for (int f=0;f<cantFilas;f++) {
			for (int c=0;c<cantFilas;c++) {
				if (tablero[f][c].getValor()!=null && tablero[f][c].getCumpleRegla()) {
					cont++;
				}
			}
		}
		return cont;
	}
	
	public boolean gano() {
		return cantCeldasCumplenRegla() == 81;
	}
}
