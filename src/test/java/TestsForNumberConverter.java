import com.example.Main;
import com.example.NumeralForms;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestsForNumberConverter {

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(0, "Ж", "И", "ноль"),
                Arguments.of(5, "С", "Р", "пяти"),
                Arguments.of(-5, "М", "Д", "минус пяти"),
                Arguments.of(111, "М", "В", "сто одиннадцать"),
                Arguments.of(24, "Ж", "Т", "двадцатью четырьмя"),
                Arguments.of(1903, "М", "П", "одной тысяче девятистах трёх"),
                Arguments.of(29467, "М", "И", "двадцать девять тысяч четыреста шестьдесят семь"),
                Arguments.of(120, "М", "И", "сто двадцать"),
                Arguments.of(100000, "Ж", "Д", "ста тысячам "),
                Arguments.of(13000, "М", "И", "тринадцать тысяч "),
                Arguments.of(61_003_603_035L, "М", "И", "шестьдесят один миллиард три миллиона шестьсот три тысячи тридцать пять")
        );
    }private static Stream<Arguments> exceptionTestData() {
        return Stream.of(
                Arguments.of(0, "shto", "И", "Неверный формат пола."),
                Arguments.of(1, "М", "А", "Неверный формат падежа."),
                Arguments.of(1_000_000_000_000L, "М", "П", "Данное число вне границ."),
                Arguments.of(-1_000_000_000_000L, "М", "П", "Данное число вне границ.")
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSumProp(long nSum, String sGender, String sCase, String expected) {
        assertEquals(expected, Main.sumProp(nSum, sGender, sCase),
                () -> String.format("На вход подавались значения: %d, %s, %s", nSum, sGender, sCase));
    }

    @ParameterizedTest
    @MethodSource("exceptionTestData")
    void testSumPropExceptions(long nSum, String sGender, String sCase, String expected) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Main.sumProp(nSum, sGender, sCase));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testGetCaseIndex() {
        assertEquals(0, NumeralForms.getCaseIndex("qwer"), "Тест для 100% покрытия");
    }
}