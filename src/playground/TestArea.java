package playground;

public class TestArea {

	public static void main(String[] args) {
		short shorty;
		int inty = 35000;
		
		shorty = (inty > Short.MAX_VALUE) ? Short.MAX_VALUE : ((inty < Short.MIN_VALUE) ? Short.MIN_VALUE : (short) inty);
		
		System.out.println("Short.MAX_VALUE: " + Short.MAX_VALUE);
		System.out.println("Short.MIN_VALUE: " + Short.MIN_VALUE);
		System.out.println("           Inty: " + inty);
		System.out.println("         Shorty: " + shorty);
	}
	
}
