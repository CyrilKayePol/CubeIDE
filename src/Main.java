import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;

public class Main {
	public static void main(String[] args) {
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
