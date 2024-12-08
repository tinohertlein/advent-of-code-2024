package dev.hertlein.aoc2024;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day08 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        var map = AntennaMap.of(inputLines);

        return map.findAntinodes(map::findSimple)
                .distinct()
                .count();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        var map = AntennaMap.of(inputLines);

        return map.findAntinodes(map::findResonantHarmonic)
                .distinct()
                .count();
    }

    record AntennaMap(
            int maxX,
            int maxY,
            List<Antenna> antennas) {

        static AntennaMap of(List<String> inputLines) {
            var antennas = new ArrayList<Antenna>();

            for (int y = 0; y < inputLines.size(); y++) {
                for (int x = 0; x < inputLines.get(y).length(); x++) {

                    var symbol = inputLines.get(y).charAt(x);
                    if (Antenna.isAntenna(symbol)) {
                        antennas.add(new Antenna(new Coordinate(x, y), symbol));
                    }
                }
            }
            return new AntennaMap(inputLines.getFirst().length(), inputLines.size(), antennas);
        }

        private boolean isWithinMap(Antinode antinode) {
            return antinode.coordinate.x >= 0 &&
                    antinode.coordinate.x < maxX &&
                    antinode.coordinate.y >= 0 &&
                    antinode.coordinate.y < maxY;
        }

        Stream<Antinode> findAntinodes(AntinodeFinder finder) {
            return antennas.stream()
                    .collect(Collectors.groupingBy(antenna -> antenna.frequency))
                    .values()
                    .stream()
                    .map(antennasOfSameFrequency ->
                            findAntinodesForAntennasWithSameFrequency(
                                    finder,
                                    new HashSet<>(),
                                    antennasOfSameFrequency
                            ))
                    .flatMap(Collection::stream);
        }

        private Set<Antinode> findAntinodesForAntennasWithSameFrequency(AntinodeFinder finder,
                                                                        Set<Antinode> antinodes,
                                                                        List<Antenna> remainingAntennas) {
            if (remainingAntennas.isEmpty()) {
                return antinodes;
            }

            var thisAntenna = remainingAntennas.getFirst();
            List<Antenna> otherAntennas = remainingAntennas.subList(1, remainingAntennas.size());

            Set<Antinode> newAntinodes = otherAntennas.stream()
                    .flatMap(otherAntenna -> finder.find(thisAntenna, otherAntenna))
                    .filter(this::isWithinMap)
                    .collect(Collectors.toSet());

            antinodes.addAll(newAntinodes);

            return findAntinodesForAntennasWithSameFrequency(
                    finder,
                    antinodes,
                    otherAntennas
            );
        }

        Stream<Antinode> findSimple(Antenna thisAntenna, Antenna otherAntenna) {
            var distance1 = thisAntenna.distanceTo(otherAntenna);
            var distance2 = otherAntenna.distanceTo(thisAntenna);
            return Stream.of(
                    otherAntenna.plus(distance1.times2()),
                    thisAntenna.plus(distance2.times2()));
        }

        Stream<Antinode> findResonantHarmonic(Antenna thisAntenna, Antenna otherAntenna) {
            Set<Antinode> antinodes = new HashSet<>();
            var distance1 = thisAntenna.distanceTo(otherAntenna);
            var distance2 = otherAntenna.distanceTo(thisAntenna);

            var antinode1 = otherAntenna.plus(distance1);
            var antinode2 = thisAntenna.plus(distance2);

            while (isWithinMap(antinode1)) {
                antinodes.add(antinode1);
                antinode1 = antinode1.plus(distance1);
            }

            while (isWithinMap(antinode2)) {
                antinodes.add(antinode2);
                antinode2 = antinode2.plus(distance2);
            }
            return antinodes.stream();
        }
    }

    @FunctionalInterface
    interface AntinodeFinder {

        Stream<Antinode> find(Antenna thisAntenna, Antenna otherAntenna);
    }

    record Antinode(Coordinate coordinate) {

        static Antinode of(int x, int y) {
            return new Antinode(new Coordinate(x, y));
        }

        Antinode plus(Vector vector) {
            return new Antinode(coordinate.plus(vector));
        }
    }

    record Antenna(Coordinate coordinate, char frequency) {

        static boolean isAntenna(char c) {
            return c != '.' && c != '#';
        }

        Vector distanceTo(Antenna other) {
            return new Vector(
                    this.coordinate.x - other.coordinate.x,
                    this.coordinate.y - other.coordinate.y);
        }

        Antinode plus(Vector vector) {
            return new Antinode(coordinate.plus(vector));
        }
    }

    record Vector(int x, int y) {

        Vector times2() {
            return new Vector(x * 2, y * 2);
        }
    }

    record Coordinate(int x, int y) {

        Coordinate plus(Vector vector) {
            return new Coordinate(this.x + vector.x, this.y + vector.y);
        }
    }
}
