package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day14.AreaSize;
import dev.hertlein.aoc2024.Day14.Position;
import dev.hertlein.aoc2024.Day14.Robot;
import dev.hertlein.aoc2024.Day14.Velocity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day14.Robots;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("Day14")
class Day14Test {

    final static AreaSize ADDITIONAL_INPUT_SAMPLE = new AreaSize(11, 7);
    final static AreaSize ADDITIONAL_INPUT_CHALLENGE = new AreaSize(101, 103);

    private final Day14 day = new Day14();

    @Nested
    class Units {

        private final static List<String> SIMPLE_INPUT = List.of(
                "p=2,4 v=2,-3");

        @Nested
        @DisplayName("Robots")
        class RobotsTests {

            @Nested
            class New {

                @Test
                void shouldCreateRobots() {
                    var robots = new Robots(SIMPLE_INPUT);

                    assertSoftly(softly -> {
                        softly.assertThat(robots.getRobots().getFirst().getVelocity()).isEqualTo(new Velocity(2, -3));
                        softly.assertThat(robots.getRobots().getFirst().getCurrentPosition().x()).isEqualTo(2);
                        softly.assertThat(robots.getRobots().getFirst().getCurrentPosition().y()).isEqualTo(4);
                    });
                }
            }

            @Nested
            class SizeOfLargestAreaWithConnectedRobots {

                @Test
                void shouldReturnSize() {
                    var robots = new Robots(
                            new Robot(new Position(0, 0)),
                            new Robot(new Position(0, 1)),
                            new Robot(new Position(1, 1)),
                            new Robot(new Position(1, 0)),
                            new Robot(new Position(3, 3)),
                            new Robot(new Position(3, 4))
                    );

                    assertThat(robots.sizeOfLargestAreaWithConnectedRobots()).isEqualTo(4);
                }
            }
        }

        @Nested
        @DisplayName("Robot")
        class RobotTests {

            @Nested
            class Navigate {

                @Test
                void shouldBeAtFinalPositionAfter5Seconds() {
                    var robot = new Robots(SIMPLE_INPUT).getRobots().getFirst();

                    robot.navigate(new AreaSize(11, 7), 5);

                    assertSoftly(softly -> {
                        softly.assertThat(robot.getCurrentPosition().x()).isEqualTo(1);
                        softly.assertThat(robot.getCurrentPosition().y()).isEqualTo(3);
                    });
                }

                @Test
                void shouldBeAtFinalPositionAfter100Seconds() {
                    var robot = new Robots(SIMPLE_INPUT).getRobots().getFirst();

                    robot.navigate(new AreaSize(11, 7), 100);

                    assertSoftly(softly -> {
                        softly.assertThat(robot.getCurrentPosition().x()).isEqualTo(4);
                        softly.assertThat(robot.getCurrentPosition().y()).isEqualTo(5);
                    });
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), ADDITIONAL_INPUT_SAMPLE)).isEqualTo(12L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), ADDITIONAL_INPUT_CHALLENGE)).isEqualTo(219_150_360L);
        }
    }

    @Nested
    class Part2 {

        @Test
        @Disabled("No sample for part 2 provided")
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), ADDITIONAL_INPUT_SAMPLE)).isEqualTo(0L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), ADDITIONAL_INPUT_CHALLENGE)).isEqualTo(8_053L);
        }
    }
}
