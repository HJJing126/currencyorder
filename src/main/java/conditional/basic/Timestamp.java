package conditional.basic;

public class Timestamp {
	public int year;
	public int month;
	public int day;
	
	public Timestamp(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	
	public boolean compare(Timestamp t2) {
		Timestamp t1 = this;
		if(t1.year > t2.year)
		   return true;
		else if(t1.year< t2.year)
			return false;
		else
			if(t1.month > t2.month)
				   return true;
				else if(t1.month< t2.month)
					return false;
				else
					if(t1.day> t2.day)
						   return true;
						
		return false;
		
			
	}

	



}
