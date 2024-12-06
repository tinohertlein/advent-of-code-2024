package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.Day06.Coordinate;
import static dev.hertlein.aoc2024.Day06.Lab;
import static dev.hertlein.aoc2024.Day06.PatrolPath.STUCK;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day06")
class Day06Test {

    private final Day06 day = new Day06();

    @Nested
    class Units {

        private final List<String> labWithObstruction = List.of(
                "....#.....",
                ".........#",
                "..........",
                "..#.......",
                ".......#..",
                "..........",
                ".#.#^.....",
                "........#.",
                "#.........",
                "......#..."
        );

        @Nested
        @DisplayName("Lab")
        class LabTests {

            @Nested
            class PatrolPath {

                @Test
                void shouldReturnSTUCKIfStuck() {
                    assertThat(new Lab(labWithObstruction).patrolPath()).isEqualTo(STUCK);
                }

                @Test
                void shouldReturnPathIfNotStuck() {
                    assertThat(new Lab(readSampleInputFor(day)).patrolPath().length()).isEqualTo(41);
                }
            }

            @Nested
            class NewLabWithObstructionAt {

                @Test
                void shouldReturnNewLabWithObstructionAt() {
                    Lab initialLab = new Lab(readSampleInputFor(day));

                    var newLabWithObstructionAt = initialLab.newLabWithObstructionAt(new Coordinate(3, 6));

                    assertThat(newLabWithObstructionAt).isEqualTo(new Lab(labWithObstruction));
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(41L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(4_696L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(6L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(1_443L);
        }
    }
}
