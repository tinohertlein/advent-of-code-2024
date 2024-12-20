package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.hertlein.aoc2024.Day20.MinimumNumberOfPicosecondsToSave;
import static dev.hertlein.aoc2024.Day20.Racetrack;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day20")
class Day20Test {

    private final Day20 day = new Day20();

    @Nested
    class Units {

        @Nested
        @DisplayName("Racetrack")
        class RacetrackTests {

            @Nested
            class DistancesToStartPosition {

                @Test
                void shouldReturnDistances() {
                    var racetrack = new Racetrack(readSampleInputFor(day));

                    assertThat(racetrack.distancesToStartPosition().get(racetrack.getEnd())).isEqualTo(84);
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), new MinimumNumberOfPicosecondsToSave(2))).isEqualTo(44L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), new MinimumNumberOfPicosecondsToSave(100))).isEqualTo(1_372L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), new MinimumNumberOfPicosecondsToSave(50))).isEqualTo(285L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), new MinimumNumberOfPicosecondsToSave(100))).isEqualTo(979_014L);
        }
    }
}
