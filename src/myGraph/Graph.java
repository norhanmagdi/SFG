package myGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Graph {
	private int v;
	private int target;
	private LinkedList<Node> adj[];
	private ArrayList<String> forwardPaths;
	private ArrayList<String> loops;
	private ArrayList<ArrayList<String>> nonTouched;
	private Map<Integer, Double> pathsGain;
	private Map<Integer, Double> loopsGain;
	private double Delta;
	private double[] deltaForward;

	public Graph(int v, int target) {
		this.v = v;
		this.target = target;
		adj = new LinkedList[v];
		for (int i = 0; i < v; i++) {
			adj[i] = new LinkedList<>();
		}
		forwardPaths = new ArrayList<String>();
		pathsGain =new HashMap<>();
		loopsGain=new HashMap<>();
		loops = new ArrayList<String>();
		nonTouched = new ArrayList<ArrayList<String>>();
	}

	public void addEdge(Graph graph, int from, int to, double weight) throws Exception {
		Node n = new Node(weight, to);
		for (int i = 0; i < graph.adj[from].size(); i++) {
			if (graph.adj[from].get(i).getNumOfNode() == to) {
				throw new Exception();
			}
		}
		graph.adj[from].add(n);
	}

	public void paths(Graph graph,int begin) {
		boolean[] visited = new boolean[v];
		String s="";
		pathsDFS(graph,begin, visited, s);
	}

	private void pathsDFS(Graph graph,int begin, boolean[] visited,String s) {
		visited[begin] = true;
		s = s + begin + ",";
		if (begin == target) {
			s = s.substring(0, s.length() - 1);
			forwardPaths.add(s);
			pathsGain.put(forwardPaths.indexOf(s),getGain(graph,s));
			return;
		}
		for(int i=0;i<adj[begin].size();i++){
			Node n = adj[begin].get(i);
			if (!visited[n.getNumOfNode()]) {
				pathsDFS(graph,n.getNumOfNode(), visited, s);
				visited[n.getNumOfNode()] = false;
			}
		}
	}

	public void loops(Graph graph) {
		boolean[] visited = new boolean[v];
		for (int i = 0; i < graph.getSize(); i++) {
			boolean[] nodesVisited = new boolean[v];
			String s = "";
			findingLoops(graph,i, i, visited, nodesVisited, s);
			visited[i] = true;
		}
		nonTouchedLoops(graph);
	}

	private void findingLoops(Graph graph,int from, int to, boolean[] visited, boolean[] nodesVisited, String s) {
		nodesVisited[from] = true;
		s = s + from + ",";
		if (from == to && s.length() > 2) {
			s = s.substring(0, s.length() - 1);
			loops.add(s);
			loopsGain.put(loops.indexOf(s),getGain(graph,s));
			return;
		}
		for(int i=0;i<adj[from].size();i++){
			Node n = adj[from].get(i);
			if ((!nodesVisited[n.getNumOfNode()] || n.getNumOfNode() == to) && !visited[n.getNumOfNode()]) {
				findingLoops(graph,n.getNumOfNode(), to, visited, nodesVisited, s);
				nodesVisited[n.getNumOfNode()] = false;
			}
		}
	}

	public void nonTouchedLoops(Graph graph) {
		for (int i = 0; i < graph.loops.size(); i++) {
			for (int j = i + 1; j < graph.loops.size(); j++) {
				if (checkNonTouched(graph.loops.get(i), graph.loops.get(j))) {
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(graph.loops.get(i));
					temp.add(graph.loops.get(j));
					nonTouched.add((ArrayList<String>) temp.clone());
				}
			}
		}
		for (int i = 0; i < graph.nonTouched.size(); i++) {
			ArrayList<String> twoNonTouched = graph.nonTouched.get(i);
			for (int j = i + 1; j < graph.nonTouched.size(); j++) {
				ArrayList<String> temp = graph.nonTouched.get(j);
				for (int k = 0; k < temp.size(); k++) {
					int counter = -1;
					for (int m = 0; m < twoNonTouched.size(); m++) {
						if (checkNonTouched(temp.get(k), twoNonTouched.get(m))) {
							counter++;
						}
					}
					if (counter == twoNonTouched.size() - 1) {
						ArrayList<String> newNontouched = (ArrayList<String>) twoNonTouched.clone();
						newNontouched.add(temp.get(k));
						if (!checkDuplicates(newNontouched)) {
							nonTouched.add((ArrayList<String>) newNontouched.clone());
						}
					}
				}
			}
		}
	}

	private boolean checkNonTouched(String loop1, String loop2) {
		String[] l1 = loop1.split(",");
		for (int i = 0; i < l1.length; i++) {
			if (loop2.contains(l1[i])) {
				return false;
			}
		}
		return true;
	}

	private boolean checkDuplicates(ArrayList<String> unTouched) {
		int counter = 0;
		for (int i = 0; i < nonTouched.size(); i++) {
			ArrayList<String> temp = nonTouched.get(i);
			if (temp.size() == unTouched.size()) {
				for (int x = 0; x < unTouched.size(); x++) {
					for (int y = 0; y < unTouched.size(); y++) {
						if (unTouched.get(x).equals(temp.get(y))) {
							counter++;
							break;
						}
					}
				}
			}
		}
		return counter == unTouched.size();
	}

	public double overallGain(Graph graph) {
		double overall;
		Delta = graph.calculateDelta(graph);
		deltaForward = new double[forwardPaths.size()];
		double deltaForward;
		double forwardGain = 0;
		for (int i = 0; i < graph.forwardPaths.size(); i++) {
			deltaForward = graph.calculateDeltaForward(graph, graph.forwardPaths.get(i));
			this.deltaForward[i] = deltaForward;
			forwardGain = forwardGain + pathsGain.get(i) * deltaForward;
		}
		overall = forwardGain / Delta;
		return overall;
	}

	public double calculateDelta(Graph graph) {
		double Delta = 1;
		if (loops.size() == 0) {
			return 1;
		}
		double sum = 0;
		for (int i = 0; i < graph.loops.size(); i++) {
			sum = sum + graph.loopsGain.get(i);
		}
		Delta = Delta - sum;
		for (int i = 0; i < graph.nonTouched.size(); i++) {
			ArrayList<String> nonTouchedLoops = graph.nonTouched.get(i);
			double loopGain;
			if (nonTouchedLoops.size() % 2 == 0)
				loopGain = 1;
			else
				loopGain = -1;

			for (int j = 0; j < nonTouchedLoops.size(); j++)
				loopGain = loopGain * getGain(graph, nonTouchedLoops.get(j));

			Delta = Delta + loopGain;
		}
		return Delta;
	}

	public double calculateDeltaForward(Graph graph, String forwardPath) {
		String[] path = forwardPath.split(",");
		double Delta = 1;
		if (loops.size() == 0) {
			return 1;
		}
		double sum = 0;
		for (int i = 0; i < graph.loops.size(); i++) {
			if (check(path, graph.loops.get(i))) {
				sum = sum + graph.getGain(graph, graph.loops.get(i));
			}
		}
		Delta = Delta - sum;
		for (int i = 0; i < graph.nonTouched.size(); i++) {
			ArrayList<String> nonTouchedLoops = graph.nonTouched.get(i);
			double loopGain = 1;
			for (int j = 0; j < nonTouchedLoops.size(); j++) {
				for (int x = 0; x < path.length; x++) {
					if (nonTouchedLoops.get(j).contains(path[x])) {
						loopGain = 0;
						break;
					}
				}
				if (loopGain == 0) {
					break;
				}
			}
			if (loopGain == 0) {
				continue;
			} else {
				if (nonTouchedLoops.size() % 2 == 0) {
					for (int j = 0; j < nonTouchedLoops.size(); j++) {
						loopGain = loopGain * getGain(graph, nonTouchedLoops.get(i));
					}
				} else {
					for (int j = 0; j < nonTouchedLoops.size(); j++) {
						loopGain = loopGain * getGain(graph, nonTouchedLoops.get(i));
					}
					loopGain = loopGain * -1;
				}
			}
			Delta = Delta + loopGain;
		}
		return Delta;
	}

	public double getGain(Graph graph, String path) {
		String[] pathSplited = path.split(",");
		double gain = 1;
		for (int i = 0; i < pathSplited.length - 1; i++) {
			int j = Integer.parseInt(pathSplited[i]);
			for (Node n : graph.adj[j]) {
				if (n.getNumOfNode() == Integer.parseInt(pathSplited[i + 1])) {
					gain = gain * n.getWeight();
				}
			}
		}
		return gain;
	}

	private boolean check(String path[], String loop) {
		for (int i = 0; i < path.length; i++) {
			if (loop.contains(path[i])) {
				return false;
			}
		}
		return true;
	}

	public int getSize() {
		return v;
	}

	public ArrayList<String> getForwardPaths() {
		return forwardPaths;
	}

	public ArrayList<String> getloops() {
		return loops;
	}

	public ArrayList<ArrayList<String>> getNonTouchedLoops() {
		return nonTouched;
	}

	public double getDelta() {
		return Delta;
	}
	public double[] getDeltaForward() {
		return deltaForward;
	}

	public Map<Integer, Double> getGainPathes() {
		return pathsGain;
	}

	public Map<Integer, Double> getGainLoops() {
		return loopsGain;
	}


	class Node {
		private double weight;
		private int numOfNode;

		Node(double weight, int numOfNode) {
			this.weight = weight;
			this.numOfNode = numOfNode;
		}

		public double getWeight() {
			return weight;
		}

		public int getNumOfNode() {
			return numOfNode;
		}

	}
}
