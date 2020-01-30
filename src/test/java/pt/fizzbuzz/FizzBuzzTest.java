package pt.fizzbuzz;

import pt.fizzbuzz.FizzBuzz;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FizzBuzzTest {
	
	@Test
	public void it_should_assert_that_a_fizzbuzz_instance_is_not_null(){
		FizzBuzz fizzBuzz = new FizzBuzz();
		
		Assert.assertNotNull(fizzBuzz);
	}
	
	@Test
	public void it_should_assert_that_a_number_is_multiple_of_another() {
		//If the value is a multiple of 3: use the value 'Fizz' instead
		FizzBuzz fizzBuzz = new FizzBuzz();
		Assert.assertTrue(fizzBuzz.isMultipleOf(3,3));
		Assert.assertTrue(fizzBuzz.isMultipleOf(15,5));
		Assert.assertTrue(fizzBuzz.isMultipleOf(15,3));
	}
	
	@Test
	public void it_should_assert_that_a_number_a_multiple_of_3_returns_fizz() {	
		FizzBuzz fizzBuzz = new FizzBuzz();
		Assert.assertEquals(fizzBuzz.sendNumber(3), "Fizz");
	}

	@Test
	public void it_should_assert_that_a_number_a_multiple_of_5_returns_buzz() {	
		FizzBuzz fizzBuzz = new FizzBuzz();
		Assert.assertEquals(fizzBuzz.sendNumber(5), "Buzz");
	}

}
