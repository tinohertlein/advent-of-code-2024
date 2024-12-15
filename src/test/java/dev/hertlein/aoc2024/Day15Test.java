package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day15.Warehouse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.Day15.Movement;
import static dev.hertlein.aoc2024.Day15.Movements;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day15")
class Day15Test {

    private final Day15 day = new Day15();

    private final static List<String> SMALL_SAMPLE = List.of(
            "########",
            "#..O.O.#",
            "##@.O..#",
            "#...O..#",
            "#.#.O..#",
            "#...O..#",
            "#......#",
            "########",
            "",
            "<^^>>>vv<v>>v<<"
    );

    @Nested
    class Units {

        @Nested
        @DisplayName("Warehouse")
        class WarehouseTests {

            @Nested
            class New {

                @Test
                void shouldCreateWarehouse() {
                    var expected = """
                            ########
                            #..O.O.#
                            ##@.O..#
                            #...O..#
                            #.#.O..#
                            #...O..#
                            #......#
                            ########
                            """;

                    var warehouse = new Warehouse(SMALL_SAMPLE.subList(0, 8));

                    assertThat(warehouse.toString()).isEqualTo(expected);
                }
            }

            @Nested
            class ScaleUp {

                @Test
                void shouldScaleUpWarehouse() {
                    var expected = """
                            ####################
                            ##....[]....[]..[]##
                            ##............[]..##
                            ##..[][]....[]..[]##
                            ##....[]@.....[]..##
                            ##[]##....[]......##
                            ##[]....[]....[]..##
                            ##..[][]..[]..[][]##
                            ##........[]......##
                            ####################
                            """;

                    var warehouse = new Warehouse(readSampleInputFor(day).subList(0, 10)).scaleUp();

                    assertThat(warehouse.toString()).isEqualTo(expected);
                }
            }
        }

        @Nested
        @DisplayName("Movements")
        class MovementsTests {

            @Nested
            class Of {

                @Test
                void shouldCreateMovements() {
                    var movements = Movements.of(SMALL_SAMPLE.subList(9, SMALL_SAMPLE.size()));

                    assertThat(movements.values()).hasSize(15);
                }
            }
        }

        @Nested
        @DisplayName("Robot")
        class RobotTests {

            @Nested
            class Move {

                @Test
                void shouldMoveCorrectly1() {
                    var warehouse = new Warehouse(List.of(
                            "##############",
                            "##......##..##",
                            "##..........##",
                            "##....[][]@.##",
                            "##....[]....##",
                            "##..........##",
                            "##############"
                    ));

                    warehouse.getRobot().move(Movement.LEFT);

                    assertThat(warehouse.toString()).isEqualTo("""
                            ##############
                            ##......##..##
                            ##..........##
                            ##...[][]@..##
                            ##....[]....##
                            ##..........##
                            ##############
                            """);
                }

                @Test
                void shouldMoveCorrectly2() {
                    var warehouse = new Warehouse(List.of(
                            "##############",
                            "##......##..##",
                            "##..........##",
                            "##...[][]...##",
                            "##....[]....##",
                            "##.....@....##",
                            "##############"
                    ));

                    warehouse.getRobot().move(Movement.UP);

                    assertThat(warehouse.toString()).isEqualTo("""
                            ##############
                            ##......##..##
                            ##...[][]...##
                            ##....[]....##
                            ##.....@....##
                            ##..........##
                            ##############
                            """);
                }

                @Test
                void shouldMoveCorrectly3() {
                    var warehouse = new Warehouse(List.of(
                            "##############",
                            "##......##..##",
                            "##...[][]...##",
                            "##....[]....##",
                            "##.....@....##",
                            "##..........##",
                            "##############"
                    ));

                    warehouse.getRobot().move(Movement.UP);

                    assertThat(warehouse.toString()).isEqualTo(
                            """
                                    ##############
                                    ##......##..##
                                    ##...[][]...##
                                    ##....[]....##
                                    ##.....@....##
                                    ##..........##
                                    ##############
                                    """);
                }

                @Test
                void shouldMoveCorrectly4() {
                    var warehouse = new Warehouse(List.of(
                            "##############",
                            "##......##..##",
                            "##...[][]...##",
                            "##...@[]....##",
                            "##..........##",
                            "##..........##",
                            "##############"
                    ));

                    warehouse.getRobot().move(Movement.UP);

                    assertThat(warehouse.toString()).isEqualTo("""
                            ##############
                            ##...[].##..##
                            ##...@.[]...##
                            ##....[]....##
                            ##..........##
                            ##..........##
                            ##############
                            """);
                }

                @Test
                void shouldMoveCorrectly5() {
                    var warehouse = new Warehouse(List.of(
                            "####################",
                            "##[]..[]....[]..[]##",
                            "##[]..........[]..##",
                            "##.........@[][][]##",
                            "##....[]..[]..[]..##",
                            "##..##....[]......##",
                            "##...[]...[]..[]..##",
                            "##.....[]..[].[][]##",
                            "##........[]......##",
                            "####################"
                    ));

                    warehouse.getRobot().move(Movement.DOWN);

                    assertThat(warehouse.toString()).isEqualTo("""
                            ####################
                            ##[]..[]....[]..[]##
                            ##[]..........[]..##
                            ##.........@[][][]##
                            ##....[]..[]..[]..##
                            ##..##....[]......##
                            ##...[]...[]..[]..##
                            ##.....[]..[].[][]##
                            ##........[]......##
                            ####################
                            """);
                }
            }

            @Nested
            class ScaleUp {

                @Test
                void shouldScaleUpWarehouse() {
                    var expected = """
                            ####################
                            ##....[]....[]..[]##
                            ##............[]..##
                            ##..[][]....[]..[]##
                            ##....[]@.....[]..##
                            ##[]##....[]......##
                            ##[]....[]....[]..##
                            ##..[][]..[]..[][]##
                            ##........[]......##
                            ####################
                            """;

                    var warehouse = new Warehouse(readSampleInputFor(day).subList(0, 10)).scaleUp();

                    assertThat(warehouse.toString()).isEqualTo(expected);
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void smallSampleShouldBeSolved() {
            assertThat(day.part1(SMALL_SAMPLE, EMPTY)).isEqualTo(2_028L);
        }

        @Test
        void largeSampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(10_092L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(1_568_399L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void largeSampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(9_021L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(1_575_877L);
        }
    }
}
