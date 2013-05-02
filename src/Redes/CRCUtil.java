package Redes;

public class CRCUtil {
	
	/*
	 Class CRCUtil, contém métodos que implementam a técnica de transmissão com CRC.
	 */
	
	/*
	 Divisao de polinómio, tem como inputs o dividendo, divisor e o resto,
	 devolve um vector de inteiros com a divisao.
	 */
	private static int[] dividePol(int div[],int divisor[], int rem[]){
        int cur=0;
        while(true){
            for(int i=0;i<divisor.length;i++)
                rem[cur+i]=(rem[cur+i]^divisor[i]);
            
            while(rem[cur]==0 && cur!=rem.length-1)
                cur++;
    
            if((rem.length-cur)<divisor.length)
                break;
        }
        return rem;
    }
	
	/*
	 método crcRem
	 A partir da mensagem com crc e o divisor, devolve uma String com o resto (remainder) da operação de CRC
	 */
	public static String crcRem(String crcStr, String divisorStr){
		int[] crc = new int[crcStr.length()];
		int[] rem = new int[crcStr.length()];
		int[] divisor = new int[divisorStr.length()];
		
		//convertemos a string em vectores de inteiros (1,0)
		for(int i=0; i < crcStr.length(); i++){
			if(crcStr.charAt(i) == '0'){
				crc[i] = 0;
			} else {
				crc[i] = 1;
			}
		}
		
		for(int i=0; i < divisorStr.length(); i++){
			if(divisorStr.charAt(i) == '0'){
				divisor[i] = 0;
			} else {
				divisor[i] = 1;
			}
		}
		
		for(int j=0; j<crc.length; j++){
			rem[j] = crc[j];
		}

		rem=dividePol(crc, divisor, rem);

		String remStr = ""; //Usamos isto para converter de volta para uma String com os dados.
		for(int i=0; i<rem.length;i++){
			if(rem[i]==0){
				remStr=remStr+'0';
        	} else {
        		remStr=remStr+'1';
        	}
		}
		return remStr;
	}
	
	/*
	 método detectErrorsCRC
	 utilizado para detectar erros no código CRC através do Resto (remainder)
	 Após a operação, caso encontre um bit 1, retorna true (encontrou erro)
	 caso contrário retorna false, ou seja, a técnica não detectou nenhum erro na trama.
	 */
	public static boolean detectErrorsCRC(String remStr){
		int[] rem = new int[remStr.length()];
		for(int i=0; i < remStr.length(); i++){
			if(remStr.charAt(i) == '0'){
				rem[i] = 0;
			} else {
				rem[i] = 1;
			}
		}
		for(int i=0; i < rem.length; i++){
            if(rem[i]!=0){
                return true;
            }
            if(i==rem.length-1)
                return false;
        }
		return false;
	}
	
	/*
	 método crc, este é o método principal desta classe,
	 tem como input a mensagem a ser codificada e o divisor original (que no caso das tramas de 4 e 11 bits neste projecto vai ser sempre 10111)
	 mas o método mantém-se dinâmico e pode ser utilizado para outros polinómios geradores.
	 Retorna a mensagem já codificada com CRC.
	 */
	public static String crc(String message, String divisorOrig){
		int[] data = new int [message.length()];
		int[] divisor = new int [divisorOrig.length()];
		int total_size = message.length() + divisorOrig.length() - 1;
		
		/*
		 Convertemos as Strings com os bits dados para vectores de inteiros, para facilitar
		 o processo.
		 */
		for(int i=0;i<message.length();i++){
			if(message.charAt(i) == '0'){
				data[i] = 0;
			} else {
				data[i] = 1;
			}
		}
		
		for(int i=0;i<divisorOrig.length();i++){
			if(divisorOrig.charAt(i) == '0'){
				divisor[i] = 0;
			} else {
				divisor[i] = 1;
			}
		}
		
		int[] div=new int[total_size];
		int[] rem=new int[total_size];
		int[] crc=new int[total_size];
		
		for(int i=0;i<data.length;i++)
            div[i]=data[i];
        
        for(int j=0; j<div.length; j++){
              rem[j] = div[j];
        }
    
        rem=dividePol(div, divisor, rem);
        
        for(int i=0;i<div.length;i++){
            crc[i]=(div[i]^rem[i]);
        }
        
        //converte-se o resultado de volta para String para trabalhar com ela nos motores.
        String str = "";
        for(int i=0;i<crc.length;i++){
        	if(crc[i]==0){
        		str=str+'0';
        	} else {
        		str=str+'1';
        	}
        }
		
		return str;
	}
}
