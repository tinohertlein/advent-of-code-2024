package dev.hertlein.aoc2024;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.Day17.Computer;
import static dev.hertlein.aoc2024.Day17.Register;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("Day17")
class Day17Test {

    private final Day17 day = new Day17();

    @Nested
    class Units {

        @Nested
        @DisplayName("Computer")
        class ComputerTests {

            @Nested
            class ExecuteInstructions {

                @Test
                void shouldExecuteSampleOperation1() {
                    var instructions = List.of(
                            "Register A: ",
                            "Register B:",
                            "Register C: 9",
                            "",
                            "Program: 2,6"

                    );
                    var computer = new Computer(instructions);

                    computer.executeProgram();

                    assertThat(computer.getValueAt(Register.B)).isEqualTo(1);
                }

                @Test
                void shouldExecuteSampleOperation2() {
                    var instructions = List.of(
                            "Register A: 10",
                            "Register B: ",
                            "Register C: ",
                            "",
                            "Program: 5,0,5,1,5,4"

                    );
                    var computer = new Computer(instructions);

                    var output = computer.executeProgram();

                    assertThat(output).isEqualTo("0,1,2");
                }

                @Test
                void shouldExecuteSampleOperation3() {
                    var instructions = List.of(
                            "Register A: 2024",
                            "Register B: ",
                            "Register C: ",
                            "",
                            "Program: 0,1,5,4,3,0"

                    );
                    var computer = new Computer(instructions);

                    var output = computer.executeProgram();

                    assertSoftly(softly -> {
                        softly.assertThat(output).isEqualTo("4,2,5,6,7,7,7,7,3,1,0");
                        softly.assertThat(computer.getValueAt(Register.A)).isEqualTo(0);
                    });
                }

                @Test
                void shouldExecuteSampleOperation4() {
                    var instructions = List.of(
                            "Register A: ",
                            "Register B: 29",
                            "Register C: ",
                            "",
                            "Program: 1,7"

                    );
                    var computer = new Computer(instructions);

                    computer.executeProgram();

                    assertThat(computer.getValueAt(Register.B)).isEqualTo(26);
                }

                @Test
                void shouldExecuteSampleOperation5() {
                    var instructions = List.of(
                            "Register A: ",
                            "Register B: 2024",
                            "Register C: 43690",
                            "",
                            "Program: 4,0"

                    );
                    var computer = new Computer(instructions);

                    computer.executeProgram();

                    assertThat(computer.getValueAt(Register.B)).isEqualTo(44_354);
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo("4,6,3,5,6,3,5,2,1,0");
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo("6,4,6,0,4,5,7,2,7");
        }
    }

    @Nested
    class Part2 {

        @Test
        @Disabled("Not solved with same algorithm")
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(117_440L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(164_541_160_582_845L);
        }
    }
}
