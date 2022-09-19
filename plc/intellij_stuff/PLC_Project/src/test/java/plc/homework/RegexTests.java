package plc.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Contains JUnit tests for {@link Regex}. A framework of the test structure 
 * is provided, you will fill in the remaining pieces.
 *
 * To run tests, either click the run icon on the left margin, which can be used
 * to run all tests or only a specific test. You should make sure your tests are
 * run through IntelliJ (File > Settings > Build, Execution, Deployment > Build
 * Tools > Gradle > Run tests using <em>IntelliJ IDEA</em>). This ensures the
 * name and inputs for the tests are displayed correctly in the run window.
 */
public class RegexTests {

    /**
     * This is a parameterized test for the {@link Regex#EMAIL} regex. The
     * {@link ParameterizedTest} annotation defines this method as a
     * parameterized test, and {@link MethodSource} tells JUnit to look for the
     * static method {@link #testEmailRegex()}.
     *
     * For personal preference, I include a test name as the first parameter
     * which describes what that test should be testing - this is visible in
     * IntelliJ when running the tests (see above note if not working).
     */
    @ParameterizedTest
    @MethodSource
    public void testEmailRegex(String test, String input, boolean success) {
        test(input, Regex.EMAIL, success);
    }

    /**
     * This is the factory method providing test cases for the parameterized
     * test above - note that it is static, takes no arguments, and has the same
     * name as the test. The {@link Arguments} object contains the arguments for
     * each test to be passed to the function above.
     */
    public static Stream<Arguments> testEmailRegex() {
        return Stream.of(
                // BEGIN MATCHING TEST CASES
                Arguments.of("Alphanumeric", "thelegend27@gmail.com", true), // Given test case
                Arguments.of("UF Domain", "otherdomain@ufl.edu", true), // Given test case
                Arguments.of("Multiple subdomains", "whatsup@rice.and.beans.yahoo.com", true),
                Arguments.of("Minimum length", "aa@b.ccc", true),
                Arguments.of("All types of allowed characters", "AZaz09._@AZaz09~.org", true),
                Arguments.of("All allowed characters", "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "._" + "@" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "~" + ".xyz", true),

                // BEGIN NON-MATCHING TEST CASES
                Arguments.of("Missing Domain Dot", "missingdot@gmailcom", false), // Given test case
                Arguments.of("Symbols", "symbols#$%@gmail.com", false), // Given test case
                Arguments.of("1 character before the @", "a@gmail.com", false),
                Arguments.of("0 characters before the @", "@gmail.com", false),
                Arguments.of("Illegal symbol (underscore) after the @", "test@test_.com", false),
                Arguments.of("Illegal symbol (*) after the @", "test@test*.com", false),
                Arguments.of("Multiple @ symbols", "sonic@@sega.com", false),
                Arguments.of("Period next to @", "mario@.com", false),
                Arguments.of("Adjacent periods", "mario@nintendo..com", false),
                Arguments.of("More than 3 characters in the Top Level Domain", "fedora@linux.club", false),
                Arguments.of("Less than 3 characters in the Top Level Domain", "debian@linux.co", false),
                Arguments.of("Zero characters in the Top Level Domain", "manjaro@linux.", false)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testOddStringsRegex(String test, String input, boolean success) {
        test(input, Regex.ODD_STRINGS, success);
    }

    public static Stream<Arguments> testOddStringsRegex() {
        return Stream.of(
                // BEGIN MATCHING TEST CASES
                // what have eleven letters and starts with gas? alternate answer... gastronomic
                Arguments.of("11 Characters (minimum)", "automobiles", true), // given test case
                Arguments.of("13 Characters", "i<3pancakes13", true), // given test case
                Arguments.of("15 Characters", "asdfghjklzxcvbn", true),
                Arguments.of("17 Characters", "asdfghjklzxcvbnqw", true),
                Arguments.of("19 Characters (maximum)", "asdfghjklzxcvbnqwer", true),
                Arguments.of("Symbols and a space", "@#$%^&*()+_-=' ", true),
                Arguments.of("Numbers only", "1234567890986753219", true),

                //BEGIN NON-MATCHING TEST CASES
                Arguments.of("5 Characters", "5five", false), // given test case
                Arguments.of("14 Characters", "i<3pancakes14!", false), //given test case
                Arguments.of("0 Characters", "", false),
                Arguments.of("1 Character", "a", false),
                Arguments.of("3 Characters", "aaa", false),
                Arguments.of("9 Characters", "aaaaaaaaa", false),
                Arguments.of("10 Characters", "aaaaaaaaaa", false),
                Arguments.of("12 Characters", "aaaaaaaaaaaa", false),
                Arguments.of("14 Characters", "aaaaaaaaaaaaaa", false),
                Arguments.of("16 Characters", "aaaaaaaaaaaaaaaa", false),
                Arguments.of("18 Characters", "aaaaaaaaaaaaaaaaaa", false),
                Arguments.of("20 Characters", "aaaaaaaaaaaaaaaaaaaa", false),
                Arguments.of("21 Characters", "aaaaaaaaaaaaaaaaaaaaa", false),
                Arguments.of("22 Characters", "aaaaaaaaaaaaaaaaaaaaaa", false)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testCharacterListRegex(String test, String input, boolean success) {
        test(input, Regex.CHARACTER_LIST, success);
    }

    public static Stream<Arguments> testCharacterListRegex() {
        return Stream.of(
                // BEGIN MATCHING TEST CASES
                Arguments.of("Single Element", "['a']", true), // given test case
                Arguments.of("Multiple Elements", "['a','b','c']", true), // given test case
                Arguments.of("Zero elements", "[]", true),
                Arguments.of("Numbers", "['1', '2', '0']", true),
                Arguments.of("Symbols", "['!', '#', ' ']", true),
                Arguments.of("Tab character", "['\t']", true),
                Arguments.of("Single quote character", "['\'']", true),
                Arguments.of("Double quote character", "['\"']", true),
                Arguments.of("Backspace character", "['\b']", true),
                Arguments.of("Formfeed character", "['\f']", true),
                Arguments.of("Literal backslash character", "['\\']", true),
                Arguments.of("Uneven spacing", "[ 'a','#', 's', '9' ]", true),

                // BEGIN NON-MATCHING TEST CASES
                Arguments.of("Missing Brackets", "'a','b','c'", false), // given test case
                Arguments.of("Missing Commas", "['a' 'b' 'c']", false), // given test case
                Arguments.of("Newline character", "['\n']", false), // given test case
                Arguments.of("Trailing comma on single character", "['a',]", false), // given test case
                Arguments.of("Trailing comma on multiple characters", "['1, 2, 3,']", false), // given test case
                Arguments.of("Trailing comma", "['1, 2, 3,']", false), // given test case
                Arguments.of("Multiple characters in an element", "['aa']", false), // given test case
                Arguments.of("Multiple characters in an element, various elements", "['aa', 'a', 'a']", false), // given test case
                Arguments.of("Multiple characters in an element", "['aa']", false), // given test case
                Arguments.of("Missing closing bracket", "['a', 'b', 'c'", false), // given test case
                Arguments.of("Missing opening bracket", "'a', 'b', 'c']", false), // given test case
                Arguments.of("Leading comma on first element", "[,'a', 'b', 'c']", false) // given test case
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testDecimalRegex(String test, String input, boolean success) {
        test(input, Regex.DECIMAL, success);
    }

    public static Stream<Arguments> testDecimalRegex() {
        return Stream.of(
                // BEGIN MATCHING TEST CASES
                Arguments.of("", "10100.001", true), // given test case
                Arguments.of("", "-1.0", true), // given test case
                Arguments.of("", "0.0", true),
                Arguments.of("", "90010.9", true),
                Arguments.of("", "9.0", true),
                Arguments.of("", "0.9", true),
                Arguments.of("", "-0.9", true),
                Arguments.of("", "-9.0", true),
                Arguments.of("", "-1.10000000000", true),
                Arguments.of("", "1.10000000000", true),

                // BEGIN NON-MATCHING TEST CASES
                Arguments.of("", "1", false), // given test case
                Arguments.of("", ".5", false), // given test case
                Arguments.of("", "-.5", false),
                Arguments.of("", "01.1", false),
                Arguments.of("", "-01.1", false),
                Arguments.of("", "0000001.1", false),
                Arguments.of("", "-0000001.1", false),
                Arguments.of("", "a.1", false),
                Arguments.of("", "1.a", false),
                Arguments.of("", "a.a", false),
                Arguments.of("", "1..1", false),
                Arguments.of("", "@.1", false),
                Arguments.of("", "1.#", false),
                Arguments.of("", "#.#", false),
                Arguments.of("", "#.#", false),
                Arguments.of("", ".1.1", false),
                Arguments.of("", ".1.1.", false),
                Arguments.of("", "1.1.", false),
                Arguments.of("", "1.1.", false),
                Arguments.of("", "0.-1", false),
                Arguments.of("", "-0.-1", false)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testStringRegex(String test, String input, boolean success) {
        test(input, Regex.STRING, success);
    }

    public static Stream<Arguments> testStringRegex() {
        return Stream.of(
                // BEGIN MATCHING TEST CASES
                Arguments.of("", "\"\"", true), // given test case
                Arguments.of("", "\"Hello, World!\"", true), // given test case
                Arguments.of("", "\"1\\t2\"", true),
                Arguments.of("", "\"lowercase\"", true),
                Arguments.of("", "\"UPPERCASE\"", true),
                Arguments.of("", "\"SyMbOlS!@#$%^&*()\"", true),
                Arguments.of("", "\"           \"", true),
                Arguments.of("", "\"\\b\\n\\r\\t\\\"\\\'\"", true),

                // BEGIN NON-MATCHING TEST CASES
                Arguments.of("", "\"unterminated", false), // given test case
                Arguments.of("", "invalid\\escape", false), // given test case
                Arguments.of("", "no opening quote\"", false),
                Arguments.of("", "no quotes", false),
                Arguments.of("", "\"\\\"", false),
                Arguments.of("", "\"\\\"", false)

        );
    }

    /**
     * Asserts that the input matches the given pattern. This method doesn't do
     * much now, but you will see this concept in future assignments.
     */
    private static void test(String input, Pattern pattern, boolean success) {
        Assertions.assertEquals(success, pattern.matcher(input).matches());
    }

}
