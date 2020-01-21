package pt.fizzbuzz;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FizzBuzzTest {
	
	  @Test
	  public void it_shoud_create_a_fizz_buzz_instance(){

	    FizzBuzz fizzBuzz = new FizzBuzz();
	    
	    Assert.assertNotSame(fizzBuzz, null); 
	    
	  }
	  

	  @Test
	  public void it_should_return_if_a_number_is_multiple_of_3() {
		  FizzBuzz fizzBuzz = new FizzBuzz();
		    
		  Assert.assertEquals(fizzBuzz.isMultipleOf3(3), true);
		  
	  }


	  @Test
	  public void  it_should_return_if_a_number_is_multiple_of_5(){

	    FizzBuzz fizzBuzz = new FizzBuzz();
	    
	    Assert.assertEquals(fizzBuzz.isMultipleOf5(5), true);

	  }
	  
	  @Test
	  public void it_should_return_if_a_number_is_multiple_of_3_and_5(){

		FizzBuzz fizzBuzz = new FizzBuzz();
	    
		Assert.assertEquals(fizzBuzz.isMultipleOf3And5(30), true);

	  }

	  
	  @Test
	  public void it_shoud_make_fizz(){

	    FizzBuzz fizzBuzz = new FizzBuzz();

	    int value = 3;
	    
	    Assert.assertEquals(fizzBuzz.sendMessage(value), "fizz");
	  }
	  
	  @Test
	  public void it_shoud_make_buzz(){

	    FizzBuzz fizzBuzz = new FizzBuzz();

	    int value = 5;
	    
	    Assert.assertEquals(fizzBuzz.sendMessage(value), "buzz");
	  }
	  
	  @Test
	  public void it_shoud_make_fizzbuzz(){

	    FizzBuzz fizzBuzz = new FizzBuzz();

	    int value = 15;
	    
	    Assert.assertEquals(fizzBuzz.sendMessage(value), "fizzbuzz");
	  }
	  
	  
}
