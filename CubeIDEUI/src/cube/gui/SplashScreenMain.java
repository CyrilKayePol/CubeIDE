package cube.gui;

import javax.swing.ImageIcon;

public class SplashScreenMain {

  private SplashScreen screen;

  public SplashScreenMain() {
    splashScreenInit();
    for (int i = 0; i <= 100; i++)
    {
    	try {
			Thread.sleep(80);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	screen.setProgress("Loading Cube IDE " + i + "%", i);  // progress bar with a message
    }
    splashScreenDestruct();
  }

  private void splashScreenDestruct() {
    screen.setScreenVisible(false);
    screen.dispose();
  }

  private void splashScreenInit() {
    ImageIcon myImage = new ImageIcon("src/images/icon.png");
    screen = new SplashScreen(myImage);
    screen.setLocationRelativeTo(null);
    screen.setProgressMax(100);
    screen.setScreenVisible(true);
  }

}