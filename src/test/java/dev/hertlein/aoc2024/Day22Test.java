package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day22.Calculator;
import dev.hertlein.aoc2024.Day22.PriceSequence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.Day22.Buyer;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day22")
class Day22Test {

    private final Day22 day = new Day22();

    @Nested
    class Units {

        @Nested
        @DisplayName("Calculator")
        class CalculatorTests {

            @Nested
            class Mix {

                @Test
                void shouldReturnMixResult() {
                    assertThat(Calculator.mix(42, 15)).isEqualTo(37);
                }
            }

            @Nested
            class Prune {

                @Test
                void shouldReturnPruneResult() {
                    assertThat(Calculator.prune(100_000_000)).isEqualTo(16_113_920);
                }
            }

            @Nested
            class Next {

                @ParameterizedTest
                @CsvSource({
                        "123,15887950",
                        "15887950,16495136",
                        "16495136,527345",
                        "527345,704524",
                        "704524,1553684",
                        "1553684,12683156",
                        "12683156,11100544",
                        "11100544,12249484",
                        "12249484,7753432",
                        "7753432,5908254",
                })
                void shouldReturnNextNumber(long given, long next) {
                    assertThat(Calculator.nextNumber(given)).isEqualTo(next);
                }
            }
        }

        @Nested
        @DisplayName("Buyer")
        class BuyerTests {

            @Nested
            class NthSecretNumber {

                @ParameterizedTest
                @CsvSource({
                        "1,15887950",
                        "2,16495136",
                        "3,527345",
                        "4,704524",
                        "5,1553684",
                        "6,12683156",
                        "7,11100544",
                        "8,12249484",
                        "9,7753432",
                        "10,5908254",
                })
                void shouldReturnResultIfNthIsVarying(int nth, long expected) {
                    var nthSecretNumber = new Buyer(123).nthSecretNumber(nth);

                    assertThat(nthSecretNumber).isEqualTo(expected);
                }

                @ParameterizedTest
                @CsvSource({
                        "1,8685429",
                        "10,4700978",
                        "100,15273692",
                        "2024,8667524",
                })
                void shouldReturnResultIfInitialNumberIsVarying(int initial, long expected) {
                    var nthSecretNumber = new Buyer(initial).nthSecretNumber(2_000);

                    assertThat(nthSecretNumber).isEqualTo(expected);
                }
            }

            @Nested
            class Prices {

                @Test
                void shouldReturnPrices() {
                    var prices = new Buyer(123).prices(10);

                    assertThat(prices).containsExactly(3L, 0L, 6L, 5L, 4L, 4L, 6L, 4L, 4L, 2L);
                }
            }

            @Nested
            class PriceChanges {

                @Test
                void shouldReturnPriceChanges() {
                    var expected = new HashMap<PriceSequence, Long>();
                    expected.put(new PriceSequence(-3, 6, -1, -1), 4L);
                    expected.put(new PriceSequence(6, -1, -1, 0), 4L);
                    expected.put(new PriceSequence(-1, -1, 0, 2), 6L);
                    expected.put(new PriceSequence(-1, 0, 2, -2), 4L);
                    expected.put(new PriceSequence(0, 2, -2, 0), 4L);
                    expected.put(new PriceSequence(2, -2, 0, -2), 2L);

                    var priceChanges = new Buyer(123).priceChanges(10);

                    assertThat(priceChanges).containsAllEntriesOf(expected);
                }
            }

        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(37_327_623L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(146_917_570_43L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(List.of("1", "2", "3", "2024"), EMPTY)).isEqualTo(23L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(1_831L);
        }
    }
}
