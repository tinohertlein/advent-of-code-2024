package dev.hertlein.aoc2024;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Day02 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return Reports.of(inputLines).countSafeReportsStrictly();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return Reports.of(inputLines).countSafeReportsDampened();
    }

    record Reports(List<Report> reports) {

        static Reports of(List<String> inputLines) {
            var reports = inputLines.stream()
                    .map(Report::of)
                    .toList();
            return new Reports(reports);
        }

        long countSafeReportsStrictly() {
            return reports.stream().filter(Report::isSafeStrictly).count();
        }

        long countSafeReportsDampened() {
            return reports.stream().filter(Report::isSafeDampened).count();
        }
    }

    record Report(List<Integer> levels) {

        static Report of(String inputLine) {
            var levels = Arrays.stream(inputLine.split("\\s"))
                    .map(Integer::parseInt)
                    .toList();
            return new Report(levels);
        }

        boolean isSafeStrictly() {
            return this.isSafe();
        }

        boolean isSafeDampened() {
            return permutations().anyMatch(Report::isSafeStrictly);
        }

        private boolean isSafe() {
            var levelDifferences = levels.stream()
                    .gather(Gatherers.windowSliding(2))
                    .map(window -> window.getFirst() - window.getLast())
                    .collect(Collectors.toSet());

            return Set.of(1, 2, 3).containsAll(levelDifferences) || Set.of(-1, -2, -3).containsAll(levelDifferences);
        }

        private Stream<Report> permutations() {
            var permutations = IntStream.range(0, levels.size())
                    .mapToObj(index -> {
                        var permutationLevels = Lists.newArrayList(this.levels);
                        permutationLevels.remove(index);
                        return new Report(permutationLevels);
                    });
            return Stream.concat(Stream.of(this), permutations);
        }
    }
}
