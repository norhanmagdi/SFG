package application;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	private static Integer NumOfNodes;

	public static int getNumOfNodes() {
		return NumOfNodes;
	}

	public static void setNumOfNodes(Integer n) {
		NumOfNodes = n;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Controller.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Signal Flow Graph");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			NumOfNodes = Integer.parseInt(JOptionPane.showInputDialog("Enter Number of Nodes!"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Number not found!", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		launch(args);
	}
}
