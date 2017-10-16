import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SCParser {

	public void readStatechart(String path, Statechart statechart) throws Exception {
		File inputFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		// Statechart statechart = new Statechart();
		System.out.println("Loading Statechart Model");
		this.initVariables_Events(doc, statechart);
		this.initStates(doc, statechart);
		System.out.println("Loading Done!");
		System.out.println();
	}

	public void initStates(Document doc, Statechart statechart) {
		NodeList regions = doc.getElementsByTagName("regions");
		for (int i = 0; i < regions.getLength(); i++) {
			Node nNode = regions.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String domain_name = eElement.getAttribute("name");
				String domain_id = eElement.getAttribute("xmi:id");
				NodeList states = eElement.getElementsByTagName("vertices");
				for (int j = 0; j < states.getLength(); j++) {
					Node nNode_state = states.item(j);
					if (nNode_state.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement_state = (Element) nNode_state;

						String sname = eElement_state.getAttribute("name");
						String sid = eElement_state.getAttribute("xmi:id");
						State state = new State(domain_id, domain_name, sid, sname);

						String specification = eElement_state.getAttribute("specification");
						if (specification != null && !specification.equals("")) {
							specification=this.replaceSymbols(specification);
							specification=specification.replaceAll("\nentry", ";entry");
							specification=specification.replaceAll("\nexit", ";exit");
							specification=specification.replaceAll("\n", "");
							specification=specification.replaceAll("\r", "");
							specification=specification.replaceAll("\\s+", "");
							
							String[] infos = specification.split(";");
							if (infos.length > 0) {
								for (String item : infos) {
									item=item.replaceAll("entry/", "");
									item=item.replaceAll("oncycle/", "");
									item=item.replaceAll("exit/", "");
									if(item.contains("/")&&item.contains("["))
									{
										String used = item.substring(item.indexOf("[") + 1);
										used = used.substring(0, used.indexOf("]"));
										used=used.replaceAll("&&", ";");
										used=used.replaceAll("\\|\\|", ";");
										String[] infos1 = used.split(";");
										if (infos1.length > 0) {
											for (String item1 : infos1) {
												item1 = replaceSymbolsCalculation(item1);
												String[] iteminfo = item1.split("[=<>]");
												state.addUsedVariableInEntryCondition(iteminfo[0].trim());
												Variable.addUsedState(statechart, iteminfo[0].trim(), state.id, null);
												String[] usedVs = iteminfo[1].split("[-+*/^%!]");
												for (String var : usedVs) {
													if (!var.equals("")) {
														if(var.contains("(")&&var.contains(")"))
														{
															String[] test1=var.split("(");
															state.addUsedVariableInEntryCondition(test1[0].trim()+"()");
															String used1 = var
																	.substring(var.indexOf("(") + 1);
															used1 = used1.substring(0, used1.indexOf(")"));
															if(used1!=null&&!used1.equals(""))
															{
																test1=used1.split(",");
																for(String s:test1)
																{
																	if(statechart.isVariable(s.trim()))
																	{
																		state.addUsedVariableInEntryCondition(var.trim());
																		Variable.addUsedState(statechart, var.trim(), state.id, null);
																	}
																}
															}
															
														}
														else
														{
															state.addUsedVariableInEntryCondition(var.trim());
															Variable.addUsedState(statechart, var.trim(), state.id, null);
														}
														
													}
												}
											}
										}
										
										String[] test1=item.split("/");
										item=test1[1];
									}
									
									
									if (item.startsWith("raise")) {
										String[] iteminfo = item.split("\\s+");
										String vname = iteminfo[1];
										state.addRaisedEvent(vname);
									} else {
										if (item.contains("=")) {
											item = replaceSymbolsCalculation(item);
											String[] iteminfo = item.split("=");
											UpdatedVariable upvar = new UpdatedVariable(iteminfo[0].trim());
											String[] usedVs = iteminfo[1].split("[-+*/^%!]");
											for (String var : usedVs) {
												if (!var.equals("")) {
													if(var.contains("(")&&var.contains(")"))
													{
														String[] test1=var.split("(");
														upvar.addUsedVariable(test1[0].trim()+"()");
														String used1 = var
																.substring(var.indexOf("(") + 1);
														used1 = used1.substring(0, used1.indexOf(")"));
														if(used1!=null&&!used1.equals(""))
														{
															test1=used1.split(",");
															for(String s:test1)
															{
																if(statechart.isVariable(s.trim()))
																{
																	upvar.addUsedVariable(var.trim());
																	Variable.addUsedState(statechart, var.trim(), state.id, upvar.name);
																}
															}
														}
														
													}
													else
													{
														upvar.addUsedVariable(var.trim());
														Variable.addUsedState(statechart, var.trim(), state.id, upvar.name);
													}
													
												}
											}
											state.addUpdatedVariable(upvar);
											Variable.addUpdatedState(statechart, upvar.name, state.id);
											Variable.updateBeUpdated(statechart, upvar.name);
										}
									}

								}
							}
						}

						String in_transition_string = eElement_state.getAttribute("incomingTransitions");
						if (in_transition_string != null && !in_transition_string.equals("")) {
							String[] in_transitions = in_transition_string.split(" ");
							for (int k = 0; k < in_transitions.length; k++) {
								state.addIncomingTransition(in_transitions[k]);
							}
						}

						NodeList out_transitions = eElement_state.getElementsByTagName("outgoingTransitions");
						if (out_transitions != null && out_transitions.getLength() > 0) {
							for (int k = 0; k < out_transitions.getLength(); k++) {
								Node nNode_transition = out_transitions.item(k);
								if (nNode_transition.getNodeType() == Node.ELEMENT_NODE) {
									Element eElement_transition = (Element) nNode_transition;
									// String tname=eElement_transition.getAttribute("name");
									String tid = eElement_transition.getAttribute("xmi:id");
									String targetid = eElement_transition.getAttribute("target");
									Transition temp_transition = new Transition(tid, state.id, targetid);
									String specification_trans = eElement_transition.getAttribute("specification");
									if (specification_trans!=null&&!specification_trans.equals("")) {
										specification_trans=this.replaceSymbols(specification_trans);
										String[] tempinfo1 = specification_trans.split("/");
										if (statechart.events.size() > 0) {
											for (Event e : statechart.events) {
												if (tempinfo1[0].contains(e.name)) {
													temp_transition.addUsedEvent(e.name);
												}
											}
										}
										if (tempinfo1.length > 1) {
											String[] infos = tempinfo1[1].split("\n");
											if (infos.length > 0) {
												for (String item : infos) {
													if (item.startsWith("raise")) {
														String[] iteminfo = item.split("\\s+");
														String vname = iteminfo[1];
														temp_transition.addRaisedEvent(vname.trim());
													} else {
														if (item.contains("=")) {
															item = replaceSymbolsCalculation(item);
															String[] iteminfo = item.split("=");
															
															UpdatedVariable tupvar = new UpdatedVariable(iteminfo[0].trim());
															String[] usedVs = iteminfo[1].split("[-+*/^%!]");
															for (String var : usedVs) {
																if (!var.equals("")) {
																	if(var.contains("(")&&var.contains(")"))
																	{
																		String[] test1=var.split("(");
																		tupvar.addUsedVariable(test1[0].trim()+"()");
																		String used1 = var
																				.substring(var.indexOf("(") + 1);
																		used1 = used1.substring(0, used1.indexOf(")"));
																		if(used1!=null&&!used1.equals(""))
																		{
																			test1=used1.split(",");
																			for(String s:test1)
																			{
																				if(statechart.isVariable(s.trim()))
																				{
																					tupvar.addUsedVariable(var.trim());
																					Variable.addUsedTransition(statechart, var.trim(),temp_transition.id, tupvar.name);
																				}
																			}
																		}
																		
																	}
																	else
																	{
																		tupvar.addUsedVariable(var.trim());
																		Variable.addUsedTransition(statechart, var.trim(),temp_transition.id, tupvar.name);
																	}
																	
																}
															}
															temp_transition.addUpdatedVariable(tupvar);
															Variable.addUpdatedTransition(statechart, tupvar.name, temp_transition.id);
															Variable.updateBeUpdated(statechart, tupvar.name);
														}
													}

												}
											}
										}

										if (specification_trans.contains("[") && specification_trans.contains("]")) {
											String used = specification_trans
													.substring(specification_trans.indexOf("[") + 1);
											used = used.substring(0, used.indexOf("]"));
											used=used.replaceAll("&&", ";");
											used=used.replaceAll("\\|\\|", ";");
											String[] infos = used.split(";");
											if (infos.length > 0) {
												for (String item : infos) {
													item = replaceSymbolsCalculation(item);
													String[] iteminfo = item.split("[=<>]");
													temp_transition.addUsedVariable(iteminfo[0].trim());
													Variable.addUsedTransition(statechart, iteminfo[0].trim(), temp_transition.id, null);
													String[] usedVs = iteminfo[1].split("[-+*/^%]");
													for (String var : usedVs) {
														if (!var.equals("")) {
															if(var.contains("(")&&var.contains(")"))
															{
																String[] test1=var.split("(");
																temp_transition.addUsedVariable(test1[0].trim()+"()");
																String used1 = var
																		.substring(var.indexOf("(") + 1);
																used1 = used1.substring(0, used1.indexOf(")"));
																if(used1!=null&&!used1.equals(""))
																{
																	test1=used1.split(",");
																	for(String s:test1)
																	{
																		if(statechart.isVariable(s.trim()))
																		{
																			 temp_transition.addUsedVariable(var.trim());
																			 Variable.addUsedTransition(statechart, var.trim(), temp_transition.id, null);
																		}
																	}
																}
																
															}
															else
															{
															    temp_transition.addUsedVariable(var.trim());
															    Variable.addUsedTransition(statechart, var.trim(), temp_transition.id, null);
															}
														}
													}
												}
											}
										}

									}
									statechart.addTransition(temp_transition);
									state.addOutgoingTransition(temp_transition.id);
								}

							}
						}
						statechart.addState(state);

					}
				}
			}
		}
	}

	public String replaceSymbolsCalculation(String item) {
		item=item.replaceAll("\\+=", "=");
		item=item.replaceAll("-=", "=");
		item=item.replaceAll("/=", "=");
		item=item.replaceAll("\\*=", "=");
		item=item.replaceAll("\n", "");
		item=item.replaceAll("==", "=");
		item=item.replaceAll("<=", "<");
		item=item.replaceAll(">=", ">");
		item=item.replaceAll("&&", "/");
		item=item.replaceAll("!", "");
		item=item.replaceAll("\\|\\|", "/");
		return item;
	}

	public String replaceSymbols(String specification_trans) {
		specification_trans=specification_trans.replaceAll("!", "");
		//specification_trans=specification_trans.replaceAll("entry/", "");
		return specification_trans;
	}
	public void initVariables_Events(Document doc, Statechart statechart) {
		NodeList variables = doc.getElementsByTagName("sgraph:Statechart");
		for (int i = 0; i < variables.getLength(); i++) {
			Node nNode = variables.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String specification = eElement.getAttribute("specification");
				String domain_id = eElement.getAttribute("xmi:id");
				specification=this.replaceSymbols(specification);
				String[] infos = specification.split("\n");
				if (infos.length > 0) {
					String prefix = "";
					String domain_name = "";
					for (String item : infos) {
						if (item.startsWith("internal")) {
							domain_name = "internal";
							prefix = "";

						} else if (item.startsWith("interface")) {
							String[] iteminfo1 = item.split("\\s+");
							iteminfo1 = iteminfo1[1].split(":");
							prefix = iteminfo1[0];
							domain_name = prefix;
							prefix = prefix + ".";
						}

						if (item.startsWith("const")) {
							String[] iteminfo =item.split("\\s+");
							iteminfo = iteminfo[1].split(":");
							String vname = prefix + iteminfo[0];
							Variable var = new Variable(domain_id.trim(), domain_name.trim(), vname.trim(), Variable.constant);
							statechart.addVariable(var);
						} else if (item.startsWith("var")) {
							String[] iteminfo = item.split("\\s+");
							iteminfo = iteminfo[1].split(":");
							String vname = prefix + iteminfo[0];
							Variable var = new Variable(domain_id.trim(), domain_name.trim(), vname.trim(), Variable.local);
							statechart.addVariable(var);
						} else if (item.startsWith("event")||item.startsWith("in event")||item.startsWith("out event")) {
							item=item.replaceAll("in event", "event");
							item=item.replaceAll("out event", "event");
							String[] iteminfo =item.split("\\s+");
							iteminfo = iteminfo[1].split(":");
							String vname = prefix + iteminfo[0];
							Event var = new Event(domain_id.trim(), domain_name.trim(), vname.trim());
							statechart.addEvent(var);
						}
						else if (item.startsWith("operation")) {
							String[] iteminfo =item.split("\\s+");
							String[]iteminfo1 = iteminfo[1].split("\\(");
							String vname = prefix + iteminfo1[0]+"()";
							
							iteminfo1=iteminfo[1].split("\\)");
							String fullName = prefix + iteminfo1[0]+")";
							Variable var = new Variable(domain_id.trim(), domain_name.trim(), vname.trim(), Variable.local);
							var.type=1;
							var.full_name=fullName.trim();
							statechart.addVariable(var);
						}

					}
				}

			}
		}
	}
}
