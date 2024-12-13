package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day13.*;
import static dev.hertlein.aoc2024.Day13.ButtonType.A;
import static dev.hertlein.aoc2024.Day13.ButtonType.B;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("Day13")
class Day13Test {

    private final Day13 day = new Day13();

    @Nested
    class Units {

        private final static List<String> SAMPLE_INPUT_1 = List.of(
                "Button A: X+94, Y+34",
                "Button B: X+22, Y+67",
                "Prize: X=8400, Y=5400"
        );

        @Nested
        @DisplayName("ClawMachine")
        class ClawMachineTests {

            @Nested
            class Of {

                @Test
                void shouldCreateClawMachine() {
                    var machine = ClawMachine.of(SAMPLE_INPUT_1, 0L);

                    assertSoftly(softly -> {
                        softly.assertThat(machine.prize()).isEqualTo(new Prize(8400, 5400));
                        softly.assertThat(machine.buttonA()).isEqualTo(new Button(A, 94, 34));
                        softly.assertThat(machine.buttonB()).isEqualTo(new Button(B, 22, 67));
                    });
                }
            }

            @Nested
            class WinPrize {

                @Test
                void shouldReturnButtonPushesIfPrizeIsWon() {
                    var machine = ClawMachine.of(SAMPLE_INPUT_1, 0L);

                    var pushes = machine.tryWinPrize();

                    assertThat(pushes.values()).containsExactlyInAnyOrder(
                            new PushesOfButton(machine.buttonA(), 80),
                            new PushesOfButton(machine.buttonB(), 40));
                }

                @Test
                void shouldReturnNoPushesIfPrizeIsNotWon() {
                    List<String> input = List.of(
                            "Button A: X+26, Y+66",
                            "Button B: X+67, Y+21",
                            "Prize: X=12748, Y=12176"
                    );
                    var machine = ClawMachine.of(input, 0L);

                    assertThat(machine.tryWinPrize()).isEqualTo(PushesOfButtons.NONE);
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(480L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(37_128L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(875_318_608_908L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(74_914_228_471_331L);
        }
    }
}
