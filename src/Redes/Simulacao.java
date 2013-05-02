package Redes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

public class Simulacao {
	private static JFrame janelaSim = new JFrame("Simulação");
	private static final int WINDOW_SIZE_X = 550, WINDOW_SIZE_Y = 700, NUM_TRAMAS = 10000000;
	private static int selectedProb = 0;
	private static JButton runButton, clearButton, stopButton;
	private static JPanel centerPanel;
	private static Container contentor;
	private static int errorMethod = 0, numBits = 4;
	private static MotorSim simMotor;
	private static GraphWindow graph;
	
	public Simulacao() throws ParseException{
		janelaSim.setSize(WINDOW_SIZE_X , WINDOW_SIZE_Y);
		janelaSim.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WINDOW_SIZE_X, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - WINDOW_SIZE_Y / 2 - 20);
		janelaSim.setResizable(false);
		janelaSim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contentor = janelaSim.getContentPane();
		
		contentor.setLayout(new BorderLayout());
		
		//criar painel de paineis para norte
		
		JPanel northPanel = new JPanel();
		//northPanel.setLayout(new GridLayout(3,1));
		northPanel.setLayout(new BorderLayout());
		
		//Criar borders e paineis
		TitledBorder radioBorder1 = BorderFactory.createTitledBorder("Técnica de Detecção/Correcção de Erros");
		TitledBorder radioBorder2 = BorderFactory.createTitledBorder("Tamanho das Tramas");
		TitledBorder dadosBorder = BorderFactory.createTitledBorder("Probabilidade de Erro de bit Independente");
		
		JPanel botoesRadio1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel botoesRadio2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		botoesRadio1.setBorder(BorderFactory.createCompoundBorder(radioBorder1, BorderFactory.createEmptyBorder(1,1,1,1)));
		botoesRadio2.setBorder(BorderFactory.createCompoundBorder(radioBorder2, BorderFactory.createEmptyBorder(1,1,1,1)));
		
		//inner panel para dados
		JPanel dadosPanel = new JPanel();
		dadosPanel.setBorder(BorderFactory.createCompoundBorder(dadosBorder, BorderFactory.createEmptyBorder(1,1,1,1)));

		
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
				errorMethod=0;
				graph.setGraphMethod(errorMethod);
			}
		});
		crc.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				errorMethod=1;
				graph.setGraphMethod(errorMethod);
			}
		});
		hamming.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				errorMethod=2;
				graph.setGraphMethod(errorMethod);
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
				numBits = 4;
			}
		});
		
		elevenBits.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				numBits = 11;
			}
		});
		
		JRadioButton pb1 = new JRadioButton("10^-6");
		pb1.setSelected(true);
		JRadioButton pb2 = new JRadioButton("10^-5");
		JRadioButton pb3 = new JRadioButton("10^-4");
		JRadioButton pb4 = new JRadioButton("10^-3");
		JRadioButton pb5 = new JRadioButton("10^-2");
		JRadioButton pb6 = new JRadioButton("10^-1");
		JRadioButton pb7 = new JRadioButton("1/8");
		JRadioButton pb8 = new JRadioButton("1/4");
		JRadioButton pb9 = new JRadioButton("1/2");
		
		ButtonGroup radioGroup3 = new ButtonGroup();
		radioGroup3.add(pb1);
		radioGroup3.add(pb2);
		radioGroup3.add(pb3);
		radioGroup3.add(pb4);
		radioGroup3.add(pb5);
		radioGroup3.add(pb6);
		radioGroup3.add(pb7);
		radioGroup3.add(pb8);
		radioGroup3.add(pb9);
		
		dadosPanel.add(pb1);
		dadosPanel.add(pb2);
		dadosPanel.add(pb3);
		dadosPanel.add(pb4);
		dadosPanel.add(pb5);
		dadosPanel.add(pb6);
		dadosPanel.add(pb7);
		dadosPanel.add(pb8);
		dadosPanel.add(pb9);
		
		pb1.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) { selectedProb = 0; }});
		pb2.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) { selectedProb = 1; }});
		pb3.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) { selectedProb = 2; }});
		pb4.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) { selectedProb = 3; }});
		pb5.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) { selectedProb = 4; }});
		pb6.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) { selectedProb = 5; }});
		pb7.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) { selectedProb = 6; }});
		pb8.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) { selectedProb = 7; }});
		pb9.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent arg0) { selectedProb = 8; }});
		
		northPanel.add(botoesRadio1,BorderLayout.NORTH);
		northPanel.add(botoesRadio2,BorderLayout.CENTER);
		northPanel.add(dadosPanel,BorderLayout.SOUTH);
		
		
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		
		
		JPanel southPanel = new JPanel();
		
		runButton = new JButton("Run");
		clearButton = new JButton("Clear");
		stopButton = new JButton("Stop");
		
		southPanel.add(runButton);
		southPanel.add(clearButton);
		southPanel.add(stopButton);
		
		runButton.addActionListener(new runListener());
		clearButton.addActionListener(new clearListener());
		stopButton.addActionListener(new stopListener());
		
		clearButton.setEnabled(false);
		stopButton.setEnabled(false);

		contentor.add(northPanel, BorderLayout.NORTH);
		contentor.add(centerPanel, BorderLayout.CENTER);
		contentor.add(southPanel, BorderLayout.SOUTH);
		
		graph = new GraphWindow();
		
		janelaSim.setVisible(true);
	}
	
	public void stoppedSim(){
		stopButton.setEnabled(false);
		janelaSim.repaint();
	}
	
	private class clearListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			simMotor.stop();
			contentor.remove(simMotor);
			contentor.add(centerPanel,BorderLayout.CENTER);
			runButton.setEnabled(true);
			clearButton.setEnabled(false);
			stopButton.setEnabled(false);
			janelaSim.repaint();
			simMotor = null;
		}
	}
	
	private class stopListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			simMotor.stop();
			stopButton.setEnabled(false);
			janelaSim.repaint();
		}
	}
	
	private class runListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			simMotor = new MotorSim(errorMethod, numBits, NUM_TRAMAS, selectedProb, janelaSim, graph,stopButton);
			
			contentor.remove(centerPanel);
			contentor.add(simMotor,BorderLayout.CENTER);
			runButton.setEnabled(false);
			clearButton.setEnabled(true);
			stopButton.setEnabled(true);
			janelaSim.revalidate();
		}
	}
}
