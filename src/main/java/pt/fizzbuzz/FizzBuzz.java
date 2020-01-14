package pt.fizzbuzz;

public class FizzBuzz {

	FizzBuzz(){
        
    }

    public boolean isMultipleOf3(int value) {
        boolean isMultipleOf3 = false;

        if(value % 3 == 0){
            isMultipleOf3 = true;
        }

        return isMultipleOf3;
    }

    public boolean isMultipleOf5(int value) {
       boolean isMultipleOf5 = false;

        if(value % 5 == 0){
            isMultipleOf5 = true;
        }

        return isMultipleOf5;
    }

    public boolean isMultipleOf3And5(int value){
        boolean isMultipleOf3And5 = false;

        if(this.isMultipleOf3(value) && this.isMultipleOf5(value)){
            isMultipleOf3And5 = true;
        }

        return isMultipleOf3And5;
    }


    public String sendMessage(int value){
    	
    	String message = "";

        if(this.isMultipleOf3And5(value)){
        	message = "fizzbuzz";
        }else if(this.isMultipleOf5(value)){
        	message = "buzz";
        }else if(this.isMultipleOf3(value)){
        	message = "fizz";
        }
        
        return message;
    }

}
