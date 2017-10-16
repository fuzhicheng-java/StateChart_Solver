import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Tree extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2707712944901661771L;

	public Tree(String title) {
		super(title);
	}

	public Tree(String title, mxGraph graph) {
		super("Hello, World!");

		graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			Object v1 = graph.insertVertex(parent, null, "Hello dsdsdssssss sdsdsddddddddd", 20, 20, 80, 30);
			Object v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30);
			graph.insertEdge(parent, null, "Edge", v1, v2);
		} finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}

	public static void main(String[] args) {
		Tree frame = new Tree("test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);
	}

	public void initVariableImpactTreeStructure(Statechart st, String name) {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			int x = 20;
			int y = 20;

			this.travVriables(st, graph, parent, null, name, x, y);
		} finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 320);
		this.setVisible(true);
		
	}

	public void travVriables(Statechart st, mxGraph graph, Object parent, Object to, String name, int x, int y) {
		if (st.variables.size() > 0) {
			for (Variable var : st.variables) {
				// System.out.println(var.name);
				if (var.name.equals(name)) {
					int width = var.name.length() + 5;
					Object v1 = graph.insertVertex(parent, null, var.name, x, y, width, 30);
					if(to!=null)
					{
						graph.insertEdge(parent, null, "", v1, to);
					}
					if (var.used_variables.size() > 0) {
						for (UpdatedVariable upvar : var.used_variables) {
							if (upvar.usedVariables.size() > 0) {
								for (String tname : upvar.usedVariables) {
									travVriables(st, graph, parent, v1, tname, x, y + 30);
									x+=tname.length() + 25;
								}
							}

						}
					}

				}
			}
		}
	}
}
