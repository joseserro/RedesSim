package Redes;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

	private static JFrame menuInicial = new JFrame("Redes Digitais - Trab 1");
	private static final int WINDOW_SIZE_X = 290, WINDOW_SIZE_Y = 90;
	
	public static void main(String[] args) throws Exception {
		menuInicial.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
	 	menuInicial.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WINDOW_SIZE_X / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - WINDOW_SIZE_Y / 2);
		menuInicial.setResizable(false);
		menuInicial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Container contentor = menuInicial.getContentPane();
		contentor.setLayout(new FlowLayout());
		
		JLabel modo = new JLabel("     Escolha o Modo pretendido:     ");
		
		JButton simulacao = new JButton("Simulação");
		JButton demonstracao = new JButton("Demonstração");
		
		contentor.add(modo);
		contentor.add(simulacao);
		contentor.add(demonstracao);
		
		menuInicial.setVisible(true);
		
		simulacao.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menuInicial.setVisible(false);
				try {
					new Simulacao();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		demonstracao.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menuInicial.setVisible(false);
				try {
					new Demonstracao();
				} catch (Exception e1) {
				}
			}
		});
		
		//TESTING
		
		//System.out.println("So 2: "+HammingUtil.getDistance("010110110", "110100110"));
	}
	
	
}
