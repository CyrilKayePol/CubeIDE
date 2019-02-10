import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import cube.gui.Cube;
import cube.gui.SplashScreenMain;

public class Main {
	public static void main(String[] args) {
		 try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		 catch (Exception e) { e.printStackTrace();}
		new SplashScreenMain();
		
		JFrame frame = new JFrame();
		Cube cube = new Cube();

        frame.add(cube);
		frame.setJMenuBar(cube.createMenuBar());		

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        frame.setVisible(true);
		
		ImageIcon icon = new ImageIcon("src/images/icon.png");
		frame.setIconImage(icon.getImage());
	}
}
