import java.util.LinkedList;

public class Statechart {

	public LinkedList<State> states = new LinkedList<>();
	public LinkedList<Transition> transitions = new LinkedList<>();
	public LinkedList<Event> events = new LinkedList<>();
	public LinkedList<Variable> variables = new LinkedList<>();

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
				if (!var.beUpdated&&var.type==0) 
				{
					if (var.used_states.size()== 0&&var.used_transitions.size()== 0) 
					{
						System.out.println("The variable " + var.name
								+ " can be treated as constant variable. There is no impacted objects in the statechart.");
					}
					else
					{
						System.out.println("The variable " + var.name
								+ " can be treated as constant variable, and the impacted objects are listed as following:");
					}
					if (var.used_states.size() > 0) 
					{

						for (VarRelation item : var.used_states) {
							State state = this.getFullState(item.id);
							if (state != null) {
								if(item.imp_var==null)
								{
									if (item.imp_var.contains("(") && item.imp_var.contains(")")) {
										System.out.println("In the region " + state.domain_name + ", in the state "
												+ state.name + ", operation " + var.name
												+ " is been used in the entry conditional statement");
									} else {
										System.out.println("In the region " + state.domain_name + ", in the state "
												+ state.name + ", variable " + var.name
												+ " is been used in the entry conditional statement");
									}
								}
								else
								{
									if (item.imp_var.contains("(") && item.imp_var.contains(")")) {
										System.out.println("In the region " + state.domain_name + ", in the state "
												+ state.name + ", variable " + item.imp_var
												+ " is been updated with the operation " + var.name);
									}
									else
									{
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
				//System.out.println(var.name);
				if (var.name.equals(name)) 
				{
					if (var.used_states.size() == 0 && var.used_transitions.size() == 0 && var.updated_states.size()==0&&var.updated_transitions.size()==0) {
						System.out.println("The variable " + var.name+" has no impacted objects in the statechart.");
					} else {
						System.out.println("The variable " + var.name
								+ " impacted objects are listed as following:");
					}
					
					if (var.updated_states.size() > 0) {

						for (String item : var.updated_states) {
							State state = this.getFullState(item);
							if (state != null) {
								System.out.println("In the region " + state.domain_name + ", in the state "
										+ state.name + ", variable " + var.name+ " is been updated");

							}
						}
					}
					
					if (var.updated_transitions.size() > 0) {

						for (String item : var.updated_transitions) {
							Transition transition = this.getFullTranistion(item);
							if (transition != null) {
								System.out.println("In the region " + transition.domain_name
										+ ", on the transition from state " + transition.from_state_name
										+ " to state " + transition.to_state_name + ", variable " + var.name
										+ " is been updated");

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

	
}
