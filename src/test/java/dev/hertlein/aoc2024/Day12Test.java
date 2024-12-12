package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day12")
class Day12Test {

    private final Day12 day = new Day12();

    @Nested
    class Units {

        private final static List<String> SIMPLE_INPUT_1 = List.of(
                "AAAA",
                "BBCD",
                "BBCC",
                "EEEC"
        );

        private final static List<String> SIMPLE_INPUT_2 = List.of(
                "OOOOO",
                "OXOXO",
                "OOOOO",
                "OXOXO",
                "OOOOO"
        );

        private final static List<String> SIMPLE_INPUT_3 = List.of(
                "EEEEE",
                "EXXXX",
                "EEEEE",
                "EXXXX",
                "EEEEE"
        );

        @Nested
        class Garden {

            @Nested
            class FenceCost {

                @Test
                void shouldReturnCostsForSample1() {
                    assertThat(new Day12.Garden(SIMPLE_INPUT_1).fenceCost()).isEqualTo(140);
                }

                @Test
                void shouldReturnCostsForSample2() {
                    assertThat(new Day12.Garden(SIMPLE_INPUT_2).fenceCost()).isEqualTo(772);
                }
            }

            @Nested
            class FenceCostWithBulkDiscount {

                @Test
                void shouldReturnCostsForSample1() {
                    assertThat(new Day12.Garden(SIMPLE_INPUT_1).fenceCostWithBulkDiscount()).isEqualTo(80);
                }

                @Test
                void shouldReturnCostsForSample2() {
                    assertThat(new Day12.Garden(SIMPLE_INPUT_2).fenceCostWithBulkDiscount()).isEqualTo(436);
                }

                @Test
                void shouldReturnCostsForSample3() {
                    assertThat(new Day12.Garden(SIMPLE_INPUT_3).fenceCostWithBulkDiscount()).isEqualTo(236);
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(1_930L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(1_371_306L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(1_206L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(805_880L);
        }
    }
}
