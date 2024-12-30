package dev.hertlein.aoc2024;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day24")
class Day24Test {

    private static final List<String> SMALL_SAMPLE = List.of(
            "x00: 1",
            "x01: 1",
            "x02: 1",
            "y00: 0",
            "y01: 1",
            "y02: 0",
            "",
            "x00 AND y00 -> z00",
            "x01 XOR y01 -> z01",
            "x02 OR y02 -> z02"
    );

    private final Day24 day = new Day24();

    @Nested
    class Units {

    }

    @Nested
    class Part1 {

        @Test
        void smallSampleShouldBeSolved() {
            var output = day.part1(SMALL_SAMPLE, EMPTY);

            assertThat(output).isEqualTo(4L);
        }

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(2024L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(42_410_633_905_894L);
        }
    }

    @Nested
    class Part2 {

        @Test
        @Disabled("No sample for part 2 provided")
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(0);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo("cqm,mps,vcv,vjv,vwp,z13,z19,z25");
        }
    }
}
