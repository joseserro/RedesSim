package Redes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;


public class Demonstracao {
	private static JFrame janelaDem = new JFrame("Demonstração");
	private static final int WINDOW_SIZE_X = 500, WINDOW_SIZE_Y = 700;
	private static double errorProbability = 0.5;
	private static JLabel autoText1Label;
	private static JSlider autoSlider;
	private static JButton runButton, clearButton;
	private static JPanel centerPanel;
	private static Container contentor;
	private static MaskFormatter formatBits1 = new MaskFormatter(),formatBits2 = new MaskFormatter();
	private static JFormattedTextField formattedManText1, formattedManText2;
	private static int errorMethod = 0, numBits = 4;
	private static MotorDemonstracao demMotor;
	private static boolean manualDem=true;
	
	public Demonstracao() throws ParseException{
		janelaDem.setSize(WINDOW_SIZE_X , WINDOW_SIZE_Y);
		janelaDem.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WINDOW_SIZE_X / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - WINDOW_SIZE_Y / 2);
		janelaDem.setResizable(false);
		janelaDem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contentor = janelaDem.getContentPane();
		
		contentor.setLayout(new BorderLayout());
		
		//criar painel de paineis para norte
		
		JPanel northPanel = new JPanel();
		//northPanel.setLayout(new GridLayout(3,1));
		northPanel.setLayout(new BorderLayout());
		
		//Criar borders e paineis
		TitledBorder radioBorder1 = BorderFactory.createTitledBorder("Técnica de Detecção/Correcção de Erros");
		TitledBorder radioBorder2 = BorderFactory.createTitledBorder("Tamanho das Tramas");
		TitledBorder dadosBorder = BorderFactory.createTitledBorder("Introdução dos Bits de Dados");
		
		Border inBorder = BorderFactory.createEtchedBorder();
		
		JPanel botoesRadio1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel botoesRadio2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		botoesRadio1.setBorder(BorderFactory.createCompoundBorder(radioBorder1, BorderFactory.createEmptyBorder(1,1,1,1)));
		botoesRadio2.setBorder(BorderFactory.createCompoundBorder(radioBorder2, BorderFactory.createEmptyBorder(1,1,1,1)));
		
		//inner panel para dados
		JPanel dadosPanel = new JPanel();
		dadosPanel.setBorder(BorderFactory.createCompoundBorder(dadosBorder, BorderFactory.createEmptyBorder(1,1,1,1)));
		
		JPanel manualPanel = new JPanel(new GridLayout(5,1));
		manualPanel.setBorder(BorderFactory.createCompoundBorder(inBorder, BorderFactory.createEmptyBorder(1,1,1,1)));
		
		JPanel autoPanel = new JPanel(new GridLayout(3,1));
		autoPanel.setBorder(BorderFactory.createCompoundBorder(inBorder, BorderFactory.createEmptyBorder(1,1,1,1)));

		dadosPanel.setLayout(new GridLayout(1,2));
		dadosPanel.add(manualPanel);
		dadosPanel.add(autoPanel);
		
		
		//painel de tecnica det/cor erros
		JRadioButton bitParidade = new JRadioButton("Bit Paridade");
		bitParidade.setSelected(true);
		JRadioButton crc = new JRadioButton("CRC G23(x)");
		JRadioButton hamming = new JRadioButton("Código de Hamming");
		
		ButtonGroup radioGroup1 = new ButtonGroup();
		radioGroup1.add(bitParidade);
		radioGroup1.add(crc);
		radioGroup1.add(hamming);
		
		botoesRadio1.add(bitParidade);
		botoesRadio1.add(crc);
		botoesRadio1.add(hamming);
		
		bitParidade.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					errorMethod=0;
					if(numBits == 4){
						formatBits2.setMask("#####");
					} else { //11
						formatBits2.setMask("###########");
					}
				} catch(Exception ex){}
			}
		});
		crc.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					errorMethod=1;
					if(numBits == 4){
						formatBits2.setMask("########");
					} else { //11
						formatBits2.setMask("###############");
					}
				} catch(Exception ex){}
			}
		});
		hamming.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					errorMethod=2;
					if(numBits == 4){
						formatBits2.setMask("#######");
					} else { //11
						formatBits2.setMask("###############");
					}
				} catch(Exception ex){}
			}
		});
		
		//painel de tamanho de tramas
		JRadioButton fourBits = new JRadioButton("4 Bits");
		fourBits.setSelected(true);
		JRadioButton elevenBits = new JRadioButton("11 Bits");
		
		ButtonGroup radioGroup2 = new ButtonGroup();
		radioGroup2.add(fourBits);
		radioGroup2.add(elevenBits);
		
		botoesRadio2.add(fourBits);
		botoesRadio2.add(elevenBits);
		
		fourBits.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					formatBits1.setMask("####");
					if(errorMethod == 0){
						formatBits2.setMask("#####");
					} else if(errorMethod == 1){
						formatBits2.setMask("########");
					} else {
						formatBits2.setMask("#######");
					}
					formattedManText1.setValue("");
					formattedManText2.setValue("");
					numBits = 4;
				} catch (ParseException e1) {}
			}
		});
		
		elevenBits.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					formatBits1.setMask("###########");
					if(errorMethod == 0){
						formatBits2.setMask("############");
					} else if(errorMethod == 1){
						formatBits2.setMask("###############");
					} else {
						formatBits2.setMask("###############");
					}
					formattedManText1.setValue("");
					formattedManText2.setValue("");
					numBits = 11;
				} catch (ParseException e1) {}
			}
		});
		
		//painel de opcoes manual/auto
		JRadioButton manualRadio = new JRadioButton("Manual");
		manualRadio.setSelected(true);
		JRadioButton autoRadio = new JRadioButton("Aleatório");
		
		
		//manual
		manualPanel.add(manualRadio);
		
		JLabel manText1Label = new JLabel("Bits de Dados:");
		JLabel manText2Label = new JLabel("Padrão de Erros:");
		formatBits1.setMask("####");
		formatBits1.setValidCharacters("01");
		formatBits2.setMask("#####");
		formatBits2.setValidCharacters("01");
		formattedManText1 = new JFormattedTextField(formatBits1);
		formattedManText2 = new JFormattedTextField(formatBits2);
		
		manualPanel.add(manText1Label);
		manualPanel.add(formattedManText1);
		
		manualPanel.add(manText2Label);
		manualPanel.add(formattedManText2);

		//auto
		autoPanel.add(autoRadio);
		autoText1Label = new JLabel("Probabilidade de Erro por Bit (50%):");
		autoSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		autoSlider.setEnabled(false);
		autoSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    autoText1Label.setText("Probabilidade de Erro por Bit ("+source.getValue()+"%):");
			    if (!source.getValueIsAdjusting()) {
			    	errorProbability = (double) source.getValue() / 100.0;
			    }
			    
			}
		});
		
		autoPanel.add(autoText1Label);
		autoPanel.add(autoSlider);
		
		//actionlisteners dos auto/manual
		
		ButtonGroup radioGroup3 = new ButtonGroup();
		radioGroup3.add(manualRadio);
		radioGroup3.add(autoRadio);
		
		manualRadio.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				formattedManText1.setEnabled(true);
				formattedManText2.setEnabled(true);
				autoSlider.setEnabled(false);
				manualDem=true;
			}
		});
		autoRadio.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				formattedManText1.setEnabled(false);
				formattedManText2.setEnabled(false);
				autoSlider.setEnabled(true);
				manualDem=false;
			}
		});
		
		northPanel.add(botoesRadio1,BorderLayout.NORTH);
		northPanel.add(botoesRadio2,BorderLayout.CENTER);
		northPanel.add(dadosPanel,BorderLayout.SOUTH);
		
		
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		
		
		JPanel southPanel = new JPanel();
		
		runButton = new JButton("Run");
		clearButton = new JButton("Clear");
		
		southPanel.add(runButton);
		southPanel.add(clearButton);
		
		runButton.addActionListener(new runListener());
		clearButton.addActionListener(new clearListener());
		
		clearButton.setEnabled(false);
		
		contentor.add(northPanel, BorderLayout.NORTH);
		contentor.add(centerPanel, BorderLayout.CENTER);
		contentor.add(southPanel, BorderLayout.SOUTH);
		
		
		janelaDem.setVisible(true);
	}
	
	private class clearListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			contentor.remove(demMotor);
			contentor.add(centerPanel,BorderLayout.CENTER);
			runButton.setEnabled(true);
			clearButton.setEnabled(false);
			janelaDem.repaint();
			demMotor = null;
		}
	}
	
	private class runListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(manualDem){
				String dados = formattedManText1.getText();
				String padrao = formattedManText2.getText();
				if(dados.endsWith(" ") || padrao.endsWith(" ")){
					return;
				}
				demMotor = new MotorDemonstracao(errorMethod, numBits, dados, padrao);
			} else {
				demMotor = new MotorDemonstracao(errorMethod, numBits, errorProbability);
			}
			contentor.remove(centerPanel);
			contentor.add(demMotor,BorderLayout.CENTER);
			runButton.setEnabled(false);
			clearButton.setEnabled(true);
			janelaDem.revalidate();
		}
	}
}
