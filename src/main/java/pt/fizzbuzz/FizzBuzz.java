package pt.fizzbuzz;

public class FizzBuzz {

	
	public boolean isMultipleOf(int numberToCheck, int multipleOf) {
		return numberToCheck%multipleOf == 0;
	}
	
	public String sendNumber(int numberToResolve) {
		
		if(isMultipleOf(numberToResolve, 3)) { 
			return "Fizz";
		} else if(isMultipleOf(numberToResolve, 5)) {
			return "Buzz";
		} 
		
		return numberToResolve+"";
	}

}
