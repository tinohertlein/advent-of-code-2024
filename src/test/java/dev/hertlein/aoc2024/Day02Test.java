package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day02")
public class Day02Test {

    private final Day02 day = new Day02();

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day))).isEqualTo(2);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day))).isEqualTo(371L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day))).isEqualTo(4);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day))).isEqualTo(426);
        }
    }
}
