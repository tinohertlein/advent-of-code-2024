package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day16")
class Day16Test {

    private final Day16 day = new Day16();

    private final static List<String> FIRST_SAMPLE = List.of(
            "###############",
            "#.......#....E#",
            "#.#.###.#.###.#",
            "#.....#.#...#.#",
            "#.###.#####.#.#",
            "#.#.#.......#.#",
            "#.#.#####.###.#",
            "#...........#.#",
            "###.#.#####.#.#",
            "#...#.....#.#.#",
            "#.#.#.###.#.#.#",
            "#.....#...#.#.#",
            "#.###.#.#.#.#.#",
            "#S..#.....#...#",
            "###############"
    );

    private final static List<String> SECOND_SAMPLE = List.of(
            "#################",
            "#...#...#...#..E#",
            "#.#.#.#.#.#.#.#.#",
            "#.#.#.#...#...#.#",
            "#.#.#.#.###.#.#.#",
            "#...#.#.#.....#.#",
            "#.#.#.#.#.#####.#",
            "#.#...#.#.#.....#",
            "#.#.#####.#.###.#",
            "#.#.#.......#...#",
            "#.#.###.#####.###",
            "#.#.#...#.....#.#",
            "#.#.#.#####.###.#",
            "#.#.#.........#.#",
            "#.#.#.#########.#",
            "#S#.............#",
            "#################"
    );

    @Nested
    class Units {

    }

    @Nested
    class Part1 {

        @Test
        void sample1ShouldBeSolved() {
            assertThat(day.part1(FIRST_SAMPLE, EMPTY)).isEqualTo(7_036L);
        }

        @Test
        void sample2ShouldBeSolved() {
            assertThat(day.part1(SECOND_SAMPLE, EMPTY)).isEqualTo(11_048L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(994_48L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sample1ShouldBeSolved() {
            assertThat(day.part2(FIRST_SAMPLE, EMPTY)).isEqualTo(45L);
        }

        @Test
        void sample2ShouldBeSolved() {
            assertThat(day.part2(SECOND_SAMPLE, EMPTY)).isEqualTo(64L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(498L);
        }
    }
}
