package dev.hertlein.aoc2024;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.hertlein.aoc2024.Day.EMPTY;
import static dev.hertlein.aoc2024.Day23.ConnectionOf3;
import static dev.hertlein.aoc2024.Day23.Network;
import static dev.hertlein.aoc2024.lib.InputReader.readChallengeInputFor;
import static dev.hertlein.aoc2024.lib.InputReader.readSampleInputFor;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day23")
class Day23Test {

    private final Day23 day = new Day23();

    @Nested
    class Units {

        @Nested
        @DisplayName("Network")
        class NetworkTests {

            @Nested
            class ConnectionsOf3 {

                @Test
                void shouldReturnConnections() {
                    var connections = Network.of(readSampleInputFor(day)).connectionsOf3();

                    assertThat(connections).containsExactlyInAnyOrder(
                            new ConnectionOf3("aq", "cg", "yn"),
                            new ConnectionOf3("aq", "vc", "wq"),
                            new ConnectionOf3("co", "de", "ka"),
                            new ConnectionOf3("co", "de", "ta"),
                            new ConnectionOf3("co", "ka", "ta"),
                            new ConnectionOf3("de", "ka", "ta"),
                            new ConnectionOf3("kh", "qp", "ub"),
                            new ConnectionOf3("qp", "td", "wh"),
                            new ConnectionOf3("tb", "vc", "wq"),
                            new ConnectionOf3("tc", "td", "wh"),
                            new ConnectionOf3("td", "wh", "yn"),
                            new ConnectionOf3("ub", "vc", "wq"));
                }
            }
        }
    }

    @Nested
    class Part1 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part1(readSampleInputFor(day), EMPTY)).isEqualTo(7L);
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part1(readChallengeInputFor(day), EMPTY)).isEqualTo(1_599L);
        }
    }

    @Nested
    class Part2 {

        @Test
        void sampleShouldBeSolved() {
            assertThat(day.part2(readSampleInputFor(day), EMPTY)).isEqualTo("co,de,ka,ta");
        }

        @ChallengeTest
        void challengeShouldBeSolved() {
            assertThat(day.part2(readChallengeInputFor(day), EMPTY)).isEqualTo("av,ax,dg,di,dw,fa,ge,kh,ki,ot,qw,vz,yw");
        }
    }
}
