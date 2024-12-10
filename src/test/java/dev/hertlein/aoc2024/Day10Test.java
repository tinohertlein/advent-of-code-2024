package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day10.TopologyMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day10")
class Day10Test {

    private final Day10 day = new Day10();

    @Nested
    class Units {

        @Nested
        @DisplayName("TopologyMap")
        class TopologyMapTests {

            @Nested
            class SumOfTrailheadsScores {

                private final TopologyMap input = new TopologyMap(List.of(
                        "0123",
                        "1234",
                        "8765",
                        "9876"));

                @Test
                void shouldCalculateScore() {
                    assertThat(input.sumOfTrailheadsScores()).isEqualTo(1);
                }
            }

            @Nested
            class SumOfTrailheadsRatings {

                private final TopologyMap input = new TopologyMap(List.of(
                        ".....0.",
                        "..4321.",
                        "..5..2.",
                        "..6543.",
                        "..7..4.",
                        "..8765.",
                        "..9...."
                ));

                @Test
                void shouldCalculateRating() {
                    assertThat(input.sumOfTrailheadsRatings()).isEqualTo(3);
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(36L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(760L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(81L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(1_764L);
        }
    }
}
