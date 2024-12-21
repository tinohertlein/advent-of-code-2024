package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day21.Starship;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day21")
class Day21Test {

    private final Day21 day = new Day21();

    @Nested
    class Units {

        @Nested
        @DisplayName("Starship")
        class StarshipTests {

            @Nested
            class LengthOfShortestSequenceFor {
                @ParameterizedTest
                @CsvSource({
                        "029A,68",
                        "980A,60",
                        "179A,68",
                        "456A,64",
                        "379A,64"
                })
                void shouldReturnResult(String input, int expected) {
                    var door = new Starship(3);

                    assertThat(door.lengthOfShortestSequenceFor(input)).isEqualTo(expected);
                }
            }

            @Nested
            class ComplexityOf {

                @ParameterizedTest
                @CsvSource({
                        "029A,1972",
                        "980A,58800",
                        "179A,12172",
                        "456A,29184",
                        "379A,24256"
                })
                void shouldReturnComplexity(String input, int expected) {
                    var door = new Starship(3);

                    assertThat(door.complexityOf(input)).isEqualTo(expected);
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(126_384L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(197_560L);
        }
    }

    @Nested
    class Part2 {

        @Test
        @Disabled("No sample for part 2 provided")
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(0);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(242_337_182_910_752L);
        }
    }
}
