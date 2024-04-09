import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	
	
	Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}

			public void updateClients(Message message) {
				for (ClientThread t : clients) {
					try {
						t.out.writeObject(message);
					} catch (Exception e) {
						// Handle the exception (possibly disconnect the client and remove from list)
					}
				}
			}

//			public void run(){
//
//				try {
//					in = new ObjectInputStream(connection.getInputStream());
//					out = new ObjectOutputStream(connection.getOutputStream());
//					connection.setTcpNoDelay(true);
//				}
//				catch(Exception e) {
//					System.out.println("Streams not open");
//				}
//
//				updateClients("new client on server: client #"+count);
//
//				 while(true) {
//					    try {
//					    	String data = in.readObject().toString();
//					    	callback.accept("client: " + count + " sent: " + data);
//					    	updateClients("client #"+count+" said: "+data);
//
//					    	}
//					    catch(Exception e) {
//					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
//					    	updateClients("Client #"+count+" has left the server!");
//					    	clients.remove(this);
//					    	break;
//					    }
//					}
//				}//end of run
			public void run() {
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);
				} catch (Exception e) {
					// Handle exceptions
				}

				// Initial message to all clients about a new connection
				updateClients(new Message(Message.TYPE_BROADCAST, "Server", "Welcome client #" + count));

				while (true) {
					try {
						Message data = (Message) in.readObject();
						// Process the message based on its type based on # ( broadcast, individual, group)
						// for a broadcast message:
						updateClients(data); // This would need to be adjusted based on message type
					} catch (Exception e) {
						// Handle disconnection, remove client from list?
						break;
					}
				}
			}
			
		}//end of client thread
}


	
	

	
