package Redes;

public class HammingUtil {
	
	/*
	 Classe HammingUtil
	 contém os métodos que implementam a técnica de transmissão de dados com Códigos de Hamming
	 No caso do projecto, Hamming(7,4) e Hamming(15,11)
	 */
	
	//Retorna true se '1', false se '0'
	private static boolean charToBool(char c){
		if(c == '1')
			return true;
		return false;
	}
	
	/*
	 Lista completa dos XORs necessarios para a implementação do código de hamming para mensagens de 4 e 11 bits
	 Os parity bits e os check bits foram calculados anteriormente
	 e apenas implementados nesta forma, sem ser generalizada.
	 
 	Parity:
 		4bit
	 	P1 = X3 + X5 + X7
	 	P2 = X3 + X6 + X7
	 	P4 = X5 + X6 + X7
	 	
	 	11bit
	 	P1 = X3 + X5 + X7 + X9 + X11 + X13 + X15
        P2 = X3 + X6 + X7 + X10 + X11 + X14 + X15
        P4 = X5 + X6 + X7 + X12 + X13 + X14 + X15
        P8 = X9 + X10 + X11 + X12 + X13 + X14 + X15
 	
 	Check:
 		4bit
		C1 = X3 + X5 + X7 + P1
        C2 = X3 + X6 + X7 + P2
        C4 = X5 + X6 + X7 + P4
        
        11bit
		C1 = X3 + X5 + X7 + X9 + X11 + X13 + X15 + P1
        C2 = X3 + X6 + X7 + X10 + X11 + X14 + X15 + P2
        C4 = X5 + X6 + X7 + X12 + X13 + X14 + X15 + P4
        C8 = X9 + X10 + X11 + X12 + X13 + X14 + X15 + P8
	 */
	
	/*
	 método hamming, método principal que codifica a mensagem
	 Tem como input apenas a mensagem, toma partido do método XOR do MotorDemonstracao para realizar
	 as operações necessarias para os parity bits.
	 */
	public static String hamming(String codeStr){
		int numBits = codeStr.length();
		boolean[] message = new boolean[numBits];
		
		for(int i=0;i<numBits;i++){
			message[i] = charToBool(codeStr.charAt(i));
		}
		
		boolean[] finalCode = null;
		if(numBits == 4){
			boolean p1,p2,x3,p4,x5,x6,x7;
			x3 = message[0];
			x5 = message[1];
			x6 = message[2];
			x7 = message[3];
			p1 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(x3, x5), x7);
			p2 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(x3, x6), x7);
			p4 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(x5, x6), x7);
			
			boolean[] temp = {p1,p2,x3,p4,x5,x6,x7};
			finalCode = temp;
		} else if(numBits == 11){
			boolean p1,p2,x3,p4,x5,x6,x7,p8,x9,x10,x11,x12,x13,x14,x15;
			x3 = message[0];
			x5 = message[1];
			x6 = message[2];
			x7 = message[3];
			x9 = message[4];
			x10 = message[5];
			x11 = message[6];
			x12 = message[7];
			x13 = message[8];
			x14 = message[9];
			x15 = message[10];
			
			p1 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x3,x5),x7),x9),x11),x13),x15);
			p2 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x3,x6),x7),x10),x11),x14),x15);
			p4 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x5,x6),x7),x12),x13),x14),x15);
			p8 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x9,x10),x11),x12),x13),x14),x15);
			
			boolean[] temp = {p1,p2,x3,p4,x5,x6,x7,p8,x9,x10,x11,x12,x13,x14,x15};
			finalCode = temp;
		}
		
		String str = "";
		for(int i = 0; i<finalCode.length;i++){
			if(finalCode[i]){
				str = str + "1";
			} else {
				str = str + "0";
			}
		}
		
		return str;
	}
	
	
	//método que retorna se a contagem de erros (ou seja a posição) é maior que 0, ou seja, se detecta um erro.
	public static boolean hasError(String codeStr){
		return countCheck(codeStr) > 0;
	}
	
	//método que substitui um caracter na posição index da String str por outro, rep
	private static String replaceCharString(String str, int index, char rep){
		String fin = "";
		for(int i = 0; i < str.length();i++){
			if(i == index){
				fin = fin + rep;
			} else {
				fin = fin + str.charAt(i);
			}
		}
		return fin;
	}
	
	
	/*
	 método getDistance
	 Retorna a distância de Hamming entre duas quaisquer Strings de bits de dados
	 Desde que estas tenham o mesmo tamanho.
	 Pode ser usada para calcular o número de bits errados entre uma mensagem e outra (como visto no simulador).
	 */
	public static int getDistance(String one, String two){
		if(one.length() == two.length()){
			boolean[] oneBool, twoBool, finalBool;
			oneBool = new boolean[one.length()];
			twoBool = new boolean[one.length()];
			finalBool = new boolean[one.length()];
			
			for(int i = 0; i<one.length();i++){
				if(one.charAt(i) == '1'){
					oneBool[i] = true;
				} else {
					oneBool[i] = false;
				}
				if(two.charAt(i) == '1'){
					twoBool[i] = true;
				} else {
					twoBool[i] = false;
				}
				finalBool[i] = MotorDemonstracao.XOR(oneBool[i], twoBool[i]);
			}
			int dist = 0;
			for(int i = 0; i<finalBool.length;i++){
				if(finalBool[i])
					dist++;
			}
			return dist;
		}
		return 0;
	}
	
	
	/*
	 A partir da mensagem codificada com hamming, retorna a mensagem original
	 Ou seja, todas as que não são parity bits.
	 */
	public static String getOriginal(String codeStr){
		int numBits = codeStr.length();
		if(numBits == 7){
			//Hamming(7,4), retorna as posiçoes 2,4,5 e 6
			return ""+codeStr.charAt(2)+codeStr.charAt(4)+codeStr.charAt(5)+codeStr.charAt(6);
		} else if(numBits == 15){
			//Hamming(15,11), retorna as posiçoes 2,4,5,6,8,9,10,11,12,13 e 14
			return ""+codeStr.charAt(2)+codeStr.charAt(4)+codeStr.charAt(5)+codeStr.charAt(6)+codeStr.charAt(8)+codeStr.charAt(9)+codeStr.charAt(10)+codeStr.charAt(11)+codeStr.charAt(12)+codeStr.charAt(13)+codeStr.charAt(14);
		}
		return codeStr;
	}
	
	/*
	 método fixError, que tem como função tentar corrigir uma trama dado que foi encontrado um erro.
	 Funciona por usar o método countCheck para descobrir se existe um erro, e a posição em que se encontra
	 e de seguida inverte o bit nessa posição.
	 Só é possivel, com este método, corrigir com 100% de certeza 1 erro de bit por trama. 
	 */
	public static String fixError(String codeStr){
		int errorPos = countCheck(codeStr);
		//System.out.println("fixError - codeStr: "+codeStr+" - errorPos: "+errorPos+" char at errorPos: "+codeStr.charAt(errorPos-1));
		if(errorPos > 0){
			char err = codeStr.charAt(errorPos-1);
			char fix = MotorDemonstracao.invertBinChar(err);
			//System.out.println("err: "+err+" fix: "+fix);
			codeStr = replaceCharString(codeStr, errorPos-1, fix);
		}
		return codeStr;
	}
	
	/*
	 método countCheck, utilizado em vários métodos acima
	 Toma partido dos checkbits para verificar a presença de erros na trama dada.
	 Utiliza o sistema listado no topo, com XORs entre as posições e o parity bit especifico.
	 Retorna a posição em que encontra (dado que encontra) um erro.
	 Consegue detectar com segurança até 2 erros. (apenas podendo ser corrigido 1)
	 */
	public static int countCheck(String codeStr){
		int numBits = codeStr.length();
		boolean[] message = new boolean[numBits];
		
		for(int i=0;i<numBits;i++){
			message[i] = charToBool(codeStr.charAt(i));
		}
		
		if(numBits == 7){
			boolean p1,p2,x3,p4,x5,x6,x7,c1,c2,c4;
			p1 = message[0];
			p2 = message[1];
			x3 = message[2];
			p4 = message[3];
			x5 = message[4];
			x6 = message[5];
			x7 = message[6];
			
			c1 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x3, x5), x7),p1);
			c2 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x3, x6), x7),p2);
			c4 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x5, x6), x7),p4);
			
			int tot = 0;
			if(c1)
				tot++;
			if(c2)
				tot+=2;
			if(c4)
				tot+=4;
			
			//System.out.println(numBits+" - "+p1+" "+p2+" "+x3+" "+p4+" "+x5+" "+x6+" "+x7+" - "+c1+" "+c2+" "+c4);
			
			return tot;
			
		} else if(numBits == 15){
			boolean p1,p2,x3,p4,x5,x6,x7,p8,x9,x10,x11,x12,x13,x14,x15,c1,c2,c4,c8;
			p1 = message[0];
			p2 = message[1];
			x3 = message[2];
			p4 = message[3];
			x5 = message[4];
			x6 = message[5];
			x7 = message[6];
			p8 = message[7];
			x9 = message[8];
			x10 = message[9];
			x11 = message[10];
			x12 = message[11];
			x13 = message[12];
			x14 = message[13];
			x15 = message[14];
			
			c1 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x3,x5),x7),x9),x11),x13),x15),p1);
			c2 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x3,x6),x7),x10),x11),x14),x15),p2);
			c4 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x5,x6),x7),x12),x13),x14),x15),p4);
			c8 = MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(MotorDemonstracao.XOR(x9,x10),x11),x12),x13),x14),x15),p8);
			
			int tot = 0;
			if(c1)
				tot++;
			if(c2)
				tot+=2;
			if(c4)
				tot+=4;
			if(c8)
				tot+=8;
			
			return tot;
		}
		return 0;
	}
}
