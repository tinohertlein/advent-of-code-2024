package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day01.LocationLists;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day01")
public class Day01Test {

    private final Day01 day = new Day01();

    @Nested
    class Units {

        @Nested
        class LocationLists_Of {

            private final static List<String> INPUT = List.of("3   4", "4   3", "2   5");

            @Test
            void shouldTransformInputToModel() {
                assertThat(LocationLists.of(INPUT))
                        .isEqualTo(new LocationLists(
                                List.of(3, 4, 2),
                                List.of(4, 3, 5)
                        ));
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day))).isEqualTo(11);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day))).isEqualTo(2057374);
        }
    }

    @Nested
    class Part2 {

        @Test
        void part1ShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day))).isEqualTo(31);
        }

        @ChallengeTest
        void part2ShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day))).isEqualTo(23177084);
        }
    }
}
