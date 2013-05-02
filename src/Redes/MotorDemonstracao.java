package Redes;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JComponent;


public class MotorDemonstracao extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private int numBits, errorMethod, hammingErrorPos;
	private boolean manual, detectadoErro;
	private String bitsDados, padraoErros, toTransmit, afterTransmit, crcRemainder, hammingErrorFix, hammingOriginal;
	private double probErro;
	
	/*
	 Dois m�todos constructores, um para a transmiss�o Manual e outro para a tranmiss�o Autom�tica
	 A diferen�a substancial � que no caso manual, inserem-se os dados em bits a enviar e o padr�o de erros,
	 possibilitando assim monotorizar o bom funcionamento de cada uma das implementa��es das t�cnicas de detec��o e correc��o de erro.
	 No caso autom�tico, as escolhas limitam-se ao m�todo e ao n�mero de bits de dados a enviar e a probabilidade
	 de erro por bit a inserir na mensagem 'encriptada' (Canal ruidoso). Permitindo assim uma analise mais estat�stica, apenas
	 com o modo demonstrativo.
	 */
	public MotorDemonstracao(int errorMethod, int numBits, String bitsDados, String padraoErros){
		this.errorMethod = errorMethod;
		this.numBits = numBits;
		this.manual = true;
		this.bitsDados = bitsDados;
		this.padraoErros = padraoErros;
		transmitManual();
	}
	
	public MotorDemonstracao(int errorMethod, int numBits, double errorProbability){
		this.errorMethod = errorMethod;
		this.numBits = numBits;
		this.manual = false;
		this.probErro = errorProbability;
		this.bitsDados = generateRandomData(numBits);
		transmitAuto();
	}
	
	/*
	 m�todo generateRandomData
	 Gera uma String de dados aleat�rios com num bits.
	 */
	public static String generateRandomData(int num){
		String str = "";
		Random rand = new Random();
		for(int i = 0; i < num; i++){
			if(rand.nextDouble()<=0.5){
				str=str+"1";
			} else {
				str=str+"0";
			}
		}
		return str;
	}
	
	/*
	 m�todo paridade
	 Gera o bit de paridade de dada String de dados em bits.
	 Caso tenha um n�mero par de 1s retorna 0, caso tenha um n�mero �mpar retorna 1.
	 */
	public static String paridade(String in){
		int ones = 0;
		for(int i = 0; i < in.length(); i++){
			if(in.charAt(i)=='1')
				ones++;
		}
		if(ones%2==0)
			return "0";
		return "1";
	}
	
	//m�todo que retorna um XOR entre duas booleanas.
	public static boolean XOR(boolean x, boolean y) {
	    return ( ( x || y ) && ! ( x && y ) );
	}
	
	/*
	 m�todo insertErrors
	 Serve para inserir erros numa string de dados com base num padr�o de erros em bit.
	 Faz XOR entre a mensagem original e o padr�o, bit a bit, e retorna o resultado da opera��o.
	 */
	public static String insertErrors(String in, String pattern){
		String fin = "";
		for(int i = 0; i < pattern.length(); i++){
			boolean inBool = false, inPat = false;
			if(in.charAt(i) == '1')
				inBool = true;
			if(pattern.charAt(i) == '1')
				inPat = true;
			String add = "0";
			if(XOR(inBool, inPat))
				add = "1";
			fin = fin + add;
		}
		return fin;
	}
	
	//m�todo para inverter um caracter. ('0' fica '1' e '1' fica '0')
	public static char invertBinChar(char in){
		if(in == '0')
			return '1';
		return '0';
	}
	
	/*
	 m�todo insertErrorsAuto
	 Insere erros numa String de dados, baseando-se na probabilidade de erro por bit
	 � usada pelo modo de demonstra��o e pelo modo de simula��o.
	 */
	public static String insertErrorsAuto(String in, double prob){
		String fin = "";
		Random rand = new Random();
		for(int i = 0; i < in.length(); i++){
			char cur = in.charAt(i);
			double r = rand.nextDouble();
			if(r <= prob){
				cur = invertBinChar(cur);
			}
			fin=fin+cur;
		}
		return fin;
	}
	
	/*
	m�todo detectErrorsParidade
	Detecta erros com o bit de paridade, retorna true se encontrar um erro
	retorna false se o bit de paridade se mantiver consistente com a trama.
	 */
	public static boolean detectErrorsParidade(String after){
		int ones = 0;
		char paridade = after.charAt(0);
		for(int i = 1; i < after.length(); i++){
			if(after.charAt(i)=='1')
				ones++;
		}
		if((ones%2==0 && paridade == '1') || (ones%2!=0 && paridade=='0'))
			return true; //deu erro
		return false; //nao foram detectados erros.
	}
	
	/*
	 m�todo transmitManual
	 Simula a tranmiss�o, baseando-se nas v�rias t�cnicas de detec��o (e correc��o)
	 A String de dados passa pelo c�lculo de cada um dos m�todos, Bit Paridade, CRC e Hamming para a String toTransmit
	 inserem-se os erros na trama com o m�todo insertErrors (ou seja, manual) directamente com o padr�o de erros para a String afterTransmit
	 para todos os casos a booleana detectadoErro vai usar os m�todos especificos para cada t�cnica de transmiss�o,
	 tentando averiguar com os m�todos de detec��o se houve um erro.
	 */
	private void transmitManual(){
		if(errorMethod == 0){ //Bit paridade
			toTransmit = paridade(bitsDados) + bitsDados;
			afterTransmit = insertErrors(toTransmit, padraoErros);
			detectadoErro = detectErrorsParidade(afterTransmit);
		} else if(errorMethod == 1) { //CRC
			toTransmit = CRCUtil.crc(bitsDados, "10111"); //Divisor � sempre 10111
			afterTransmit = insertErrors(toTransmit, padraoErros);
			crcRemainder = CRCUtil.crcRem(afterTransmit, "10111");
			detectadoErro = CRCUtil.detectErrorsCRC(crcRemainder);
		} else if(errorMethod == 2) { //Hamming
			toTransmit = HammingUtil.hamming(bitsDados);
			afterTransmit = insertErrors(toTransmit, padraoErros);
			detectadoErro = HammingUtil.hasError(afterTransmit);
			hammingErrorPos = HammingUtil.countCheck(afterTransmit);
			hammingErrorFix = HammingUtil.fixError(afterTransmit);
			hammingOriginal = HammingUtil.getOriginal(hammingErrorFix);
		}
		
	}
	
	/*
	 m�todo transmitAuto
	 Simula a transmiss�o, parecido ao m�todo transmitManual
	 A String passa pelos mesmos c�lculos, mas desta vez os bitsDados v�o ser gerados aleat�riamente
	 e o m�todo de inser��o de erros utilizado � baseado em probabilidade de erro por bit
	 em vez de um padr�o.
	 */
	private void transmitAuto(){
		if(errorMethod == 0){ //Bit paridade
			toTransmit = paridade(bitsDados) + bitsDados;
			afterTransmit = insertErrorsAuto(toTransmit, probErro);
			detectadoErro = detectErrorsParidade(afterTransmit);
		} else if(errorMethod == 1) { //CRC
			toTransmit = CRCUtil.crc(bitsDados, "10111"); //Divisor � sempre 10111
			afterTransmit = insertErrorsAuto(toTransmit, probErro);
			crcRemainder = CRCUtil.crcRem(afterTransmit, "10111");
			detectadoErro = CRCUtil.detectErrorsCRC(crcRemainder);
		} else if(errorMethod == 2) { //Hamming
			toTransmit = HammingUtil.hamming(bitsDados);
			afterTransmit = insertErrorsAuto(toTransmit, probErro);
			detectadoErro = HammingUtil.hasError(afterTransmit);
			hammingErrorPos = HammingUtil.countCheck(afterTransmit);
			hammingErrorFix = HammingUtil.fixError(afterTransmit);
			hammingOriginal = HammingUtil.getOriginal(hammingErrorFix);
		}
	}
	
	//desenhar o motor de demonstra��o
	protected void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		Rectangle size = g.getClipBounds();
		g.fillRect(0,0,size.width,size.height);
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial",Font.BOLD,12));
		g.drawString("DEMONSTRA��O", 10, 19);
		String metd = "BIT PARIDADE";
		if(errorMethod == 1)
			metd = "CRC";
		if(errorMethod == 2)
			metd = "HAMMING";
		g.drawString("ERROR DETECTION METHOD: "+metd, 10, 32);
		g.drawString("#BITS: "+numBits, 10, 45);
		if(manual){
			g.drawString("MODE: MANUAL", 10, 58);
			g.drawString("DATA: "+bitsDados, 10, 71);
			g.drawString("ERROR PATTERN: "+padraoErros, 10, 84);
		} else {
			g.drawString("MODE: RANDOM", 10, 58);
			g.drawString("DATA (GENERATED): "+bitsDados, 10, 71);
			g.drawString("ERROR CHANCE: "+(int)(probErro*100)+"%", 10, 84);
		}
		g.setColor(Color.RED);
		g.drawString("----------------------------", 10, 97);
		g.setColor(Color.GREEN);
		g.drawString("SENT: "+toTransmit, 10, 110);
		g.drawString("RCVD: "+afterTransmit, 10, 123);
		if(detectadoErro){
			g.setColor(Color.RED);
			g.drawString("ERROR FOUND", 10, 136);
			if(errorMethod == 2){
				g.drawString("ERROR POS: "+hammingErrorPos, 10, 149);
				g.drawString("FIX: "+hammingErrorFix, 10, 162);
			}
			g.setColor(Color.GREEN);
		} else {
			g.drawString("NO ERRORS FOUND", 10, 136);
		}
		if(errorMethod == 1){
			g.drawString("REMAINDER: "+crcRemainder, 10, 149);
		} else if (errorMethod == 2){
			int pos = 149;
			if(detectadoErro){
				pos = 175;
			}
			g.drawString("FINAL MESSAGE: "+hammingOriginal, 10, pos);
		}
	}

}
