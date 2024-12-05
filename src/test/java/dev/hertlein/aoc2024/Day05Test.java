package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.Day05.PrintQueue;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day05")
class Day05Test {

    private final Day05 day = new Day05();

    @Nested
    class Units {

        @Nested
        @DisplayName("PageUpdate")
        class PageUpdateTests {

            private final PrintQueue printQueue = new PrintQueue(readSampleInputFor(day));

            private PrintQueue.PageUpdate pageUpdateAt(int index) {
                return printQueue.updates.values.get(index);
            }

            @Nested
            class PageInTheMiddle {

                @ParameterizedTest
                @CsvSource({
                        "0,61",
                        "1,53",
                        "2,29"
                })
                void shouldReturnPageNumberInTheMiddle(int index, int expected) {
                    assertThat(pageUpdateAt(index).pageInTheMiddle()).isEqualTo(new PrintQueue.Page(expected));
                }
            }

            @Nested
            class IsOrderedCorrectly {

                @ParameterizedTest
                @CsvSource({
                        "0,true",
                        "1,true",
                        "2,true",
                        "3,false",
                        "4,false",
                        "5,false"})
                void shouldIdentifyIfPageUpdateIsOrderedCorrectly(int index, boolean expected) {
                    assertThat(pageUpdateAt(index).isOrderedCorrectly()).isEqualTo(expected);
                }
            }

            @Nested
            class FixOrder {

                @ParameterizedTest
                @ValueSource(ints = {3, 4, 5})
                void shouldFixOrderOfMisorderedOnes(int index) {
                    assertThat(pageUpdateAt(index).fixOrder().isOrderedCorrectly()).isTrue();
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(143L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(5_064L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(123L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(5_152L);
        }
    }
}
