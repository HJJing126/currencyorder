package conditional.currencyorder;


import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import conditional.basic.*;
public class FindCC {
	
	
	HashMap<String, HashMap<Timestamp,ArrayList<Edge>>> sumGraph;
	HashMap<String, HashMap<String,ArrayList<NodeTime>>> atrList_Map;// <atr,<sourcenode,currencyorder>>
	HashMap<Condition,HashMap<String,HashMap<String,ArrayList<NodeTime>>>> con_atrList_Map;
	//       con             atr              source     order
	HashMap<String,HashMap<String,Order>> currency_Order ;//<atr,<value,order>>
	HashMap<Condition,HashMap<String,HashMap<String,Order>>> con_currency_Order;
	//<con,<atr,<value,order>
	HashMap<String,HashSet<String>>  atr_Dom;//atr valueset
	HashSet<String> co_atr;
	
	public FindCC() {
		sumGraph =new HashMap<String, HashMap<Timestamp,ArrayList<Edge>>>();
		atrList_Map=new HashMap<String, HashMap<String,ArrayList<NodeTime>>>();
		con_atrList_Map=new HashMap<Condition,HashMap<String,HashMap<String,ArrayList<NodeTime>>>>();
		currency_Order=new HashMap<String,HashMap<String,Order>>();
		con_currency_Order= new HashMap<Condition,HashMap<String,HashMap<String,Order>>>(); 
		atr_Dom = new HashMap<String,HashSet<String>> ();
		co_atr = new HashSet<>();
		
		
	}
	public void loadFile(String filepath) throws IOException {
		FileInputStream filein = new FileInputStream(filepath);
		BufferedReader buffers = new BufferedReader(new InputStreamReader(filein));
		String line = null;
		
		while((line = buffers.readLine()) != null) {
			String subject = "";
			String predicate = "";
			String object = "";
			String date = null;	//YYYY-MM-DD
			String type = "null";	//待定
			Timestamp ts = new Timestamp(0,0,0);
			
			String[] msg = line.split("\t");
			subject = msg[0];
			predicate = msg[1];
			object = msg[2];
			
			// add to atr_dom
			HashSet<String> dom = new HashSet<>();
			if(atr_Dom.containsKey(predicate)) {
				dom = atr_Dom.get(predicate);
				
			}
			dom.add(object);
			atr_Dom.put(predicate,dom);
			
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
				if(date!=null) {	//如果输入数据有时间信息
					Node n = new Node(type, object);
					Edge e = new Edge(predicate, n);
					ArrayList<Edge> e_list = new ArrayList<Edge>();
					if(time_edge.containsKey(ts)) {	//当前时间戳已存
						e_list = time_edge.get(ts);
					}
					if(!e_list.contains(e))
						e_list.add(e);
					time_edge.remove(ts);
					time_edge.put(ts, e_list);
				}
			}
			else {
				HashMap<Timestamp,ArrayList<Edge>> time_edge = new HashMap<Timestamp,ArrayList<Edge>>();
				if(!date.equals("")) {	//有时间信息
					ArrayList<Edge> e_list = new ArrayList<Edge>();
					Node n = new Node(type, object);
					Edge e = new Edge(predicate, n);
					e_list.add(e);
					time_edge.remove(ts);
					time_edge.put(ts, e_list);
					sumGraph.put(subject, time_edge);
				}
				else {	//没有时间信息
				}
			}
		}
	}
	
	public void checkData() {
		Iterator iter = sumGraph.entrySet().iterator();
		int count = 0;
		while (iter.hasNext() && count<10) {	//只检查前10个
			Entry entry = (Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			HashMap<Timestamp,ArrayList<Edge>> time_edge = sumGraph.get(key);
			Iterator iter_TE = time_edge.entrySet().iterator();
			while(iter_TE.hasNext()) {
				Entry entry_TE = (Entry) iter_TE.next();
				Object key_TE = entry_TE.getKey();
				Object val_TE = entry_TE.getValue();
				Timestamp t = (Timestamp) key_TE;
				ArrayList<Edge> edge = time_edge.get(key_TE);
				for(Edge e: edge) {
					System.out.println(key+"\t"+e.predicate+"\t"+e.destnode.value+"\t"+t.year+"-"+t.month+"-"+t.day);
				}
			}
			count++;
		}
	}

	public void all_find_cc() {
		
		for(String sourcenode:sumGraph.keySet() ) {
			//System.out.println("operating:  "+sourcenode);
			HashMap<Timestamp,ArrayList<Edge>> timeEdge = sumGraph.get(sourcenode);
			// generate the attr list
			for(Timestamp t: timeEdge.keySet()) {
				//for every time
				//System.out.println("           operating:  "+t.year+"-"+t.month+"-"+t.day);
				ArrayList<Edge> each_atr= timeEdge.get(t);
				for(int i=0;i< each_atr.size();i++) {
					Edge e = each_atr.get(i);
					if(!atrList_Map.containsKey(e.predicate)) {
						
						atrList_Map.put(e.predicate, new HashMap<String,ArrayList<NodeTime>>());
					}
					
					HashMap<String,ArrayList<NodeTime>> atr_time_map = atrList_Map.get(e.predicate); 
					//get the array of sourcenode
					if(!atr_time_map.containsKey(sourcenode)) {
						atr_time_map.put(sourcenode,new ArrayList<NodeTime>());
					}
					ArrayList<NodeTime> atr_time = atr_time_map.get(sourcenode);
					
					atr_time = Add2ArrayNT(t,e.destnode,atr_time);
					atrList_Map.get(e.predicate).remove(sourcenode);
					atrList_Map.get(e.predicate).put(sourcenode,atr_time);
					
				}
				
				
				
					
			}
			
			System.out.println("operating:  "+sourcenode + "end ");
		}
		
	}
	/*
	 * find conditional currency order
	 * 
	 */
	public void Con_findcc( ) {
		for(String atr: atr_Dom.keySet()) {
			//if(!co_atr.contains(atr)) {
				HashSet<String> dom = atr_Dom.get(atr);
				for(String dom_str: dom) {
					Node n = new Node("", dom_str);
					Condition con = new Condition(atr,n);
					if( !con_atrList_Map.containsKey(con)) {
						con_atrList_Map.put(con, new HashMap<String,HashMap<String,ArrayList<NodeTime>>>()); 
					}
					HashMap<String,HashMap<String,ArrayList<NodeTime>>> snt = con_atrList_Map.get(con);
					
					snt = Con_findcc_core(con,snt);
					con_atrList_Map.put(con,snt);
				}
			//}
			
		}
				
				
				
				
			
	}
	

	public HashMap<String,HashMap<String,ArrayList<NodeTime>>> Con_findcc_core(Condition con, HashMap<String,HashMap<String,ArrayList<NodeTime>>> snt) {
		// TODO Auto-generated method stub
		for(String sourcenode:sumGraph.keySet() ) {
			//System.out.println("operating:  "+sourcenode);
			HashMap<Timestamp,ArrayList<Edge>> timeEdge = sumGraph.get(sourcenode);
			// generate the attr list
			for(Timestamp t: timeEdge.keySet()) {
				//for every time
				//System.out.println("           operating:  "+t.year+"-"+t.month+"-"+t.day);
				ArrayList<Edge> each_atr= timeEdge.get(t);
				if(each_atr.contains((Edge)con)) {
					for(int i=0;i< each_atr.size();i++) {
						Edge e = each_atr.get(i);
						//if this e is not the con and  this atr do not have all currency order
						if(!e.equals((Edge)con) && !co_atr.contains(e.predicate)) {
							if(!snt.containsKey(e.predicate)) {
								
								snt.put(e.predicate, new HashMap<String,ArrayList<NodeTime>>());
							}
							
							HashMap<String,ArrayList<NodeTime>> atr_time_map = snt.get(e.predicate); 
							//get the array of sourcenode
							if(!atr_time_map.containsKey(sourcenode)) {
								atr_time_map.put(sourcenode,new ArrayList<NodeTime>());
							}
							ArrayList<NodeTime> atr_time = atr_time_map.get(sourcenode);
							
							atr_time = Add2ArrayNT(t,e.destnode,atr_time);
							snt.get(e.predicate).remove(sourcenode);
							snt.get(e.predicate).put(sourcenode,atr_time);
						}
						
						
					}
				}
				
				
				
				
					
			}
			
			
		}
		
		return snt;
	}
	
	
	public void all_find_cc_sum(){
			
		//real
		//find the relative position 
		for(String atr:atrList_Map.keySet()) {
			//for each atr
			if(!currency_Order.containsKey(atr)) {
				
				currency_Order.put(atr, new HashMap<String,Order>());
			}
			//for each source
			for(String s: atrList_Map.get(atr).keySet()) {
				ArrayList<NodeTime> nt_array = atrList_Map.get(atr).get(s);
				for(int i=0;i<nt_array.size();i++) {
					//if no this value
					if(!currency_Order.get(atr).containsKey(nt_array.get(i).value)) {
						Order order = new Order();
						order.SetValue(atr);
						currency_Order.get(atr).put(nt_array.get(i).value,order);
					}
					Order order = currency_Order.get(atr).get(nt_array.get(i).value);
					if(order == null) {
						order = new Order();
						order.SetValue(atr);
					}
						
					order = generate_Order(order, i ,nt_array);
					currency_Order.get(atr).put(nt_array.get(i).value, order);
				}
				
			}
			//finish one atr, summarize
			for(String s: currency_Order.get(atr).keySet()) {
				if(currency_Order.get(atr).get(s)!=null) {
					co_atr.add(atr);
					currency_Order.get(atr).get(s).SumOrder();
					System.out.println("VLAUE:   "+s);
					currency_Order.get(atr).get(s).print();
				}
				   
				  
				   
			}
			
		}
			
			
		
		
		
	}
	
	public void con_SummarizeCurrencyOrder() {
		
		//HashMap<Str,HashMap<String,HashMap<String,Order>>> con_currency_Order
		for(Condition con: con_atrList_Map.keySet()) {
			System.out.println("condition is -----"+con.predicate+" = "+con.destnode.value);
			if(!con_currency_Order.containsKey(con)) {
				HashMap<String,HashMap<String,Order>> sso =new HashMap<String,HashMap<String,Order>>();
				con_currency_Order.put(con,sso);
			}
			HashMap<String, HashMap<String, ArrayList<NodeTime>>> part_con_atrList_Map = con_atrList_Map.get(con);
			HashMap<String,HashMap<String,Order>> part_con_currency_Order = con_currency_Order.get(con);
			 part_con_currency_Order = con_SummarizeCurrencyOrder_core(part_con_atrList_Map,part_con_currency_Order);
			con_currency_Order.put(con,part_con_currency_Order);
			
		}
	}
	
	
	
	public  HashMap<String, HashMap<String, Order>> con_SummarizeCurrencyOrder_core(
			HashMap<String, HashMap<String, ArrayList<NodeTime>>> part_con_atrList_Map,
			HashMap<String, HashMap<String, Order>>part_con_currency_Order) {
		// TODO Auto-generated method stub
		
		for(String atr:part_con_atrList_Map.keySet()) {
			//for each atr
			if(!part_con_currency_Order.containsKey(atr)) {
				
				part_con_currency_Order.put(atr, new HashMap<String,Order>());
			}
			//for each source
			for(String s: part_con_atrList_Map.get(atr).keySet()) {
				ArrayList<NodeTime> nt_array = part_con_atrList_Map.get(atr).get(s);
				for(int i=0;i<nt_array.size();i++) {
					//if no this value
					if(!part_con_currency_Order.get(atr).containsKey(nt_array.get(i).value)) {
						Order order = new Order();
						order.SetValue(atr);
						part_con_currency_Order.get(atr).put(nt_array.get(i).value,order);
					}
					Order order = part_con_currency_Order.get(atr).get(nt_array.get(i).value);
					if(order == null) {
						order = new Order();
						order.SetValue(atr);
					}
						
					order = generate_Order(order, i ,nt_array);
					part_con_currency_Order.get(atr).put(nt_array.get(i).value, order);
				}
				
			}
			//finish one atr, summarize
			for(String s: part_con_currency_Order.get(atr).keySet()) {
				if(part_con_currency_Order.get(atr).get(s)!=null) {
					part_con_currency_Order.get(atr).get(s).SumOrder();
					//System.out.println("VLAUE:   "+s);
					//part_con_currency_Order.get(atr).get(s).print();
				}
				   
				  
				   
			}
			
		}
   
		return part_con_currency_Order;
		
		
		
		
	}
	public ArrayList<NodeTime> Edge2NT( ArrayList<Edge> array_e){
		// problem: if now the orderdisobey the order
		ArrayList<NodeTime> array_NT = new ArrayList<NodeTime>();
	
		return array_NT;
		
	}
	
	
	// add node n at proper position  according to the timestamp
	public ArrayList<NodeTime> Add2ArrayNT(Timestamp t,Node n, ArrayList<NodeTime> array_NT){
		
        
		NodeTime nt = new NodeTime(n.type, n.value, t);
		boolean end=false;
	
		for(int i=0; i<array_NT.size(); i++) { 
			 if(nt.compare(array_NT.get(i))) {
				   continue;
			   }
			 else {
			       array_NT.add(i, nt);
			       end = true;
			       break;
			  }
		 }
		  
		if(end == false) {
			array_NT.add(nt);
		}
		return array_NT;
	}
	
	public Order generate_Order(Order order, int index ,ArrayList<NodeTime> nt_array) {
		//Order order = new Order();
		
		if(nt_array.size()<2)
			return null;
		int j=0;
		while(j<index) {
			order.AddOld(nt_array.get(j).value);
			j++;
		}
		j++;//index
		while(j<nt_array.size()) {
			//System.out.println("======generate_Order======="+ nt_array.get(j).value);
			
			order.AddCur(nt_array.get(j).value);
			j++;
		}
		return order;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		FindCC findCC = new FindCC();
		//String filename = args[0];
		String filename = "D:\\eclipse\\currencyorder\\Transactions_RDF";
		System.out.println("======load file======="+filename);
		findCC.loadFile(filename);
		System.out.println("======finish load file=======");
//		System.out.println("======check data=======");
//		findCC.checkData();
//		
		findCC.all_find_cc();
		System.out.println("======finish all_find_cc=======");
		findCC.all_find_cc_sum();
		System.out.println("======all_find_cc_sum=======");
		
		findCC.Con_findcc();
		System.out.println("======finish con_find_cc=======");
		findCC.con_SummarizeCurrencyOrder();
		System.out.println("======con_SummarizeCurrencyOrder=======");
		
		//System.out.println("hello");
	}
	

}
