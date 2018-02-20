import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

public class Statechart {

	private static final int supportFactor = 3;
	public LinkedList<State> states = new LinkedList<>();
	public LinkedList<Transition> transitions = new LinkedList<>();
	public HashSet<Event> events = new HashSet<>();
	public LinkedList<Variable> variables = new LinkedList<>();
	public Map<HashSet<String>, Integer> actionMap = new HashMap();
	public LinkedList<Region> regions=new LinkedList<>();
	public LinkedList<String> entryTransition=new LinkedList<>();

	// public LinkedList<ActionSet> actionSets=new LinkedList<>();
	public void addState(State state) {
		this.states.add(state);
	}

	public void addVariable(Variable var) {
		this.variables.add(var);
	}

	public void addEvent(Event event) {
		this.events.add(event);
	}

	public void addTransition(Transition transition) {
		this.transitions.add(transition);
	}

	public void traceFailedPath_Constant(String path) {
		if (path.equals("")) {
			return;
		} else {
			String[] sids = path.split(" ");
			LinkedList<State> test_states = new LinkedList<>();
			for (int i = 0; i < sids.length; i++) {
				String id = sids[i];
				if (this.states.size() > 0) {
					for (State state : this.states) {
						if (state.id.equals(id)) {
							test_states.add(state);
						}
					}
				}
			}

			for (int i = 0; i < test_states.size(); i++) {
				State item_state = test_states.get(i);
				checkUpdatedVariableInState(item_state);
				if (i + 1 < test_states.size() && item_state.incoming_transitions.size() > 0) {
					for (String itran : item_state.incoming_transitions) {
						if (this.transitions.size() > 0) {
							for (Transition tran : this.transitions) {
								if (tran.id.equals(itran) && tran.to_state.equals(item_state.id)
										&& test_states.get(i + 1).id.equals(tran.from_state)) {
									checkUpdatedVariableInTransition(test_states.get(i + 1), tran, item_state);
								}
							}
						}
					}
				}
			}

		}
	}

	public void checkUpdatedVariableInTransition(State pre_state, Transition trans, State state) {

		if (trans != null && trans.updated_variables.size() > 0) {
			for (UpdatedVariable upvar : trans.updated_variables) {
				if (upvar.usedVariables.size() > 0) {
					for (String varname : upvar.usedVariables) {
						if (this.variables.size() > 0) {
							for (Variable var : this.variables) {
								if (var.name.equals(varname)) {
									if (var.isConsant == Variable.constant) {
										System.out.println(
												"Variable " + upvar.name + " has been updated with constant variable "
														+ varname + " in the transition from state " + pre_state.name
														+ " to state " + state.name);
									}
								}
							}
						}
					}
				}
			}

			if (trans.used_variables.size() > 0) {
				for (String varname : trans.used_variables) {
					if (this.variables.size() > 0) {
						for (Variable var : this.variables) {
							if (var.name.equals(varname)) {
								if (var.isConsant == Variable.constant) {
									System.out.println("Constant variable " + varname
											+ " has been used in a conidtional statement in the transition from state "
											+ pre_state.name + " to state " + state.name);
								}
							}
						}
					}
				}
			}

		}
	}

	public void checkUpdatedVariableInState(State state) {

		if (state != null && state.updated_variables.size() > 0) {
			for (UpdatedVariable upvar : state.updated_variables) {
				if (upvar.usedVariables.size() > 0) {
					for (String varname : upvar.usedVariables) {
						if (this.variables.size() > 0) {
							for (Variable var : this.variables) {
								if (var.name.equals(varname)) {
									if (var.isConsant == Variable.constant) {
										System.out.println(
												"Variable " + upvar.name + " has been updated with constant variable "
														+ varname + " in state " + state.name);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void getUnChangedVariablesInfo() {
		if (this.variables.size() > 0) {
			for (Variable var : this.variables) {
				if (!var.beUpdated && var.type == 0) {
					if (var.used_states.size() == 0 && var.used_transitions.size() == 0) {
						System.out.println("The variable " + var.name
								+ " can be treated as constant variable. There is no impacted objects in the statechart.");
					} else {
						System.out.println("The variable " + var.name
								+ " can be treated as constant variable, and the impacted objects are listed as following:");
					}
					if (var.used_states.size() > 0) {

						for (VarRelation item : var.used_states) {
							State state = this.getFullState(item.id);
							if (state != null) {
								if (item.imp_var == null) {

									System.out.println("In the region " + state.domain_name + ", in the state "
											+ state.name + ", variable " + var.name
											+ " is been used in the entry conditional statement");

								} else {
									if (item.imp_var.contains("(") && item.imp_var.contains(")")) {
										System.out.println("In the region " + state.domain_name + ", in the state "
												+ state.name + ", variable " + item.imp_var
												+ " is been updated with the operation " + var.name);
									} else {
										System.out.println("In the region " + state.domain_name + ", in the state "
												+ state.name + ", variable " + item.imp_var
												+ " is been updated with the variable " + var.name);
									}

								}

							}
						}

						// System.out.println("The variable "+var+" can be treated as constant variable,
						// and the impacted objects are listed as following:");

					}

					if (var.used_transitions.size() > 0) {
						for (VarRelation item : var.used_transitions) {
							Transition transition = this.getFullTranistion(item.id);
							if (transition != null) {
								if (item.imp_var == null) {
									System.out.println("In the region " + transition.domain_name
											+ ", on the transition from state " + transition.from_state_name
											+ " to state " + transition.to_state_name + ", variable " + var.name
											+ " is used as part of conditional statement!");
								} else {
									System.out.println("In the region " + transition.domain_name
											+ ", on the transition from state " + transition.from_state_name
											+ " to state " + transition.to_state_name + ", variable " + item.imp_var
											+ " is been updated with the variable " + var.name);
								}
							}
						}
					}
					System.out.println("*************************************************************************");
					System.out.println();

				}
			}
		}
	}

	public void getSingleVariableImpactionInfo(String name) {
		// TODO Auto-generated method stub
		if (this.variables.size() > 0) {
			for (Variable var : this.variables) {
				// System.out.println(var.name);
				if (var.name.equals(name)) {
					if (var.used_states.size() == 0 && var.used_transitions.size() == 0
							&& var.updated_states.size() == 0 && var.updated_transitions.size() == 0) {
						System.out.println("The variable " + var.name + " has no impacted objects in the statechart.");
					} else {
						System.out.println("The variable " + var.name + " impacted objects are listed as following:");
					}

					if (var.updated_states.size() > 0) {

						for (String item : var.updated_states) {
							State state = this.getFullState(item);
							if (state != null) {
								System.out.println("In the region " + state.domain_name + ", in the state " + state.name
										+ ", variable " + var.name + " is been updated");

							}
						}
					}

					if (var.updated_transitions.size() > 0) {

						for (String item : var.updated_transitions) {
							Transition transition = this.getFullTranistion(item);
							if (transition != null) {
								System.out.println("In the region " + transition.domain_name
										+ ", on the transition from state " + transition.from_state_name + " to state "
										+ transition.to_state_name + ", variable " + var.name + " is been updated");

							}
						}
					}

					if (var.used_states.size() > 0) {

						for (VarRelation item : var.used_states) {
							State state = this.getFullState(item.id);
							if (state != null) {
								if (item.imp_var == null) {
									System.out.println("In the region " + state.domain_name + ", in the state "
											+ state.name + ", variable " + var.name
											+ " is been used in the entry conditional statement");
								} else {
									if (item.imp_var.contains("(") && item.imp_var.contains(")")) {
										System.out.println("In the region " + state.domain_name + ", in the state "
												+ state.name + ", variable " + item.imp_var
												+ " is been updated with the operation " + var.name);
									} else {
										System.out.println("In the region " + state.domain_name + ", in the state "
												+ state.name + ", variable " + item.imp_var
												+ " is been updated with the variable " + var.name);
									}

								}

							}
						}

						// System.out.println("The variable "+var+" can be treated as constant variable,
						// and the impacted objects are listed as following:");

					}

					if (var.used_transitions.size() > 0) {
						for (VarRelation item : var.used_transitions) {
							Transition transition = this.getFullTranistion(item.id);
							if (transition != null) {
								if (item.imp_var == null) {
									System.out.println("In the region " + transition.domain_name
											+ ", on the transition from state " + transition.from_state_name
											+ " to state " + transition.to_state_name + ", variable " + var.name
											+ " is used as part of conditional statement!");
								} else {
									System.out.println("In the region " + transition.domain_name
											+ ", on the transition from state " + transition.from_state_name
											+ " to state " + transition.to_state_name + ", variable " + item.imp_var
											+ " is been updated with the variable " + var.name);
								}
							}
						}
					}
					System.out.println("*************************************************************************");
					System.out.println();
					break;
				}
			}

		}
	}

	public void getSingleVariableImpactionInfo_Tree(String name, int start) {
		// TODO Auto-generated method stub
		if (this.variables.size() > 0) {
			for (Variable var : this.variables) {
				// System.out.println(var.name);
				if (var.name.equals(name)) {
					if (var.used_variables.size() == 0 && start == 0) {
						System.out.println("The variable " + var.name + " has not other related variables.");
					} else if (start == 0) {
						System.out.println("The variable " + var.name + " has following related variables:");
					}

					if (var.used_variables.size() > 0) {

						for (String item : var.updated_states) {
							State state = this.getFullState(item);
							if (state != null) {
								System.out.println("In the region " + state.domain_name + ", in the state " + state.name
										+ ", variable " + var.name + " is been updated");

							}
						}
					}

					if (var.updated_transitions.size() > 0) {

						for (String item : var.updated_transitions) {
							Transition transition = this.getFullTranistion(item);
							if (transition != null) {
								System.out.println("In the region " + transition.domain_name
										+ ", on the transition from state " + transition.from_state_name + " to state "
										+ transition.to_state_name + ", variable " + var.name + " is been updated");

							}
						}
					}

					if (var.used_states.size() > 0) {

						for (VarRelation item : var.used_states) {
							State state = this.getFullState(item.id);
							if (state != null) {
								if (item.imp_var == null) {
									System.out.println("In the region " + state.domain_name + ", in the state "
											+ state.name + ", variable " + var.name
											+ " is been used in the entry conditional statement");
								} else {
									if (item.imp_var.contains("(") && item.imp_var.contains(")")) {
										System.out.println("In the region " + state.domain_name + ", in the state "
												+ state.name + ", variable " + item.imp_var
												+ " is been updated with the operation " + var.name);
									} else {
										System.out.println("In the region " + state.domain_name + ", in the state "
												+ state.name + ", variable " + item.imp_var
												+ " is been updated with the variable " + var.name);
									}

								}

							}
						}

						// System.out.println("The variable "+var+" can be treated as constant variable,
						// and the impacted objects are listed as following:");

					}

					if (var.used_transitions.size() > 0) {
						for (VarRelation item : var.used_transitions) {
							Transition transition = this.getFullTranistion(item.id);
							if (transition != null) {
								if (item.imp_var == null) {
									System.out.println("In the region " + transition.domain_name
											+ ", on the transition from state " + transition.from_state_name
											+ " to state " + transition.to_state_name + ", variable " + var.name
											+ " is used as part of conditional statement!");
								} else {
									System.out.println("In the region " + transition.domain_name
											+ ", on the transition from state " + transition.from_state_name
											+ " to state " + transition.to_state_name + ", variable " + item.imp_var
											+ " is been updated with the variable " + var.name);
								}
							}
						}
					}
					System.out.println("*************************************************************************");
					System.out.println();
					break;
				}
			}

		}
	}

	public void initExecutionPattern() {

		if (this.states != null && this.states.size() > 0) {
			for (State st : this.states) {
				if (st.actionSet.size() >= 2 && actionMap.containsKey(st.actionSet)) {
					int value = (int) actionMap.get(st.actionSet) + 1;
					actionMap.put(st.actionSet, value);
					// break;
				} else {
					if (st.actionSet.size() >= 2) {
						actionMap.put(st.actionSet, 1);
					}

				}
			}

		}

		if (this.transitions != null && this.transitions.size() > 0) {

			for (int i = 0; i < this.transitions.size(); i++) {
				Transition tran = this.transitions.get(i);

				if (tran.actionSet.size() >= 2 && actionMap.containsKey(tran.actionSet)) {
					int value = (int) actionMap.get(tran.actionSet) + 1;
					actionMap.put(tran.actionSet, value);
					// break;
				} else {
					if (tran.actionSet.size() >= 2) {
						actionMap.put(tran.actionSet, 1);
					}
					// actionMap.put(tran.actionSet, 1);

				}
			}

		}
	}

	public void generateNodeTrace(State targetState, State entry) {
		if (!targetState.id.equals(entry.id)) {
			if (targetState.incoming_transitions.size() > 0) {
				for (String tid : targetState.incoming_transitions) {
					Transition ts = this.getTransition(tid);
					if (ts!=null&&!ts.from_state.equals(entry.id)) {

						SubNode node = new SubNode();
						State nextstate = this.getState(ts.from_state);
						node.transition = ts;
						node.state = nextstate;
						if (ts.used_events.size() > 0) {
							for (String event : ts.used_events) {
								node.events.add(event);
							}
						}

						if (nextstate.used_events.size() > 0) {
							for (String event : nextstate.used_events) {
								node.events.add(event);
							}
						}
						targetState.nodes.add(node);
						this.generateNodeTrace(nextstate, entry);
					}

				}
			}
		}
	}
	public void validateExecutionPattern() {
		if (this.states != null && this.states.size() > 0) {
			for (State st : this.states) {
				if (st.actionSet.size() >= 2) {

					if (this.actionMap.keySet().size() > 0) {
						String out = "";
						for (HashSet<String> key : this.actionMap.keySet()) {
							int support = this.actionMap.get(key);
							if (support >= Statechart.supportFactor) {
								int sameFactor = key.size() / 2;
								int sameNumber = CollectionUtils.intersection(key, st.actionSet).size();
								if (sameNumber >= sameFactor && sameNumber < key.size()) {

									out += key + "  ";
								}
							}
						}

						if (!out.equals("")) {
							System.out.println("In the state" + st.name + ", the execution pattern " + st.actionSet
									+ " may be not correct compared with pattern " + out);
						}
					}
				}

			}

		}

		if (this.transitions != null && this.transitions.size() > 0) {

			for (Transition tran : this.transitions) {
				if (tran.actionSet.size() >= 2) {

					if (this.actionMap.keySet().size() > 0) {
						String out = "";
						for (HashSet<String> key : this.actionMap.keySet()) {
							int support = this.actionMap.get(key);
							if (support >= Statechart.supportFactor) {
								int sameFactor = key.size() / 2;
								int sameNumber = CollectionUtils.intersection(key, tran.actionSet).size();
								if (sameNumber >= sameFactor && sameNumber < key.size()) {

									out += key + "  ";
								}
							}
						}
						if (!out.equals("")) {
							Transition newT = this.getFullTranistion(tran.id);
							System.out.println("In the transition from state " + newT.from_state_name + " to state "
									+ newT.to_state_name + ", the execution pattern " + tran.actionSet
									+ " may be not correct compared with pattern " + out);
						}
					}
				}
			}

		}
		System.out.println();
	}

	public void getExecutionPattern() {
		Iterator it = this.actionMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<HashSet<String>, Integer> pair = (Entry<HashSet<String>, Integer>) it.next();
			if (pair.getValue() >= Statechart.supportFactor) {
				System.out.println(pair.getKey() + " can be treated as implicit execution pattern with support value "
						+ pair.getValue());
			}

			// it.remove(); // avoids a ConcurrentModificationException
		}
		System.out.println();
	}

	private Transition getFullTranistion(String id) {
		// TODO Auto-generated method stub
		if (this.transitions.size() > 0) {
			for (Transition trans : this.transitions) {
				if (trans.id.equals(id)) {
					State fromS = this.getFullState(trans.from_state);
					State toS = this.getFullState(trans.to_state);
					trans.from_state_name = fromS.name;
					trans.to_state_name = toS.name;
					trans.domain_name = fromS.domain_name;
					return trans;
				}
			}
		}
		return null;
	}

	private State getFullState(String id) {
		if (this.states.size() > 0) {
			for (State state : this.states) {
				if (state.id.equals(id)) {
					return state;
				}
			}
		}
		return null;
	}

	public boolean isVariable(String name) {
		if (this.variables.size() > 0) {
			for (Variable var : this.variables) {
				if (var.name.equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	
	public void generateFaultTree(String path, String nodeName) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		if(nodeName==null)
		{
			if(this.regions.size()>0)
			{
				for(Region temp:this.regions)
				{
					writer.write("<region name=\""+temp.name+"\">");
					if(temp.states.size()>0)
					{
						
						for(State state:temp.states)
						{
							if(!state.hasOutGoingStateExceptGivenState(temp.entryState, this))
							{
								this.generateNodeTrace(state, temp.entryState);
								writer.write("<node name=\""+state.name+"\">");
								writer.newLine();
								printFaultTree(writer, state);
								writer.write("</node>");
								writer.newLine();
							}
						}
					}
					writer.write("</region>");
				}
			}
		}
	}

	public void printFaultTree(BufferedWriter writer, State state) throws IOException {
		LinkedList<SubNode> tempS=new LinkedList<>();
		if(state.nodes.size()>0)
		{
			for(SubNode node:state.nodes)
			{
				tempS.add(node);
				
				writer.write("<node name=\""+state.name+"\">");
				writer.newLine();
				writer.write("<state name=\""+node.state.name+"\" id=\"state.id\" tid=\""+node.transition.id+"\">"+state.name+"</state>");
				writer.newLine();
				if(node.events.size()>0)
				{
					for(String event:node.events)
					{
						writer.write("<event name=\""+event+"\">"+event+"</>");
						writer.newLine();
					}
					
				}
				printFaultTree(writer, node.state);
				writer.write("</node>");
				writer.newLine();
				
			}
		}
	}
	
	
	public Transition getTransition(String id)
	{
		Transition result=null;
		if(this.transitions.size()>0)
		{
			for(Transition t:this.transitions)
			{
				if(t.id.equals(id))
				{
					result=t;
					break;
				}
			}
		}
		return result;
	}
	
	public State getState(String id)
	{
		State result=null;
		if(this.states.size()>0)
		{
			for(State t:this.states)
			{
				if(t.id.equals(id))
				{
					result=t;
					break;
				}
			}
		}
		return result;
	}
	
	public void generateConfigurableStatechart(String path, String file, String name) {
		BufferedReader br = null;
		FileReader fr = null;

		BufferedWriter bw = null;
		FileWriter fw = null;
		try {

			// br = new BufferedReader(new FileReader(FILENAME));
			String newfile = path + "new_statechart.sct";
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			fw = new FileWriter(newfile);
			bw = new BufferedWriter(fw);

			String newName = "";
			if (name.contains(".")) {
				String[] names = name.split("\\.");
				newName = names[0] + ".get_" + names[1] + "()";
			} else {
				newName = "get_" + name + "()";
			}
			String line;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if (line.contains("<sgraph:Statechart")) {
					String[] alls = line.split("&#xA;");
					for (String item : alls) {
						// if()
					}
					System.out.println("tetst!");
				}
				// bw.write(sCurrentLine.replaceAll(name, newName));
			}
			System.out.println("New Statechart has been generated!");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (Exception ex) {

				ex.printStackTrace();

			}

		}

	}

}
