package dev.hertlein.aoc2024;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

class Day19 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return new Towels(inputLines).numberOfPossibleDesigns();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return new Towels(inputLines).numberOfPossibleArrangements();
    }

    @Getter
    static class Towels {
        private final Set<String> patterns;
        private final List<String> designs;

        private final LoadingCache<String, Long> possibleArrangementsCache =
                CacheBuilder.newBuilder().build(new CacheLoader<>() {
                    @Override
                    @NonNull
                    public Long load(@NonNull String design) {
                        return possibleArrangementsFor(design);
                    }
                });

        Towels(List<String> inputLines) {
            patterns = Arrays.stream(inputLines.getFirst().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());

            designs = inputLines
                    .stream()
                    .dropWhile(not(String::isEmpty))
                    .dropWhile(String::isEmpty)
                    .toList();
        }

        long numberOfPossibleDesigns() {
            return designs.stream().filter(this::isPossible).count();
        }

        long numberOfPossibleArrangements() {
            return designs.stream().mapToLong(this::possibleArrangementsFor).sum();
        }

        boolean isPossible(String design) {
            if (design.isEmpty()) {
                return true;
            } else {
                for (int i = 1; i <= design.length(); i++) {
                    String prefix = design.substring(0, i);
                    String rest = design.substring(i);
                    if (patterns.contains(prefix) && isPossible(rest)) {
                        return true;
                    }
                }
                return false;
            }
        }

        @SneakyThrows
        long possibleArrangementsFor(String design) {
            if (design.isEmpty()) {
                return 1L;
            } else {
                var count = 0L;
                for (int i = 1; i <= design.length(); i++) {
                    String prefix = design.substring(0, i);
                    String rest = design.substring(i);
                    if (patterns.contains(prefix)) {
                        count += possibleArrangementsCache.get(rest);
                    }
                }
                return count;
            }
        }
    }
}
