package cube.gui;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Editor extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Editor(){
		JTextPane textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(textPane);
		TextLineNumber tln = new TextLineNumber(textPane);
		scrollPane.setRowHeaderView( tln );
		
		scrollPane.setBounds(5, 5, 900, 500);
		setSize(1000, 550);
		setLayout(null);
		
		getContentPane().add(scrollPane);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}