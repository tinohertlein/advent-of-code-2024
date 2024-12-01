package dev.hertlein.aoc2024;

import com.google.common.collect.Streams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Day01 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return LocationLists.of(inputLines).totalDistance();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return LocationLists.of(inputLines).similarityScore();
    }

    record LocationLists(List<Integer> leftList, List<Integer> rightList) {

        static LocationLists of(List<String> inputLines) {
            return inputLines.stream()
                    .map(LocationPair::of)
                    .reduce(empty(), LocationLists::add, LocationLists::addAll)
                    .immutable();
        }

        private static LocationLists empty() {
            return new LocationLists(new ArrayList<>(), new ArrayList<>());
        }

        private LocationLists immutable() {
            return new LocationLists(List.copyOf(leftList), List.copyOf(rightList));
        }

        private LocationLists add(LocationPair locationPair) {
            this.leftList.add((locationPair.left));
            this.rightList.add((locationPair.right));
            return this;
        }

        private LocationLists addAll(LocationLists other) {
            this.leftList.addAll(other.leftList);
            this.rightList.addAll(other.rightList);
            return this;
        }

        long totalDistance() {
            return Streams.zip(leftList.stream().sorted(),
                            rightList.stream().sorted(),
                            LocationPair::new)
                    .mapToInt(LocationPair::distance)
                    .sum();
        }

        long similarityScore() {
            var occurrences = rightList
                    .stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            return leftList
                    .stream()
                    .mapToLong(location -> location * occurrences.getOrDefault(location, 0L))
                    .sum();
        }
    }

    record LocationPair(int left, int right) {

        private static final Pattern REGEX = Pattern.compile("(\\d+)\\s+(\\d+)");

        static LocationPair of(String line) {
            var matcher = REGEX.matcher(line);

            if (matcher.find()) {
                var left = matcher.group(1);
                var right = matcher.group(2);
                return new LocationPair(Integer.parseInt(left), Integer.parseInt(right));
            } else {
                throw new IllegalArgumentException("No locations found in '%s'".formatted(line));
            }
        }

        int distance() {
            return Math.abs(left - right);
        }
    }
}