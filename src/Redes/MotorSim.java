package Redes;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class MotorSim extends JComponent implements Runnable {
	private static final long serialVersionUID = 1L;
	
	/*
	 Tabelas para calcular as probabilidades de não detecção de Erro com os métodos de CRC e Código de Hamming.
	 */
	
	private static final int[] crcErrorFour = {1,11,3}, crcErrorEleven = {9,177,625,795,387,51,3};
	private static final int[] hammingErrorFour = {0,0,7,7,0,0,7}, hammingErrorEleven = {0,0,35,105,168,280,435,435,280,168,105,35,0,0,1};
	
	// Tabela de probabilidades (escolha para gráficos)
	private static final double[] probs = {0.000001, 0.00001, 0.0001, 0.001, 0.01, 0.1, 0.125, 0.25, 0.5};
	
	private JFrame janela;
	private int numBits, numTramas, errorMethod, tramasTransmitted, tramasComErro, tramasArranjadas, tramasArranjadasMal,tramasComErroNaoDetectados, bitsErradosExp;
	private int selProb;
	private double selectedProb;
	private boolean stop = false;
	
	private GraphWindow graph;
	private JButton stopButton;
	
	public MotorSim(int errorMethod, int numBits, int numTramas, int selProb, JFrame janela, GraphWindow graph, JButton stopButton) {
		this.janela = janela;
		this.errorMethod = errorMethod;
		this.numBits = numBits;
		this.numTramas = numTramas;
		this.selProb = selProb;
		this.selectedProb = probs[selProb];
		this.graph = graph;
		this.stopButton = stopButton;
		
		new Thread(this).start();
	}
	
	
	/*
	 Para propósitos de facilidade de manobra no código, os cálculos teóricos são realizados à medida que nos são apresentados
	 os valores experimentais da simulação. Daí estarem dentro do método paintComponent.
	 Cada vez que é redesenhado (a cada aprox. 100 mil tramas enviadas), apresenta os valores teoricos probabilisticos
	 e as percentuagens experimentais.
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		Rectangle size = g.getClipBounds();
		g.fillRect(0,0,size.width,size.height);
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial",Font.BOLD,12));
		g.drawString("SIMULAÇÃO", 10, 19);
		String metd = "BIT PARIDADE";
		if(errorMethod == 1)
			metd = "CRC";
		if(errorMethod == 2)
			metd = "HAMMING";
		g.drawString("ERROR DETECTION METHOD: "+metd, 10, 32);
		g.drawString("#BITS: "+numBits, 10, 45);
		
		g.drawString("ERROR PROBABILITY p/ BIT: "+selectedProb, 10, 58);
		
		g.setColor(Color.RED);
		g.drawString("----------------------------", 10, 71);
		g.setColor(Color.GREEN);
		
		double ntmpBits = (double) numBits;
		
		if(errorMethod == 0){
			ntmpBits = numBits + 1;
		} else if(errorMethod == 1){
			ntmpBits = numBits + 4;
		} else if(errorMethod == 2){
			if(numBits == 4){
				ntmpBits = 7;
			} else {
				ntmpBits = 15;
			}
		}
		
		g.drawString("#BITS p/ TRAMA: "+(int)ntmpBits, 420, 19);
			
		g.setColor(Color.YELLOW);
		g.drawString("THEORICAL STATS", 10, 84);
		g.setColor(Color.WHITE);
		
		
		/*
		 As probabilidades foram calculadas a partir das fórmulas estipuladas no relatório.
		 Probabilidade de Transmissão sem Erros Teórica.
		 Em que Ptse = (1-Pb)^L, sendo Pb (selectedProb a probabilidade de erro por bit escolhida
		 e ntmpBits (L) o numero de bits na trama.
		 */
		double ptseTeorico = Math.pow((1-selectedProb),ntmpBits);
		
		/*
		 Numero esperado de bitsErrados no total (numero de bits por trama * numero de tramas no total * probabilidade de erro por bit)
		 */
		int bitsErrados = (int) (ntmpBits * numTramas * selectedProb);
		
		double pndceTeorico = 0;
		double pdccTeorico = 0;
		
		/*
		 Para o cálculo da Probabilidade de Não Detecção, sabendo que houve erro,
		 recorremos a vários métodos que se provam eficazes na previsão da probabilidade, para cada
		 uma das técnicas utilizadas.
		 Para o bit de paridade toma-se partido a fórmula simplificada, explicada no relatório.
		 No caso das técnicas de CRC e Código de Hamming,
		 o método utilizado baseia-se no uso das tabelas (listadas no topo da classe)
		 que permitem, graças à pre-análise extensiva, chegar aos valores exactos para o cálculo.
		 
		 Foi utilizado o valor exacto para os dois, apesar de ter sido recomendado a aproximação no caso do código de hamming.
		 */
		
		if(errorMethod == 0){ //Bit paridade
			pndceTeorico = (0.5)*(1+(Math.pow(1-2*selectedProb, ntmpBits)) - 2*Math.pow(1-selectedProb, ntmpBits));
			pndceTeorico /= (1.0 - ptseTeorico);
		} else if(errorMethod == 1){ //CRC
			int k = 2;
			int[] tempCrcError;
			if(numBits == 4){
				tempCrcError = crcErrorFour;
			} else {
				tempCrcError = crcErrorEleven;
			}
			for(int i = 0; i < tempCrcError.length ;i++){
				double ptemp = tempCrcError[i] * Math.pow(selectedProb, k) * Math.pow(1.0-selectedProb, ntmpBits - k);
				pndceTeorico += ptemp;
				k+=2;
			}
			
			pndceTeorico /= (1.0 - ptseTeorico);
		} else if(errorMethod == 2){ //Codigo Hamming
			//pndce
			int[] tempHammingError;
			if(numBits == 4){
				tempHammingError = hammingErrorFour;
			} else {
				tempHammingError = hammingErrorEleven;
			}
			for(int i = 1; i <= tempHammingError.length; i++){
				double ptemp = tempHammingError[i-1] * Math.pow(selectedProb, i) * Math.pow(1.0-selectedProb, ntmpBits-i);
				pndceTeorico += ptemp;
			}
			
			pndceTeorico /= (1.0 - ptseTeorico);
			
			/*
			 Probabilidade de correcção correcta, dado que ocorreu um erro. (Probabilidade condicionada pela probabilidade de haver erro)
			 */
			pdccTeorico = ntmpBits * selectedProb * Math.pow(1.0-selectedProb,ntmpBits-1) / (1.0 - ptseTeorico);
		}
		
		g.drawString("Prob. Transmissao sem Erros: "+ptseTeorico*100+"%", 10, 97);
		g.drawString("Valor esperado de #bits errados: "+bitsErrados, 10, 110);
		g.drawString("Valor esperado de Percentagem de Bits Errados: "+((double)bitsErrados/(double)(numTramas*ntmpBits))*100+"%", 10, 123);
		g.drawString("Prob. N Deteçao de Erros Sabendo que Houve: "+pndceTeorico*100+"%", 10, 136);
		if(errorMethod == 2){
			g.drawString("Prob de Correçao Correcta: "+pdccTeorico*100+"%", 10, 149);
		}
		
		g.setColor(Color.CYAN);
		g.drawString("#TRAMAS: "+tramasTransmitted, 10, 175);
		g.drawString("#TRAMAS w/ ERRORS: "+tramasComErro, 10, 188);
		g.drawString("#TRAMAS w/ ERRORS (NOT DETECTED): "+tramasComErroNaoDetectados, 10, 201);
		if(errorMethod == 2){
			g.setColor(Color.GREEN);
			g.drawString("#TRAMAS SUCCESSFULLY FIXED: "+tramasArranjadas, 10, 214);
			g.setColor(Color.RED);
			g.drawString("#TRAMAS ERRANOUSLY FIXED: "+tramasArranjadasMal, 10, 227);
		}
		
		g.setColor(Color.YELLOW);
		g.drawString("EXPERIMENTAL STATS", 10, 253);
		g.setColor(Color.WHITE);
		
		//Nota: TramasComErro + TramasComErroNaoDetectados = Todos os Erros
		g.drawString("Percentagem de Transmissão s/ Erros: "+(1.0-((double)(tramasComErro+tramasComErroNaoDetectados)/(double)tramasTransmitted))*100+"%", 10, 266);
		g.drawString("Bits Errados: "+bitsErradosExp, 10, 279);
		g.drawString("Percentagem de Bits Errados: "+((double)bitsErradosExp/(double)(tramasTransmitted*ntmpBits))*100+"%", 10, 292);
		g.drawString("Percentagem de Erros Não Detectados: "+((double)tramasComErroNaoDetectados/(double)(tramasComErro+tramasComErroNaoDetectados))*100+"%", 10, 305);
		if(errorMethod == 2){
			g.drawString("Percentagem de Tramas Corrigidas Correctamente: "+((double)tramasArranjadas/(double)(tramasComErro))*100+"%", 10, 318);
		}
		
		//Grafico
		/*
		 	graphIndex:
			0 - transmissao sem erros
			1 - bits errados
			2 - nao detectados
			3 - corrigidas correctamente
		 */
		graph.setGraphVar(0, selProb, ptseTeorico, numBits, errorMethod, false); //transmissao sem erros teorico
		graph.setGraphVar(1, selProb, (double)bitsErrados/(double)(numTramas*ntmpBits), numBits, errorMethod, false); //bits errados teorico
		graph.setGraphVar(2, selProb, pndceTeorico, numBits, errorMethod, false); //nao detectados teorico
		
		graph.setGraphVar(0, selProb, (1.0-((double)(tramasComErro+tramasComErroNaoDetectados)/(double)tramasTransmitted)), numBits, errorMethod, true); //transmissao sem erros exp
		graph.setGraphVar(1, selProb, (double)bitsErradosExp/(double)(tramasTransmitted*ntmpBits), numBits, errorMethod, true); //bits errados exp
		graph.setGraphVar(2, selProb, (double)tramasComErroNaoDetectados/(double)(tramasComErro+tramasComErroNaoDetectados), numBits, errorMethod, true); //nao detectados exp
		
		if(errorMethod == 2){
			graph.setGraphVar(3, selProb, pdccTeorico, numBits, errorMethod, false); //corrigidos correctamente teorico
			graph.setGraphVar(3, selProb, (double)tramasArranjadas/(double)tramasComErro, numBits, errorMethod, true); //corrigidos correctamente exp
		}
		
		//Barra de progresso
		
		double totalDone = (double)tramasTransmitted/(double)numTramas;
		
		double totalSize = 520;
		int curSize = (int) (totalSize * totalDone);
		
		g.setColor(Color.YELLOW);
		g.fillRect(10, 420, curSize, 25);
		
		g.setColor(Color.BLUE);
		g.drawString((int)(totalDone*100)+"%", (curSize/2), 438);
		
	}
	
	private void endCalc(){
		stopButton.setEnabled(false);
		janela.repaint();
	}
	
	public void stop(){
		stop = true;
	}
	
	/*
	 método transmitData()
	 MODO SIMULAÇÃO
	 Utilizado em cada iteração de envio de uma trama, calcula (igual ao modo de demonstração) o processo de passagem de uma trama
	 desde a geração aleatória de Dados em bits, passando pelo processo de inserção de erros baseando-se em probabilidade de bit
	 até a recepção e correcção (quando aplicável) de cada uma.
	 
	 Este processo 
	 */
	private void transmitData(){
		tramasTransmitted++;
		String beforeTrans = MotorDemonstracao.generateRandomData(numBits);
		if(errorMethod == 0){ //Bit Paridade
			String toTransmit = beforeTrans;
			String parity = MotorDemonstracao.paridade(toTransmit);
			String afterTransmit = MotorDemonstracao.insertErrorsAuto(parity+toTransmit, selectedProb);
			
			bitsErradosExp += HammingUtil.getDistance(parity+toTransmit, afterTransmit); // A distancia de hamming da-nos a diferença (em #bits) entre 2 palavras
			
			if(!MotorDemonstracao.detectErrorsParidade(afterTransmit)){
				if(!afterTransmit.equals(parity+toTransmit)){
					tramasComErroNaoDetectados++;
				}
			} else {
				tramasComErro++;
			}
			
		} else if(errorMethod == 1){ //CRC
			String toTransmit = CRCUtil.crc(beforeTrans, "10111");
			String afterTransmit = MotorDemonstracao.insertErrorsAuto(toTransmit, selectedProb);
			
			bitsErradosExp += HammingUtil.getDistance(toTransmit, afterTransmit);
			
			String crcRemainder = CRCUtil.crcRem(afterTransmit, "10111");
			boolean detectadoErro = CRCUtil.detectErrorsCRC(crcRemainder);
			
			if(!detectadoErro){
				if(!afterTransmit.equals(toTransmit)){
					tramasComErroNaoDetectados++;
				}
			} else {
				tramasComErro++;
			}
		} else if(errorMethod == 2){ //Hamming
			String toTransmit = HammingUtil.hamming(beforeTrans);
			String afterTransmit = MotorDemonstracao.insertErrorsAuto(toTransmit, selectedProb);
			boolean detectadoErro = HammingUtil.hasError(afterTransmit);
			
			bitsErradosExp += HammingUtil.getDistance(toTransmit, afterTransmit);
			
			if(!detectadoErro){
				if(!afterTransmit.equals(toTransmit)){
					tramasComErroNaoDetectados++;
				}
			} else {
				tramasComErro++;
				String hammingErrorFix = HammingUtil.fixError(afterTransmit);
				String hammingOriginal = HammingUtil.getOriginal(hammingErrorFix);
				if(hammingOriginal.equals(beforeTrans)){
					tramasArranjadas++;
				} else {
					tramasArranjadasMal++;
				}
			}
		}
	}
	
	/*
	 Método run() da implementação Runnable para criação do thread para o simulador.
	 Corre numTramas de vezes ou até que seja comandado parar (flag, boolean stop)
	 */
	@Override
	public void run() {
		tramasTransmitted = 0;
		while(tramasTransmitted < numTramas && !stop){
			transmitData();
			
			if(tramasTransmitted % (numTramas/100) == 0){
				janela.repaint();
			}
		}
		endCalc();
	}

}
