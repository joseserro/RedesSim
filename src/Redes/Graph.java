package Redes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;

public class Graph extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private final int PROB_LEN = 9;
	
	private double[] yFourParityExp = new double[PROB_LEN], yElevenParityExp = new double[PROB_LEN];
	private double[] yFourCRCExp = new double[PROB_LEN], yElevenCRCExp = new double[PROB_LEN];
	private double[] yFourHammingExp = new double[PROB_LEN], yElevenHammingExp = new double[PROB_LEN];
	
	private double[] yFourParityThr = new double[PROB_LEN], yElevenParityThr = new double[PROB_LEN];
	private double[] yFourCRCThr = new double[PROB_LEN], yElevenCRCThr = new double[PROB_LEN];
	private double[] yFourHammingThr = new double[PROB_LEN], yElevenHammingThr = new double[PROB_LEN];
	
	private String[] probs = {"10^-6","10^-5","10^-4","10^-3","10^-2","10^-1","1/8","1/4","1/2"};
	
	private int showingMethod = 0;
	
	public void setPlot(int x, double val, int numBits, int errorMethod, boolean exp){
		if(exp){
			if(numBits == 4){
				if(errorMethod == 0){
					yFourParityExp[x] = val;
				} else if(errorMethod == 1){
					yFourCRCExp[x] = val;
				} else if(errorMethod == 2){
					yFourHammingExp[x] = val;
				}
			} else {
				if(errorMethod == 0){
					yElevenParityExp[x] = val;
				} else if(errorMethod == 1){
					yElevenCRCExp[x] = val;
				} else if(errorMethod == 2){
					yElevenHammingExp[x] = val;
				}
			}
		} else {
			if(numBits == 4){
				if(errorMethod == 0){
					yFourParityThr[x] = val;
				} else if(errorMethod == 1){
					yFourCRCThr[x] = val;
				} else if(errorMethod == 2){
					yFourHammingThr[x] = val;
				}
			} else {
				if(errorMethod == 0){
					yElevenParityThr[x] = val;
				} else if(errorMethod == 1){
					yElevenCRCThr[x] = val;
				} else if(errorMethod == 2){
					yElevenHammingThr[x] = val;
				}
			}
		}
		this.repaint();
	}
	
	public void setShowingMethod(int showMeth){
		this.showingMethod = showMeth;
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
		//Random rand = new Random();
		//Color col = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
		g.setColor(Color.BLACK);
		Rectangle size = g.getClipBounds();
		g.fillRect(0,0,size.width,size.height);
		
		int dist = 20;
		
		//teorico: apresentar linha
		if(showingMethod == 0){
			for(int i = 0; i < PROB_LEN; i++){
				int x = dist+(i+1)*((size.width-dist*2)/9);
				if(i != 0){
					int prevx = dist+i*((size.width-dist*2)/9);
					g.setColor(Color.GREEN);
					g.drawLine(prevx, (int)(size.height-dist - (yFourParityThr[i-1])*(size.height-dist*2))+1, x, (int)(size.height-dist - (yFourParityThr[i])*(size.height-dist*2))+1);
					g.setColor(Color.MAGENTA);
					g.drawLine(prevx, (int)(size.height-dist - (yElevenParityThr[i-1])*(size.height-dist*2)), x, (int)(size.height-dist - (yElevenParityThr[i])*(size.height-dist*2)));
				}
				g.setColor(Color.RED);
				g.fillOval(x-2, (int)(size.height-dist - (yFourParityExp[i])*(size.height-dist*2)), 5, 5);
				g.setColor(Color.BLUE);
				g.fillOval(x-2, (int)(size.height-dist - (yElevenParityExp[i])*(size.height-dist*2)), 5, 5);
				
				g.setColor(Color.WHITE);
				g.drawString((int)(yFourParityExp[i]*100)+"%",x-2, (int)(size.height-dist - (yFourParityExp[i])*(size.height-dist*2)));
				g.drawString((int)(yElevenParityExp[i]*100)+"%",x-2, (int)(size.height-dist - (yElevenParityExp[i])*(size.height-dist*2)));
			}
		} else if(showingMethod == 1){
			for(int i = 0; i < PROB_LEN; i++){
				int x = dist+(i+1)*((size.width-dist*2)/9);
				if(i != 0){
					int prevx = dist+i*((size.width-dist*2)/9);
					g.setColor(Color.GREEN);
					g.drawLine(prevx, (int)(size.height-dist - (yFourCRCThr[i-1])*(size.height-dist*2))+1, x, (int)(size.height-dist - (yFourCRCThr[i])*(size.height-dist*2))+1);
					g.setColor(Color.MAGENTA);
					g.drawLine(prevx, (int)(size.height-dist - (yElevenCRCThr[i-1])*(size.height-dist*2)), x, (int)(size.height-dist - (yElevenCRCThr[i])*(size.height-dist*2)));
				}
				g.setColor(Color.RED);
				g.fillOval(x-2, (int)(size.height-dist - (yFourCRCExp[i])*(size.height-dist*2)), 5, 5);
				g.setColor(Color.BLUE);
				g.fillOval(x-2, (int)(size.height-dist - (yElevenCRCExp[i])*(size.height-dist*2)), 5, 5);
				
				g.setColor(Color.WHITE);
				g.drawString((int)(yFourCRCExp[i]*100)+"%",x-2, (int)(size.height-dist - (yFourCRCExp[i])*(size.height-dist*2)));
				g.drawString((int)(yElevenCRCExp[i]*100)+"%",x-2, (int)(size.height-dist - (yElevenCRCExp[i])*(size.height-dist*2)));
			}
		} else if(showingMethod == 2){
			for(int i = 0; i < PROB_LEN; i++){
				int x = dist+(i+1)*((size.width-dist*2)/9);
				if(i != 0){
					int prevx = dist+i*((size.width-dist*2)/9);
					g.setColor(Color.GREEN);
					g.drawLine(prevx, (int)(size.height-dist - (yFourHammingThr[i-1])*(size.height-dist*2))+1, x, (int)(size.height-dist - (yFourHammingThr[i])*(size.height-dist*2))+1);
					g.setColor(Color.MAGENTA);
					g.drawLine(prevx, (int)(size.height-dist - (yElevenHammingThr[i-1])*(size.height-dist*2)), x, (int)(size.height-dist - (yElevenHammingThr[i])*(size.height-dist*2)));
				}
				g.setColor(Color.RED);
				g.fillOval(x-2, (int)(size.height-dist - (yFourHammingExp[i])*(size.height-dist*2)), 5, 5);
				g.setColor(Color.BLUE);
				g.fillOval(x-2, (int)(size.height-dist - (yElevenHammingExp[i])*(size.height-dist*2)), 5, 5);
				
				g.setColor(Color.WHITE);
				g.drawString((int)(yFourHammingExp[i]*100)+"%",x-2, (int)(size.height-dist - (yFourHammingExp[i])*(size.height-dist*2)));
				g.drawString((int)(yElevenHammingExp[i]*100)+"%",x-2, (int)(size.height-dist - (yElevenHammingExp[i])*(size.height-dist*2)));
			}
		}
		
		g.setColor(Color.WHITE);
		g.fillRect(size.width - dist*9, dist, dist*8, dist*2 + 4);
		int fontSize = 9;
		g.setFont(new Font("Arial",Font.PLAIN,fontSize));
		String metd = "Bit Paridade";
		if(showingMethod == 1)
			metd = "CRC";
		if(showingMethod == 2)
			metd = "Hamming";
		g.setColor(Color.BLACK);
		g.drawString(metd+" (4 bits) - Simul.", size.width-dist*9+3, dist+fontSize+1);
		g.drawString(metd+" (4 bits) - Teorico", size.width-dist*9+3, dist+(fontSize+1)*2);
		g.drawString(metd+" (11 bits) - Simul.", size.width-dist*9+3, dist+(fontSize+1)*3);
		g.drawString(metd+" (11 bits) - Teorico", size.width-dist*9+3, dist+(fontSize+1)*4);
		
		g.setColor(Color.BLACK);
		g.fillRect(size.width-dist*2-6, dist+1, dist+5, dist*2+2);
		
		g.setColor(Color.RED);
		g.fillOval(size.width-dist*2+3, dist+fontSize-4, 5, 5);
		g.setColor(Color.BLUE);
		g.fillOval(size.width-dist*2+3, dist+(fontSize+1)*3-5, 5, 5);
		g.setColor(Color.GREEN);
		g.drawLine(size.width-dist*2-2, dist+(fontSize+1)*2-3, size.width-dist*2+13, dist+(fontSize+1)*2-3);
		g.setColor(Color.MAGENTA);
		g.drawLine(size.width-dist*2-2, dist+(fontSize+1)*4-3, size.width-dist*2+13, dist+(fontSize+1)*4-3);
		
		g.setColor(Color.WHITE);
		g.drawLine(dist*2, dist, dist*2, size.height-dist);
		g.drawLine(dist*2, size.height-dist, size.width-dist, size.height-dist);
		
		g.drawString("100%", dist/2, dist);
		g.drawString("50%", dist/2+6, (size.height-dist)/2);
		g.drawString("0%", dist/2+9, size.height-dist);
		
		for(int i = 0; i < PROB_LEN; i++){
			int x = dist+(i+1)*((size.width-dist*2)/9);
			g.drawLine(x, size.height-dist, x, size.height-dist+5);
			g.drawString(probs[i], x,size.height-4);
		}
	}
}
