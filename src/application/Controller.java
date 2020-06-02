package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;

import myGraph.Graph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller {
	@FXML
	private Canvas canvas;
	@FXML
	private TextField result;
	@FXML
	private Label status;
	@FXML
	private TextField firstTxt;
	@FXML
	private TextField secondTxt;
	@FXML
	private TextField weight;
	@FXML
	private Button overAllGain;

	private static Graph graph;
	private static ArrayList<String> forwardPaths;
	private static ArrayList<String> loops;
	private static ArrayList<ArrayList<String>> nonTouchedLoops;
	private static double[] deltaForward;
	private static double Delta;
	private static boolean calculated;
	private static Map<Integer, Double> gainPaths;
	private static Map<Integer, Double> gainLoops;


	public static ArrayList<String> getForward() {
		return forwardPaths;
	}

	public static ArrayList<String> getloops() {
		return loops;
	}

	public static ArrayList<ArrayList<String>> getNontouching() {
		return nonTouchedLoops;
	}

	public static Graph getGraph() {
		return graph;
	}

	public static double[] getDeltaForward() {
		return deltaForward;
	}

	public static Map<Integer, Double> getGainPathes() {
		return gainPaths;
	}

	public static Map<Integer, Double> getGainLoops() {
		return gainLoops;
	}

	public static double getDelta() {
		return Delta;
	}


	@FXML
	public void initialize() {
		if (Main.getNumOfNodes() >= 10) {
			canvas.setHeight(Main.getNumOfNodes() * 50);
		} else {
			canvas.setHeight(400);
		}
		canvas.setWidth(Main.getNumOfNodes() * 100);
		GraphicsContext g = canvas.getGraphicsContext2D();
		double x = 25;
		double y = canvas.getHeight() / 2.0;
		for (int i = 0; i < Main.getNumOfNodes(); i++) {
			g.setFill(Color.PLUM);
			g.fillOval(x, y - 50, 50, 50);
			g.setFill(Color.BLACK);
			g.fillText(String.valueOf(i + 1), x + 22, y - 20);
			x = (i + 1) * 100 + 25;
		}
		graph = new Graph(Main.getNumOfNodes(), Main.getNumOfNodes() - 1);
	}

	public void addEdge(ActionEvent event) {
			if (firstTxt.getText() == null || secondTxt.getText() == null || weight.getText() == null) {
				status.setText("Invalid Input");
				status.setTextFill(Color.RED);
			} else {
				try {
					graph.addEdge(graph, Integer.valueOf(firstTxt.getText()) - 1,
							Integer.valueOf(secondTxt.getText()) - 1, Double.valueOf(weight.getText()));
					status.setText(null);
					drawEdge(Integer.valueOf(firstTxt.getText()), Integer.valueOf(secondTxt.getText()),
							Double.valueOf(weight.getText()));
				} catch (Exception e) {
					status.setText("Invalid Input");
					status.setTextFill(Color.RED);
				}
			}

	}

	public void solve(ActionEvent event) {
		graph.paths(graph,0);
		graph.loops(graph);
		result.setText(String.valueOf(graph.overallGain(graph)));
		forwardPaths = graph.getForwardPaths();
		loops = graph.getloops();
		nonTouchedLoops = graph.getNonTouchedLoops();
		gainPaths = graph.getGainPathes();
		gainLoops = graph.getGainLoops();
		Delta = graph.getDelta();
		deltaForward = graph.getDeltaForward();
		calculated=true;
	}

	private void drawEdge(int src, int des, double weight) {
		double x1 = (src - 1) * 100 + 25 + 20;
		double x2 = (des - 1) * 100 + 25 + 20;
		double y2;
		double y1;
		if (des < src) {
			y2 = (canvas.getHeight() / 2.0);
			y1 = Math.abs(y2 + (((x1 - x2) / 2.0) * Math.tan((1 * Math.PI) / 6.0)));
		} else {
			y2 = (canvas.getHeight() / 2.0) - 50;
			y1 = Math.abs(y2 - (((x2 - x1) / 2.0) * Math.tan((1 * Math.PI) / 6.0)));
		}
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.beginPath();
		g.moveTo(x1, y2);
		g.quadraticCurveTo(((x2 - x1) / 2.0) + x1, y1, x2, y2);
		g.stroke();
		g.closePath();
		if (des < src) {
			double y3 = y1 - 0.5 * (y1 - y2);
			double[] x = new double[] { ((x1 - x2) / 2.0) + x2, ((x1 - x2) / 2.0) + x2 + 5,
					((x1 - x2) / 2.0) + x2 + 5 };
			double[] y = new double[] { y3, y3 + 5, y3 - 5 };
			g.fillPolygon(x, y, 3);
			g.fillText(String.valueOf(weight), ((x1 - x2) / 2.0) + x2, y3 + 15);
		} else {
			double y3 = y1 + 0.5 * (y2 - y1);
			double[] x = new double[] { ((x2 - x1) / 2.0) + x1, ((x2 - x1) / 2.0) + x1 - 5,
					((x2 - x1) / 2.0) + x1 - 5 };
			double[] y = new double[] { y3, y3 + 5, y3 - 5 };
			g.fillPolygon(x, y, 3);
			g.fillText(String.valueOf(weight), ((x2 - x1) / 2.0) + x1, y3 - 15);
		}
	}

	public void showDetail(ActionEvent event) throws IOException {
		if(calculated){
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("ShowDetail.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Signal Flow Graph Details");
			primaryStage.setResizable(false);
			primaryStage.show();
		}
		else
			JOptionPane.showMessageDialog(null, "solve graph first!", "Error", JOptionPane.ERROR_MESSAGE);


	}

	public void newGraph(ActionEvent event) {
		calculated=false;
		Integer n;
		try {
			n = Integer.parseInt(JOptionPane.showInputDialog("Enter Number of Nodes!"));
			if (n >= 80) {
				JOptionPane.showMessageDialog(null, "Number too large!", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			Main.setNumOfNodes(n);
			canvas.getGraphicsContext2D().clearRect(canvas.getLayoutX(), canvas.getLayoutY(), canvas.getWidth(),
					canvas.getHeight());
			initialize();
			firstTxt.setText(null);
			secondTxt.setText(null);
			weight.setText(null);
			result.setText(null);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Number not found!", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
}
