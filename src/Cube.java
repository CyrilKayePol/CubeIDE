
import java.awt.*;
import javax.swing.*;

public class Cube extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel sidePanel, topPanel1, topPanel2, centerPanel;
	private JLabel topPanel1Labels[] = new JLabel[4];
	
	public Cube() {
		this.setLayout(null);
		this.setPreferredSize(new Dimension(1400,700));
		this.setBackground(new Color(250,250,250));
		
		init();
		createSidePanel();
		createTopPanel();
		createCenterPanel();
	}
	
	private void init() {
		sidePanel = new JPanel();
		topPanel1 = new JPanel();
		topPanel2 = new JPanel();
		centerPanel = new JPanel();
		String topPanel1Names[] = {"File", "Search", "Edit", "View"};
		
		for(int a = 0;a<4;a++) {
			topPanel1Labels[a] = new JLabel(topPanel1Names[a]);
		}
	}
	
	private void createSidePanel() {
		sidePanel.setPreferredSize(new Dimension(200, 700));
		sidePanel.setBackground(Color.BLUE);
		sidePanel.setBounds(0,50,200,700);
		this.add(sidePanel);
	}
	
	private void createTopPanel() {
		topPanel1.setPreferredSize(new Dimension(1400, 20));
		topPanel1.setBackground(Color.PINK);
		topPanel1.setBounds(0,0,1400,20);
		topPanel1.setLayout(null);
		this.add(topPanel1);
		
		int start = 5;
		for(int a = 0; a<4; a++) {
			if(a == 1) {
				topPanel1Labels[a].setBounds(start, 0, 50, 20);
				start+=50;
			}else {
				topPanel1Labels[a].setBounds(start, 0, 30, 20);
				start+=30;
			}
			topPanel1Labels[a].setFont(new Font("Arial", Font.PLAIN, 12));
			topPanel1.add(topPanel1Labels[a]);
		}
		
		topPanel2.setPreferredSize(new Dimension(1400, 30));
		topPanel2.setBackground(Color.ORANGE);
		topPanel2.setBounds(0,20,1400,30);
		this.add(topPanel2);
	}
	
	private void createCenterPanel() {
		centerPanel.setPreferredSize(new Dimension(1200,700));
		centerPanel.setBackground(Color.GREEN);
		centerPanel.setBounds(200,50,1200,700);
		this.add(centerPanel);
	}
}
