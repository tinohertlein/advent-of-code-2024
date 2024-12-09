package dev.hertlein.aoc2024;


import dev.hertlein.aoc2024.Day09.Disk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day09")
class Day09Test {

    private final Day09 day = new Day09();

    @Nested
    class Units {

        @Nested
        @DisplayName("Disk")
        class DiskTests {

            @Nested
            class Of {

                @Test
                void shouldCreateDiskFromEvenSample() {
                    var disk = Disk.of("1234");

                    assertThat(disk.toString()).isEqualTo("0..111....");
                }

                @Test
                void shouldCreateDiskFromOddSample() {
                    var disk = Disk.of("12345");

                    assertThat(disk.toString()).isEqualTo("0..111....22222");
                }

                @Test
                void shouldCreateDiskFromSampleInput() {
                    var disk = Disk.of(readSampleInputFor(day).getFirst());

                    assertThat(disk.toString()).isEqualTo("00...111...2...333.44.5555.6666.777.888899");
                }

                @ChallengeTest
                void shouldCreateDiskFromChallengeInput() {
                    var disk = Disk.of(readChallengeInputFor(day).getFirst());

                    assertThat(disk.toString()).isNotEmpty();
                }
            }

            @Nested
            class CompactByMovingDataBlocks {

                @Test
                void shouldCompactDiskFromSimpleSample() {
                    var disk = Disk.of("12345");

                    assertThat(disk.compactByMovingDataBlocks().toString())
                            .isEqualTo("022111222......");
                }

                @Test
                void shouldCompactDiskFromSampleInput() {
                    var disk = Disk.of(readSampleInputFor(day).getFirst());

                    assertThat(disk.compactByMovingDataBlocks().toString())
                            .isEqualTo("0099811188827773336446555566..............");
                }
            }

            @Nested
            class CompactByMovingFiles {

                @Test
                void shouldCompactDiskFromSampleInput() {
                    var disk = Disk.of(readSampleInputFor(day).getFirst());

                    assertThat(disk.compactByMovingFiles().toString())
                            .isEqualTo("00992111777.44.333....5555.6666.....8888..");
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(1_928L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(6_344_673_854_800L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo(2_858L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo(6_360_363_199_987L);
        }
    }
}
