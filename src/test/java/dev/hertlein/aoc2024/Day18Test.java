package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day18.AdditionalInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.hertlein.aoc2024.Day18.Position;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day18")
class Day18Test {

    final static AdditionalInput ADDITIONAL_INPUT_SAMPLE = new AdditionalInput(new Position(0, 0), new Position(6, 6), 12);
    final static AdditionalInput ADDITIONAL_INPUT_CHALLENGE = new AdditionalInput(new Position(0, 0), new Position(70, 70), 1024);

    private final Day18 day = new Day18();

    @Nested
    class Units {

    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), ADDITIONAL_INPUT_SAMPLE)).isEqualTo(22L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), ADDITIONAL_INPUT_CHALLENGE)).isEqualTo(316L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), ADDITIONAL_INPUT_SAMPLE)).isEqualTo("6,1");
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), ADDITIONAL_INPUT_CHALLENGE)).isEqualTo("45,18");
        }
    }
}
