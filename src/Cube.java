
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.*;
import lexer.TextLineNumber;

public class Cube extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel sidePanel, topPanel1, topPanel2, centerPanel;
	private JLabel topPanel1Labels[] = new JLabel[4];
	private JLabel topPanel2Labels[] = new JLabel[4];
	private ImageIcon topPanel2Icons[] = new ImageIcon[4];
	private String topPanel1Names[] = {"File", "Search", "Edit", "View"};
	private String topPanel2IconNamesEnter[] = {"src/images/open_enter.png", "src/images/new_enter.png", "src/images/save_enter.png", "src/images/close_enter.png"};
	private String topPanel2IconNames[] = {"src/images/open.png", "src/images/new.png", "src/images/save.png", "src/images/close.png"};
	private Handler handler;
  private JTextPane textPane;
	private JScrollPane scrollPane;
	private TextLineNumber tln;
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
		handler = new Handler();
		
		for(int a = 0;a<4;a++) {
			topPanel1Labels[a] = new JLabel(topPanel1Names[a]);
			topPanel2Icons[a] = new ImageIcon(topPanel2IconNames[a]);
			topPanel2Labels[a] = new JLabel(topPanel2Icons[a]);
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
		topPanel1.setBounds(0,0,1400,20);
		topPanel1.setLayout(null);
		this.add(topPanel1);
		
		int x = 5;
		for(int a = 0; a<4; a++) {
			if(a == 1) {
				topPanel1Labels[a].setBounds(x, 0, 50, 20);
				x+=50;
			}else {
				topPanel1Labels[a].setBounds(x, 0, 30, 20);
				x+=30;
			}
			topPanel1Labels[a].setFont(new Font("Arial", Font.PLAIN, 12));
			topPanel1Labels[a].addMouseListener(handler);
			topPanel1.add(topPanel1Labels[a]);
			
		}
		
		topPanel2.setPreferredSize(new Dimension(1400, 30));
		topPanel2.setBackground(new Color(180, 236, 250, 70));
		topPanel2.setBounds(0,20,1400,30);
		topPanel2.setLayout(null);
		this.add(topPanel2);
		
		x = 5;
		int y = 0;
		for(int b = 0; b<4; b++) {
			if(b != 0)
				y = 4;
			topPanel2Labels[b].setBounds(x, y, topPanel2Icons[b].getIconWidth(), topPanel2Icons[b].getIconHeight());
			topPanel2Labels[b].addMouseListener(handler);
			topPanel2.add(topPanel2Labels[b]);
			x += topPanel2Icons[b].getIconWidth() + 20;
			
		}
	}
	
	private void createCenterPanel() {
    textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		tln = new TextLineNumber(textPane);
		scrollPane.setRowHeaderView( tln );
		
		scrollPane.setBounds(200,50,1200,700);
    
		this.add(scrollPane);
	}
	
	private class Handler implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(topPanel1Labels[0] == e.getSource()) {
				createFileWindow();
			}else if(topPanel2Labels[0] == e.getSource()) {
        System.out.println("I came here");
				openFiles();
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			for(int a = 0;a<4;a++) {
				if(topPanel1Labels[a] == e.getSource()) {
					topPanel1Labels[a].setText("<html><u>"+topPanel1Names[a]+"</u></html>");
				}else if(topPanel2Labels[a] == e.getSource()) {
					topPanel2Labels[a].setIcon(new ImageIcon(topPanel2IconNamesEnter[a]));
				}
			}
			repaint();
			revalidate();
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			for(int a = 0;a<4;a++) {
				if(topPanel1Labels[a] == e.getSource()) {
					topPanel1Labels[a].setText(topPanel1Names[a]);
				}else if(topPanel2Labels[a] == e.getSource()) {
					topPanel2Labels[a].setIcon(new ImageIcon(topPanel2IconNames[a]));
				}
			}
			repaint();
			revalidate();
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void createFileWindow() {
		JWindow win = new JWindow();
		JPanel pan = new JPanel();
		pan.setBackground(Color.RED);
		win.setSize(300,400);
		win.add(pan);
		win.setLocationRelativeTo(this);
		win.setLocation(20,70);
		win.setVisible(true);
	}
	
	private void openFiles() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
		}
	}
}
