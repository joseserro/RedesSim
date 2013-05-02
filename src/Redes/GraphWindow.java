package Redes;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraphWindow {
	private static final int WINDOW_SIZE_X = 600, WINDOW_SIZE_Y = 500;
	
	private JFrame janelaGraph;
	private Container contentor;
	
	private JButton prevGraph, nextGraph;
	private JLabel text;
	
	private ArrayList<Graph> graficos = new ArrayList<Graph>();
	private ArrayList<String> nomesGraficos = new ArrayList<String>();
	
	private int currentGraph = 0;
	
	public GraphWindow(){
		janelaGraph = new JFrame("Graficos");
		janelaGraph.setSize(WINDOW_SIZE_X , WINDOW_SIZE_Y);
		janelaGraph.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 + 40, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - WINDOW_SIZE_Y / 2 - 20);
		janelaGraph.setResizable(false);
		janelaGraph.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		contentor = janelaGraph.getContentPane();
		
		contentor.setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel();
		
		northPanel.setLayout(new BorderLayout());
		
		prevGraph = new JButton(" < ");
		prevGraph.setEnabled(false);
		nextGraph = new JButton(" > ");
		
		graficos.add(new Graph());
		graficos.add(new Graph());
		graficos.add(new Graph());
		graficos.add(new Graph());
		
		nomesGraficos.add("Transmissão Sem Erros");
		nomesGraficos.add("Bits Errados");
		nomesGraficos.add("Não Detecção de Erros Sabendo que Houve");
		nomesGraficos.add("Correcção Correcta");
		
		text = new JLabel(nomesGraficos.get(currentGraph));
		text.setHorizontalAlignment(JLabel.CENTER);
		
		prevGraph.addActionListener(new prevListener());
		nextGraph.addActionListener(new nextListener());
		
		northPanel.add(prevGraph, BorderLayout.WEST);
		northPanel.add(nextGraph, BorderLayout.EAST);
		northPanel.add(text, BorderLayout.CENTER);
		
		contentor.add(northPanel, BorderLayout.NORTH);
		contentor.add(graficos.get(currentGraph), BorderLayout.CENTER);
		
		janelaGraph.setVisible(true);
		
	}
	
	public void setGraphMethod(int meth){
		for(Graph g : graficos){
			g.setShowingMethod(meth);
		}
	}
	
	public void setGraphVar(int graphIndex, int x, double val, int numBits, int errorMethod, boolean exp){
		graficos.get(graphIndex).setPlot(x, val, numBits, errorMethod, exp);
	}
	
	private class prevListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			contentor.remove(graficos.get(currentGraph));
			if(currentGraph == 1){
				prevGraph.setEnabled(false);
			}
			nextGraph.setEnabled(true);
			currentGraph--;
			text.setText(nomesGraficos.get(currentGraph));
			contentor.add(graficos.get(currentGraph),BorderLayout.CENTER);
			janelaGraph.repaint();
		}
	}
	
	private class nextListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			contentor.remove(graficos.get(currentGraph));
			if(currentGraph == 2){
				nextGraph.setEnabled(false);
			}
			prevGraph.setEnabled(true);
			currentGraph++;
			text.setText(nomesGraficos.get(currentGraph));
			contentor.add(graficos.get(currentGraph),BorderLayout.CENTER);
			janelaGraph.repaint();
		}
	}
}
