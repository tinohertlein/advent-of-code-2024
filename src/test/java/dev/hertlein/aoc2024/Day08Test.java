package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day08.Antinode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.Day08.AntennaMap;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day08")
class Day08Test {

    private final Day08 day = new Day08();

    @Nested
    class Units {

        @Nested
        @DisplayName("AntennaMap")
        class AntennaMapTests {

            @Nested
            class FindAntinodes {

                @Nested
                class Simple {

                    @Test
                    void shouldCreateAntinodesForSample1() {
                        // Antennas: 4/3, 5/5
                        // Antinodes: 3/1, 6/7
                        var SAMPLE_1 = List.of(
                                "..........",
                                "...#......",
                                "..........",
                                "....a.....",
                                "..........",
                                ".....a....",
                                "..........",
                                "......#...",
                                "..........",
                                ".........."
                        );
                        var map = AntennaMap.of(SAMPLE_1);

                        assertThat(map.findAntinodes(map::findSimple)).containsExactlyInAnyOrder(
                                Antinode.of(3, 1),
                                Antinode.of(6, 7));
                    }

                    @Test
                    void shouldCreateAntinodesForSample2() {
                        // Antennas: 4/3, 8/4, 5/5
                        // Antinodes: 3/1, 0/2, 2/6, 6/7
                        var SAMPLE_2 = List.of(
                                "..........",
                                "...#......",
                                "#.........",
                                "....a.....",
                                "........a.",
                                ".....a....",
                                "..#.......",
                                "......#...",
                                "..........",
                                ".........."
                        );
                        var map = AntennaMap.of(SAMPLE_2);

                        assertThat(map.findAntinodes(map::findSimple)).containsExactlyInAnyOrder(
                                Antinode.of(3, 1),
                                Antinode.of(0, 2),
                                Antinode.of(2, 6),
                                Antinode.of(6, 7));
                    }

                    @Test
                    void shouldCreateAntinodesForSample3() {
                        var map = AntennaMap.of(readSampleInputFor(day));

                        assertThat(map.findAntinodes(map::findSimple)).containsExactlyInAnyOrder(
                                Antinode.of(6, 0),
                                Antinode.of(11, 0),
                                Antinode.of(3, 1),
                                Antinode.of(3, 1),
                                Antinode.of(4, 2),
                                Antinode.of(10, 2),
                                Antinode.of(2, 3),
                                Antinode.of(9, 4),
                                Antinode.of(1, 5),
                                Antinode.of(6, 5),
                                Antinode.of(3, 6),
                                Antinode.of(0, 7),
                                Antinode.of(7, 7),
                                Antinode.of(10, 10),
                                Antinode.of(10, 11)
                        );
                    }
                }

                @Nested
                class ResonantHarmonic {

                    @Test
                    void shouldCreateAntinodes() {
                        var SAMPLE = List.of(
                                "T....#....",
                                "...T......",
                                ".T....#...",
                                ".........#",
                                "..#.......",
                                "..........",
                                "...#......",
                                "..........",
                                "....#.....",
                                ".........."
                        );
                        var map = AntennaMap.of(SAMPLE);

                        assertThat(map.findAntinodes(map::findResonantHarmonic)).containsExactlyInAnyOrder(
                                Antinode.of(0, 0),
                                Antinode.of(1, 2),
                                Antinode.of(5, 0),
                                Antinode.of(3, 1),
                                Antinode.of(6, 2),
                                Antinode.of(9, 3),
                                Antinode.of(2, 4),
                                Antinode.of(3, 6),
                                Antinode.of(4, 8)
                        );
                    }
                }

            }
        }

    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(14L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(244L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(34L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(912L);
        }
    }
}
