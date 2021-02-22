# JUnit-and-Mockito
JUnit 5, Mockito, PowerMockito, code coverage

Gradle build commands: ./gradlew --i clean build

<b>gradlew: Permission Denied: chmod +x gradlew</b>

<p>when you mocked an Interface or Class, entire behaviour of that interface or class get lossed. A mock does not retain behaviour(code) of original class.
unlike a <b>Spy</b>, by default retain behaviour(code) of original class. 
</p>

<p><b>Mockito Limitations;</b>
  
  1. Static
  2. Final
  3. Constructors
  4. Private
  5. Enums
</p>
<p>use the <b>PowerMock</b> to overcome above limitations. PowerMock uses the Mockito & EasyMock.
    Mockito uses the Proxy Pattern to mock a dependent class, if that class declared as final, can not mock that class. While PowerMock uses the Byte Code Manipulation and Custom Class Loader to achieve the same. PowerMock uses the similar syntax for Subbing, verifying and setting the expectations as Mockito.
</p>

Best Practices & Principles:

1. FIRST Principle wrt Agile Methology;

Fast

Independent

Repeatable

Self-Validating

Timely

2. Test Doubles Patterns

Dummy

Stubs

Mocks

Fake

Spies
## JUnit 5
- org.junit.jupiter.api.Assertions.assertAll - use when using multiple assertEquals wrt verify object fields equality(two different objects are created & they are not equal to each other) and you don't know which assertion are going to be failed.
- import static org.junit.jupiter.api.Assertions.assertArrayEquals - use wrt array comparision
- import org.junit.jupiter.params.ParameterizedTest;

```ruby
@ParameterizedTest(name = "weight={0}, height={1}")
@CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.0, 1.78"})
void doubleParameterisedValueTestIsDietRecommended_Should_ReturnTrue_When_dietRecommended(Double coderWeight, Double coderHeight) {
		double weight = coderWeight;
		double height = coderHeight;
		
		boolean recommended = BMICalculator.isDietRecommended(weight, height);
		
		assertTrue(recommended, "recommended should true");
		
}
```
