package conditional.currencyorder;


import java.io.IOException;
import java.util.*;

import conditional.basic.*;
public class FindCC {
	
	
	HashMap<String, HashMap<Timestamp,ArrayList<Edge>>> sumGraph;
	HashMap<String, HashMap<String,ArrayList<NodeTime>>> atrList_Map;// <atr,<sourcenode,currencyorder>>
	HashMap<String,HashMap<Condition,ArrayList<NodeTime>>> con_atrList;
	HashMap<String,HashMap<String,Order>> currency_Order ;//<atr,<value,order>>
	
	
	public FindCC() {
		sumGraph =null;
		atrList_Map=null;
		con_atrList=null;
		currency_Order=null;
	}
	public void loadFile(String filepath) throws IOException {
		FileInputStream filein = new FileInputStream(filepath);
		BufferedReader buffers = new BufferedReader(new InputStreamReader(filein));
		String line = null;
		
		while((line = buffers.readLine()) != null) {
			String subject = "";
			String predicate = "";
			String object = "";
			String date = "";	//YYYY-MM-DD
			int type = 0;	//待定
			Timestamp ts = new Timestamp(0,0,0);
			
			String[] msg = line.split("\t");
			subject = msg[0];
			predicate = msg[1];
			object = msg[2];
			if(msg.length>3) {
				date = msg[3];
				String[] t = date.split("-");
				int year = Integer.parseInt(t[0]);
				int month = Integer.parseInt(t[1]);
				int day = Integer.parseInt(t[2]);
				ts = new Timestamp(year, month, day);
			}
			
			if(sumGraph.containsKey(subject)) {
				HashMap<Timestamp,ArrayList<Edge>> time_edge = sumGraph.get(subject);
				if(!date.equals("") && time_edge.containsKey(date)) {	//如果有时间信息
					Node n = new Node(type, object);
					Edge e = new Edge(predicate, n);
					ArrayList<Edge> e_list = time_edge.get(date);
					if(!e_list.contains(e))
						e_list.add(e);
					time_edge.put(ts, e_list);
				}
				else {	//如果没有时间信息
				}
			}
			else {
				HashMap<Timestamp,ArrayList<Edge>> time_edge = new HashMap<Timestamp,ArrayList<Edge>>();
				if(!date.equals("")) {	//有时间信息
					ArrayList<Edge> e_list = new ArrayList<Edge>();
					Node n = new Node(type, object);
					Edge e = new Edge(predicate, n);
					e_list.add(e);
					time_edge.put(ts, e_list);
					sumGraph.put(subject, time_edge);
				}
				else {	//没有时间信息
				}
			}
		}
	}
	
	public void Generate_currency_list() {
		int one_source;
		for(String sourcenode:sumGraph.keySet() ) {
			one_source=0;
			HashMap<Timestamp,ArrayList<Edge>> timeEdge = sumGraph.get(sourcenode);
			// generate the attr list
			for(Timestamp t: timeEdge.keySet()) {
				ArrayList<Edge> each_atr= timeEdge.get(t);
				for(int i=0;i< each_atr.size();i++) {
					Edge e = each_atr.get(i);
					if(!atrList_Map.containsKey(e.predicate)) {
						
						atrList_Map.put(e.predicate, new HashMap<String,ArrayList<NodeTime>>());
					}
					
					HashMap<String,ArrayList<NodeTime>> atr_time_map = atrList_Map.get(e.predicate); 
					//get the array of sourcenode
					if(! atr_time_map.containsKey(sourcenode)) {
						atr_time_map.put(sourcenode,new ArrayList<NodeTime>());
					}
					ArrayList<NodeTime> atr_time = atr_time_map.get(sourcenode);
					
					ArrayList<NodeTime> new_atr_time = Add2ArrayNT(t,e.destnode,atr_time);
					atrList_Map.get(e.predicate).remove(sourcenode);
					atrList_Map.get(e.predicate).put(sourcenode,new_atr_time);
					
				}
				
				
				
					
			}
			
		
		}
		
	}
	/*
	 * find conditional currency order
	 * 
	 */
	public void Con_findcc(Condition c) {
		for(String sourcenode:sumGraph.keySet() ) {
			HashMap<Timestamp,ArrayList<Edge>> timeEdge = sumGraph.get(sourcenode);
			// generate the attr list
			for(Timestamp t: timeEdge.keySet()) {
				ArrayList<Edge> each_atr= timeEdge.get(t);
				if(each_atr.contains(c)) {
					for(int i=0;i< each_atr.size();i++) {
						Edge e = each_atr.get(i);
						if(e!=c) {
							if(!con_atrList.containsKey(e.predicate)) {
								con_atrList.put(e.predicate, new HashMap<Condition,ArrayList<NodeTime>>());
							}
							else if(!con_atrList.get(e.predicate).containsKey(c)) {
								con_atrList.get(e.predicate).put(c,new ArrayList<NodeTime>());
							}
							 ArrayList<NodeTime>con_atr_time = con_atrList.get(e.predicate).get(c);
							
							ArrayList<NodeTime> new_con_atr_time = Add2ArrayNT(t,e.destnode,con_atr_time);
                            con_atrList.get(e.predicate).remove(c);
							con_atrList.get(e.predicate).put(c,new_con_atr_time);
							
						}
						
					}
				}
				
				
				
				
					
			}
			
		
		}
	}
	public ArrayList<NodeTime> Edge2NT( ArrayList<Edge> array_e){
		// problem: if now the orderdisobey the order
		ArrayList<NodeTime> array_NT = new ArrayList<NodeTime>();
	
		return array_NT;
		
	}
	
	
	// add node n at proper position  according to the timestamp
	public ArrayList<NodeTime> Add2ArrayNT(Timestamp t,Node n,ArrayList<NodeTime> array_NT){
		// problem: if now the orderdisobey the order
        
		NodeTime nt = new NodeTime(n.type, n.value, t);
		boolean end=false;
		int i=0;
		for(i=0; i<array_NT.size(); i++) { 
			 if(nt.compare(array_NT.get(i))) {
				   continue;
			   }
			   else {
			       array_NT.add(i, nt);
			       end = true;
			  }
		 }
		  
		if(end == false) {
			array_NT.add(i, nt);
		}
		return array_NT;
	}
	public void SummarizeCurrencyOrder(){
		//just for return
		
		
		//real
		//find the relative position 
		for(String atr:atrList_Map.keySet()) {
			if(!currency_Order.containsKey(atr)) {
				currency_Order.put(atr, new HashMap<String,Order>());
			}
			
			//for each source
			for(String s: atrList_Map.get(atr).keySet()) {
				ArrayList<NodeTime> nt_array = atrList_Map.get(atr).get(s);
				for(int i=0;i<nt_array.size();i++) {
					//if no this value
					if(!currency_Order.get(atr).containsKey(nt_array.get(i))) {
						Order order = new Order();
						order.SetValue(atr);
						currency_Order.get(atr).put(nt_array.get(i).value,order);
					}
					Order order = currency_Order.get(atr).get(nt_array.get(i));
					order = generate_Order(order, i ,nt_array);
					currency_Order.get(atr).put(nt_array.get(i).value, order);
				}
				
			}
			//finish one atr, summarize
			for(String s: currency_Order.get(atr).keySet()) {
				currency_Order.get(atr).get(s).SumOrder();
				currency_Order.get(atr).get(s).print();
			}
			
		}
			
			
		
		
		
	}
	public Order generate_Order(Order order, int index ,ArrayList<NodeTime> nt_array) {
		//Order order = new Order();
		int j=0;
		while(j<index) {
			order.AddOld(nt_array.get(j).value);
		}
		j++;
		while(j<nt_array.size()) {
			order.AddCur(nt_array.get(j).value);
		}
		return order;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		FindCC findCC = new FindCC();
		String filename = "";
		findCC.LoadFile(filename);
		findCC.Generate_currency_list();
		findCC.SummarizeCurrencyOrder();
		
		System.out.println("hello");
	}
	

}
