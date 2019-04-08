package cube.semantics;

public class Output implements Runnable{
	private String message;
	
	public Output(String m) {
		message =  m;
	}
	public void run() {
		String messageCopy = message;
		while(true) {
			if(message!= messageCopy) {
				System.out.println(message);
				messageCopy = message;
			}
		}
	}
}
