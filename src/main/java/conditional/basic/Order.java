package conditional.basic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Order {
	public String value;
	public HashMap<String,Integer> old;
	public HashMap<String,Integer> cur;
	public HashSet<String> old_set;
	public HashSet<String> cur_set;
	
	
	public Order() {
		 value = "";
		 old= new HashMap<String,Integer>();
		 cur= new HashMap<String,Integer>();
		 old_set=new HashSet<String>();
		 cur_set=new HashSet<String>();
	}
	public void AddOld(String oldvalue) {
		if(!old.containsKey(oldvalue)) {
			old.put(oldvalue,0);
		}
        int count = old.get(oldvalue);
        //System.out.println("get old"+ count);
        old.remove(oldvalue);
        count=count+1;
		old.put(oldvalue,count);	
		//System.out.println("add old"+ old.get(oldvalue));
	}
	public void AddCur(String curvalue) {
		if(!cur.containsKey(curvalue)) {
			cur.put(curvalue, 0);
		}
        int count = cur.get(curvalue);
       // System.out.println("add cur"+ count);
        cur.remove(curvalue);
        count=count+1;
		cur.put(curvalue,count);	
		//System.out.println("add cur"+ this.cur.get(curvalue));
	}
	public void SetValue(String value) {
		this.value = value;
	}
	public void SumOrder() {
		for(String s : old.keySet()) {
			int old_count = old.get(s);
			//System.out.println("old"+old_count);
			int cur_count = 0;
			if(cur.containsKey(s)) {
				cur_count = cur.get(s);
			}
			//System.out.println("cur"+cur_count);
		    if(old_count== 0 && cur_count!=0)
		    	cur_set.add(s);
		    else if(old_count!=0 && cur_count==0)
		    	old_set.add(s);
		    
		    else if(old_count!= 0 && cur_count!=0 && old_count/(cur_count+old_count) > 0.99999)
		    	old_set.add(s);
		    else
		    	cur_set.add(s);
		    	
		    
		}
		for(String s : cur.keySet()) {
			int  old_count = 0;
			int cur_count = cur.get(s);
			if(old.containsKey(s)) {
				old_count = cur.get(s);
			}
		    if(cur_count==0 && old_count!=0)
		    	old_set.add(s);
		    else if(cur_count!=0 && old_count==0)
		    	cur_set.add(s);
		    else if(old_count!= 0 && cur_count!=0 && cur_count/(cur_count+old_count) > 0.99999)
		    	cur_set.add(s);
		    else
		    	old_set.add(s);
		    	
		    
		}
	}
	public String print() {
		String print_str = "ATR : "+ value + '\n'+
				           "old_set:"+ old_set.toString()+ '\n'+
				           "cur_set:"+ cur_set.toString()+ '\n';
		System.out.println(print_str);

		return print_str;

	}
	
//	public void writeFile() throws IOException {
//		//写文件
//		File file =new File("all_cc");
//		
//		if(!file.exists()) {
//			file.createNewFile();
//		}
//		FileWriter fileWritter = new FileWriter(file.getName(), true);
//		//end
//		
//		String print_str = "ATR : "+ value + '\n'+
//		           "old_set:"+ old_set.toString()+ '\n'+
//		           "cur_set:"+ cur_set.toString()+ '\n';
//		fileWritter.write(print_str + "\n");
//	}

}
