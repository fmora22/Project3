import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread {

	Socket socketClient;
	ObjectOutputStream out;
	ObjectInputStream in;
	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call) {
		callback = call;
	}

	@Override
	public void run() {
		try {
			socketClient = new Socket("127.0.0.1", 5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);

			while (true) {
				Message message = (Message) in.readObject(); // Cast any received object to Message
				callback.accept(message); //  message object
			}
		} catch (Exception e) {
			e.printStackTrace(); // Handle exceptions
		}
	}

	public void send(Message data) { // Accept a Message object
		try {
			out.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace(); // Handle exceptions
		}
	}
}
