package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day11.Stones;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day11")
class Day11Test {

    private final Day11 day = new Day11();

    @Nested
    class Units {

        @Nested
        @DisplayName("Stones")
        class StonesTests {

            @Nested
            class CountStonesAfterBlinks {

                @ParameterizedTest
                @CsvSource({
                        "1, 0 1 10 99 999, 7",
                        "1, 125 17, 3",
                        "2, 125 17, 4",
                        "3, 125 17, 5",
                        "4, 125 17, 9",
                        "5, 125 17, 13",
                        "6, 125 17, 22",
                })
                void shouldCountStones(int blinks, String stones, int expected) {
                    assertThat(Stones.of(List.of(stones)).countStonesAfterBlinks(blinks)).isEqualTo(expected);
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(55_312L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(231_278L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(65_601_038_650_482L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(274_229_228_071_551L);
        }
    }
}
