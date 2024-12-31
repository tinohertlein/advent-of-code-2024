package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.Day25.Schematic;
import static dev.hertlein.aoc2024.Day25.Type;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day25")
class Day25Test {

    private final Day25 day = new Day25();

    @Nested
    class Units {

        @Nested
        @DisplayName("Schematic")
        class SchematicTests {

            @Nested
            class Of {

                @Test
                void shouldCreateLock() {
                    var input = List.of(
                            "#####",
                            ".####",
                            ".####",
                            ".####",
                            ".#.#.",
                            ".#...",
                            "....."
                    );

                    var schematic = Schematic.of(input);

                    assertThat(schematic).isEqualTo(new Schematic(Type.LOCK, List.of(0, 5, 3, 4, 3)));
                }

                @Test
                void shouldCreateKey() {
                    var input = List.of(
                            ".....",
                            "#....",
                            "#....",
                            "#...#",
                            "#.#.#",
                            "#.###",
                            "#####"
                    );

                    var schematic = Schematic.of(input);

                    assertThat(schematic).isEqualTo(new Schematic(Type.KEY, List.of(5, 0, 2, 1, 3)));
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(3);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(2_900);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(0L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(0L);
        }
    }
}
