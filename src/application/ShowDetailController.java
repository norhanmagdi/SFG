package application;

import java.util.ArrayList;
import java.util.Map;

import myGraph.Graph;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ShowDetailController {
	@FXML
	private TextArea forwardPathsText;
	@FXML
	private TextArea loopsText;
	@FXML
	private TextArea nonTouchingText;
	@FXML
	private TextArea deltasText;

	private Graph graph;
	private ArrayList<String> forwardPaths;
	private ArrayList<String> loops;
	private ArrayList<ArrayList<String>> nonTouchingLoops;
	private double[] deltaForward;
	private double Delta;
	private Map<Integer, Double> gainPaths;
	private Map<Integer, Double> gainLoops;

	@FXML
	public void initialize() {
		graph = Controller.getGraph();
		forwardPaths = Controller.getForward();
		loops = Controller.getloops();
		nonTouchingLoops = Controller.getNontouching();
		deltaForward = Controller.getDeltaForward();
		Delta = Controller.getDelta();
		gainPaths=Controller.getGainPathes();
		gainLoops = Controller.getGainLoops();
		for (int i = 0; i < forwardPaths.size(); i++) {
			String[] ss = forwardPaths.get(i).split(",");
			String s = "";
			for (int x = 0; x < ss.length; x++) {
				s = s + (Integer.valueOf(ss[x]) + 1);
				if (x < ss.length - 1) {
					s = s + ",";
				}
			}
			forwardPathsText.appendText("M" + (i + 1) + " = " + s + " = " + gainPaths.get(i) + "\n");
		}
		for (int i = 0; i < loops.size(); i++) {
			String[] ss = loops.get(i).split(",");
			String s = "";
			for (int x = 0; x < ss.length; x++) {
				s = s + (Integer.valueOf(ss[x]) + 1);
				if (x < ss.length - 1) {
					s = s + ",";
				}
			}
			loopsText.appendText("L" + (i + 1) + " = " + s + " = " + gainLoops.get(i) + "\n");
		}
		for (int i = 0; i < nonTouchingLoops.size(); i++) {
			ArrayList<String> arr = nonTouchingLoops.get(i);
			for (int j = 0; j < arr.size(); j++) {
				String[] ss = arr.get(j).split(",");
				String s = "";
				for (int x = 0; x < ss.length; x++) {
					s = s + (Integer.valueOf(ss[x]) + 1);
					if (x < ss.length - 1) {
						s = s + ",";
					}
				}
				if (j < arr.size() - 1) {
					nonTouchingText.appendText(s + " & ");
				} else {
					nonTouchingText.appendText(s + "\n");
				}
			}
		}
		deltasText.appendText("Delta = " + Delta + "\n");
		for (int i = 0; i < deltaForward.length; i++) {
			deltasText.appendText("Delta(" + (i + 1) + ") = " + deltaForward[i] + "\n");
		}
	}
}
