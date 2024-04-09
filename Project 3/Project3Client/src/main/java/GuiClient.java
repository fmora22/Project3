import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;


public class GuiClient extends Application{

	//starter code attributes
	TextField textBox;
	Button button1;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	Client clientConnection;
	
	ListView<String> listItems2;

	//scene refrence
	private Stage primaryStage;


	public static void main(String[] args) {
		launch(args);
	}

	public Scene createClientGui() {

		clientBox = new VBox(10, textBox, button1, listItems2);
		clientBox.setStyle("-fx-background-color: blue;"+"-fx-font-family: 'serif';");
		return new Scene(clientBox, 400, 300);
		
	}

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage; // assign the primary stage (we can reference it from anywhere to facilate switch)
		clientConnection = new Client(data -> {
			Platform.runLater(() -> {
				if (data instanceof Message) {
					Message message = (Message) data;
					switch (message.getMessageType()) {
						case Message.TYPE_USERNAME_TAKEN:
							showAlert("Username is already taken, please choose another.");
							break;
						case Message.TYPE_USERNAME_ACCEPTED:
							primaryStage.setScene(createMainChatScene()); // Transition to the main chat scene
							primaryStage.setTitle("Chat Application"); // Update window title
							break;
						// other cases for other message types ?
					}
				}
			});
		});

		clientConnection.start();

		// Set up the sign-in page
		primaryStage.setScene(createSignInScene());
		primaryStage.setTitle("Client - Sign In");
		primaryStage.show();
	}

	public Scene createSignInScene() {
		VBox signInBox = new VBox(10);
		signInBox.setAlignment(Pos.CENTER); // Centers the VBox contents

		// Style the container
		signInBox.setStyle("-fx-background-color: #c0c0c0;" + "-fx-padding: 20;");

		// Username field
		TextField usernameField = new TextField();
		usernameField.setPromptText("Username");
		usernameField.setMaxWidth(200);

		// Password field
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.setMaxWidth(200);

		// Sign in button
		Button signInButton = new Button("Sign In");
		signInButton.setOnAction(e -> {
			String username = usernameField.getText();
			// Password is not used for now?? take it awa?
			Message message = new Message(Message.TYPE_USERNAME_CHECK, username, ""); // No password, empty string?
			clientConnection.send(message);
		});



		// Add elements to the sign-in box
		signInBox.getChildren().addAll(new Label("Welcome! Sign in below"), usernameField, passwordField, signInButton);

		// Container for sign-in box  to center it on the screen
		BorderPane mainLayout = new BorderPane();
		mainLayout.setCenter(signInBox);
		mainLayout.setStyle("-fx-background-color: white;"); // Sets the outer background to white

		// Return the scene containing this layout
		return new Scene(mainLayout, 300, 200);
	}

	//Defualt page after sign in
	public Scene createMainChatScene() {
		VBox chatBox = new VBox(10);
		chatBox.setStyle("-fx-background-color: #c0c0c0;");

		return new Scene(chatBox, 600, 400); // test chat scene
	}



	//Username Error Alert
	public void showAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error - username taken");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
