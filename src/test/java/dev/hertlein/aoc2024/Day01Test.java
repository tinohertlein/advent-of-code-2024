package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day01.LocationLists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day01")
class Day01Test {

    private final Day01 day = new Day01();

    @Nested
    class Units {

        @Nested
        @DisplayName("LocationLists")
        class LocationListsTests {

            @Nested
            class Of {

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
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(11L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(2_057_374L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void part1ShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(31L);
        }

        @ChallengeTest
        void part2ShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(23_177_084L);
        }
    }
}
