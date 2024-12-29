package dev.hertlein.aoc2024;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.*;
import static java.util.stream.Collectors.joining;

class Day23 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return Network.of(inputLines)
                .connectionsOf3()
                .stream()
                .filter(ConnectionOf3::doesAtLeast1ComputerStartWithT)
                .count();
    }

    @Override
    public String part2(List<String> inputLines, Void v) {
        return Network.of(inputLines)
                .lanParty()
                .stream()
                .sorted()
                .collect(joining(","));
    }

    record Network(Map<String, Set<String>> computersGraph) {

        static Network of(List<String> inputLines) {
            var graph = new HashMap<String, Set<String>>();

            for (var inputLine : inputLines) {
                var splitLine = inputLine.split("-");
                graph.computeIfAbsent(splitLine[0], _ -> new HashSet<>()).add(splitLine[1]);
                graph.computeIfAbsent(splitLine[1], _ -> new HashSet<>()).add(splitLine[0]);
            }
            return new Network(graph);
        }

        Set<ConnectionOf3> connectionsOf3() {
            var connectionsOf3 = new HashSet<ConnectionOf3>();

            for (String computerA : computersGraph.keySet()) {
                Set<String> connectedToA = computersGraph.get(computerA);
                for (List<String> connectionsOf2 : cartesianProduct(connectedToA, connectedToA)) {
                    String computerB = connectionsOf2.get(0);
                    String computerC = connectionsOf2.get(1);

                    if (!computerB.equals(computerC) && computersGraph.get(computerB).contains(computerC)) {
                        connectionsOf3.add(ConnectionOf3.of(computerA, computerB, computerC));
                    }
                }
            }
            return connectionsOf3;
        }

        Set<String> lanParty() {
            var lanParty = new HashSet<String>();
            // Apply https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
            bronKerbosch(new HashSet<>(), new ConcurrentSkipListSet<>(computersGraph.keySet()), new HashSet<>(), lanParty);
            return lanParty;
        }

        private void bronKerbosch(
                Set<String> r,
                Set<String> p,
                Set<String> x,
                Set<String> largestClique) {

            if (p.isEmpty() && x.isEmpty()) {
                if (r.size() > largestClique.size()) {
                    largestClique.clear();
                    largestClique.addAll(r);
                }
                return;
            }

            for (var vertex : p) {
                var neighbors = computersGraph.get(vertex);
                bronKerbosch(
                        (union(r, Set.of(vertex))),
                        new ConcurrentSkipListSet<>(intersection(p, neighbors)),
                        new HashSet<>(intersection(x, neighbors)),
                        largestClique
                );
                p.remove(vertex);
                x.add(vertex);
            }
        }
    }

    record ConnectionOf3(String computerA, String computerB, String computerC) {

        static ConnectionOf3 of(String computerA, String computerB, String computerC) {
            var list = newArrayList(computerA, computerB, computerC).stream().sorted().toList();
            return new ConnectionOf3(list.get(0), list.get(1), list.get(2));
        }

        boolean doesAtLeast1ComputerStartWithT() {
            return Stream.of(computerA, computerB, computerC).anyMatch(computer -> computer.startsWith("t"));
        }

        @Override
        public String toString() {
            return String.join(",", computerA, computerB, computerC);
        }
    }
}
