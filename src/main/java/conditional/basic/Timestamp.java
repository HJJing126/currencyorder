package conditional.basic;

public class Timestamp {
	public Integer year;
	public Integer month;
	public Integer day;
	
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
    public int hashCode() {

        int result = 17;
        result = 31 * result + year.hashCode();
        result = 31 * result + month.hashCode();
        result = 31 * result + day.hashCode();

        return result;
    }

    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Timestamp)) {
            return false;
        }

        Timestamp tsp = (Timestamp) obj;
        return year.equals(tsp.year) && month.equals(tsp.month) && day.equals(tsp.day);

    }
	

	



}
