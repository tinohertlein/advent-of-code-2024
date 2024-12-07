package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day07.Equation;
import dev.hertlein.aoc2024.Day07.Equations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.Day07.Operator.*;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day07")
class Day07Test {

    private final Day07 day = new Day07();

    @Nested
    class Units {

        @Nested
        @DisplayName("Equations")
        class EquationTests {

            @Nested
            class IsTestResultPossiblyCorrectWith {

                private final Equations equations = Equations.of(readSampleInputFor(day));

                private Equation equationAt(int index) {
                    return equations.equations().get(index);
                }

                @ParameterizedTest
                @ValueSource(ints = {0, 1, 8})
                void shouldReturnTrueForAddAndMultiply(int index) {
                    assertThat(equationAt(index).isTestResultPossiblyCorrectWith(Set.of(ADD, MULTIPLY))).isTrue();
                }

                @ParameterizedTest
                @ValueSource(ints = {2, 3, 4, 5, 6, 7})
                void shouldReturnFalseForAddAndMultiply(int index) {
                    assertThat(equationAt(index).isTestResultPossiblyCorrectWith(Set.of(ADD, MULTIPLY))).isFalse();
                }

                @ParameterizedTest
                @ValueSource(ints = {3, 4, 6})
                void shouldReturnTrueForAddAndMultiplyAndConcat(int index) {
                    assertThat(equationAt(index).isTestResultPossiblyCorrectWith(Set.of(ADD, MULTIPLY, CONCAT))).isTrue();
                }

                @ParameterizedTest
                @ValueSource(ints = {2, 5, 7})
                void shouldReturnFalseForAddAndMultiplyAndConcat(int index) {
                    assertThat(equationAt(index).isTestResultPossiblyCorrectWith(Set.of(ADD, MULTIPLY, CONCAT))).isFalse();
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(3_749L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(1_582_598_718_861L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(11_387L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(165_278_151_522_644L);
        }
    }
}
