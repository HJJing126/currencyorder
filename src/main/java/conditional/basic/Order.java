package conditional.basic;

import java.util.*;

public class Order {
	public String value;
	public HashMap<String,Integer> old;
	public HashMap<String,Integer> cur;
	public Set<String> old_set;
	public Set<String> cur_set;
	
	
	public Order() {
		 value = null;
		 old=null;
		 cur=null;
		 old_set=null;
		 cur_set=null;
	}
	public void AddOld(String oldvalue) {
		if(!old.containsKey(oldvalue)) {
			old.put(oldvalue, 0);
		}
        int count = old.get(oldvalue);
		this.old.put(oldvalue,count++);	
	}
	public void AddCur(String curvalue) {
		if(!cur.containsKey(curvalue)) {
			old.put(curvalue, 0);
		}
        int count = cur.get(curvalue);
		this.cur.put(curvalue,count++);		
	}
	public void SetValue(String value) {
		this.value = value;
	}
	public void SumOrder() {
		for(String s : old.keySet()) {
			int old_count = old.get(s);
			int cur_count = 0;
			if(cur.containsKey(s)) {
				cur_count = cur.get(s);
			}
		    if(old_count==0 && cur_count!=0)
		    	cur_set.add(s);
		    else if(old_count!=0 && cur_count==0)
		    	old_set.add(s);
		    else if(old_count/(cur_count+old_count) > 0.8)
		    	old_set.add(s);
		    else
		    	cur_set.add(s);
		    	
		    
		}
	}
	public void print() {
		String print_str = "value : "+ value + '\n'+
				           "old_set:"+ old_set.toString()+ '\n'+
				           "cur_set:"+ cur_set.toString()+ '\n';
				           
	}
	

}
