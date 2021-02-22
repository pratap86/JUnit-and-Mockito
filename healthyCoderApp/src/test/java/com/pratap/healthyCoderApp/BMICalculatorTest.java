package com.pratap.healthyCoderApp;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BMICalculatorTest {
	
	private String environment = "dev";
	
	@BeforeAll
	static void beforeAll() {
		System.out.println("setup DB connection..");
	}
	
	@AfterAll
	static void afterAll() {
		System.out.println("closing DB connection..");
	}
	
	@Nested
	class IsDietRecommendedTests{
		
		@Test
		void testIsDietRecommended_Should_ReturnTrue_When_dietRecommended() {
			System.out.println("executing testIsDietRecommended_Should_ReturnTrue_When_dietRecommended..");
			// given
			double weight = 85;
			double height = 1.57;
			//when
			boolean recommended = BMICalculator.isDietRecommended(weight, height);
			//then
			assertTrue(recommended, "recommended should true");
			
			System.out.println("executed testIsDietRecommended_Should_ReturnTrue_When_dietRecommended..");
		}
		
		@Test
		void testIsDietRecommended_Should_ReturnFalse_When_dietNotRecommended() {
			// given
			double weight = 50;
			double height = 1.92;
			//when
			boolean recommended = BMICalculator.isDietRecommended(weight, height);
			//then
			assertFalse(recommended, "recommended should false");
		}
		
		@Test
		void testIsDietRecommended_Should_ThrowArithmeticException_When_HeightZero() {
			// given
			double weight = 50;
			double height = 0.0;
			//when
			Executable executable = () -> BMICalculator.isDietRecommended(weight, height);
			//then
			assertThrows(ArithmeticException.class, executable);
		}
		
		@ParameterizedTest(name = "weight={0}")
		@ValueSource(doubles = {89.0, 95.0, 110})
		void singleParameterisedValueTestIsDietRecommended_Should_ReturnTrue_When_dietRecommended(Double coderWeight) {
			double weight = coderWeight;
			double height = 1.57;
			
			boolean recommended = BMICalculator.isDietRecommended(weight, height);
			
			assertTrue(recommended, "recommended should true");
			
		}
		
		@ParameterizedTest(name = "weight={0}, height={1}")
		@CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.0, 1.78"})
		void doubleParameterisedValueTestIsDietRecommended_Should_ReturnTrue_When_dietRecommended(Double coderWeight, Double coderHeight) {
			double weight = coderWeight;
			double height = coderHeight;
			
			boolean recommended = BMICalculator.isDietRecommended(weight, height);
			
			assertTrue(recommended, "recommended should true");
			
		}
		
		@ParameterizedTest(name = "weight={0}, height={1}")
		@CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)// numLinesToSkip to skip the first Header Line
		void passParametersThroughCSVFileTestIsDietRecommended_Should_ReturnTrue_When_dietRecommended(Double coderWeight, Double coderHeight) {
			double weight = coderWeight;
			double height = coderHeight;
			
			boolean recommended = BMICalculator.isDietRecommended(weight, height);
			
			assertTrue(recommended, "recommended should true");
			
		}
	}

	@Nested
	class FindCoderWithWorstBMITests{
	
		@Test
		void testFindCoderWithWorstBMI_ReturnWorstBMICoder_When_CoderListNotEmpty() {
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.82, 64.7));
			
			Coder worstBMICoder = BMICalculator.findCoderWithWorstBMI(coders);
			// assertAll is very useful multiple assertions are failing
			assertAll(
					() -> assertEquals(1.82, worstBMICoder.getHeight()),
					() -> assertEquals(98.0, worstBMICoder.getWeight())
			);
		}
		
		@Test
		void testFindCoderWithWorstBMI_ReturnNullBMICoder_When_CoderListEmpty() {
			List<Coder> coders = new ArrayList<>();
			
			Coder worstBMICoder = BMICalculator.findCoderWithWorstBMI(coders);
			
			assertNull(worstBMICoder);
		}
		
		@Test
		void testFindCoderWithWorstBMIIn1Ms_ReturnWorstBMICoder_When_CoderListHas10000Elements() {
			
			assumeTrue(environment.equals("prod"));
			List<Coder> coders = new ArrayList<>();
			for(int i = 0; i < 10000; i++) {
				coders.add(new Coder(1.0 + i, 10.0 + i));
			}
			
			assertTimeout(Duration.ofMillis(100), () -> BMICalculator.findCoderWithWorstBMI(coders));
		}
	}

	@Nested
	@DisplayName("{}>>>> sample inner class display name")
	class GetBMIScoresTests{
		
		@Test
		@DisplayName(">>>>> sample display test method name")
		@DisabledOnOs(OS.WINDOWS)
		void testGetBMIScores_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty() {
			
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.82, 64.7));
			
			double[] expected = {18.52, 29.59, 19.53};
			
			assertArrayEquals(expected, BMICalculator.getBMIScores(coders));
		}

		@Test
		void testGetBMIScores_Should_ThrowArithmeticException_When_CoderHeightZero() {
			
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(0.0, 98.0));
			coders.add(new Coder(1.82, 64.7));
			
			assertThrows(ArithmeticException.class, () -> BMICalculator.getBMIScores(coders));
		}
	}
	
}
