package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day19.Towels;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("Day19")
class Day19Test {

    private final Day19 day = new Day19();

    @Nested
    class Units {

        @Nested
        @DisplayName("Towels")
        class TowelsTests {

            @Nested
            class New {

                @Test
                void shouldCreateTowels() {
                    var towels = new Towels(readSampleInputFor(day));

                    assertSoftly(softly -> {
                        softly.assertThat(towels.getPatterns()).containsExactlyInAnyOrder("r", "wr", "b", "g", "bwu", "rb", "gb", "br");
                        softly.assertThat(towels.getDesigns()).containsExactly("brwrr", "bggr", "gbbr", "rrbgbr", "ubwu", "bwurrg", "brgr", "bbrgwb");
                    });
                }
            }

            @Nested
            class IsPossible {

                @ParameterizedTest
                @CsvSource({
                        "brwrr,true",
                        "bggr,true",
                        "gbbr,true",
                        "rrbgbr,true",
                        "ubwu,false",
                        "bwurrg,true",
                        "brgr,true",
                        "bbrgwb,false"
                })
                void shouldReturnIfDesignIsPossible(String design, boolean expected) {
                    var towels = new Towels(readSampleInputFor(day));

                    assertThat(towels.isPossible(design)).isEqualTo(expected);
                }
            }

            @Nested
            class PossibleArrangementsFor {

                @ParameterizedTest
                @CsvSource({
                        "brwrr,2",
                        "bggr,1",
                        "gbbr,4",
                        "rrbgbr,6",
                        "ubwu,0",
                        "bwurrg,1",
                        "brgr,2",
                        "bbrgwb,0"
                })
                void shouldReturnNumberOfPossibleArrangements(String design, int expected) {
                    var towels = new Towels(readSampleInputFor(day));

                    assertThat(towels.possibleArrangementsFor(design)).isEqualTo(expected);
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(6L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(296L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(16L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(619_970_556_776_002L);
        }
    }
}
