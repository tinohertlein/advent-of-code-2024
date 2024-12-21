package dev.hertlein.aoc2024;

import com.google.common.base.Joiner;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Gatherers;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

class Day21 implements Day<Void> {

    @Override
    public Object part1(List<String> codes, Void v) {
        final int ROBOTS_NUMERICAL = 1;
        final int ROBOTS_DIRECTIONAL = 2;
        return new Starship(ROBOTS_NUMERICAL + ROBOTS_DIRECTIONAL).complexitiesOf(codes);
    }

    @Override
    public Object part2(List<String> codes, Void v) {
        final int ROBOTS_NUMERICAL = 1;
        final int ROBOTS_DIRECTIONAL = 25;
        return new Starship(ROBOTS_NUMERICAL + ROBOTS_DIRECTIONAL).complexitiesOf(codes);
    }

    static class Starship {
        private static final Pattern CODE_PATTERN = Pattern.compile("(\\d{3})");

        private final LoadingCache<Pair<String, Integer>, Long> lengthOfShortestSequenceCache =
                CacheBuilder.newBuilder().build(new CacheLoader<>() {
                    @Override
                    @NonNull
                    public Long load(@NonNull Pair<String, Integer> pair) {
                        return lengthOfShortestSequencesOnDirectionalKeypadFor(pair.getKey(), pair.getValue());
                    }
                });

        private final int numberOfRobots;
        private final Map<CharacterFromTo, List<String>> shortestNumericalSeqs;
        private final Map<CharacterFromTo, List<String>> shortestDirectionalSeqs;

        Starship(int numberOfRobots) {
            this.numberOfRobots = numberOfRobots;
            this.shortestNumericalSeqs = shortestSequencesOn(new NumericalKeypad());
            this.shortestDirectionalSeqs = shortestSequencesOn(new DirectionalKeypad());
        }

        private Map<CharacterFromTo, List<String>> shortestSequencesOn(Keypad keypad) {
            return keypad
                    .allCombinationsOfFromTo()
                    .stream()
                    .collect(toMap(
                            fromTo -> {
                                var fromChar = keypad.charAtPosition(fromTo.from);
                                var toChar = keypad.charAtPosition(fromTo.to);
                                return new CharacterFromTo(fromChar, toChar);
                            },
                            fromTo -> fromTo.isDestinationReached() ? List.of("A") : shortestSequencesOn(keypad, fromTo)));
        }

        private List<String> shortestSequencesOn(Keypad keypad, PositionFromTo fromTo) {
            var shortestLength = Integer.MAX_VALUE;
            var shortestSequences = new ArrayList<String>();
            var queue = new ArrayList<MovesToPosition>();
            queue.add(new MovesToPosition(fromTo.from, ""));

            while (!queue.isEmpty()) {
                var current = queue.removeFirst();
                var from = current.position;
                var moves = current.moves;

                if (moves.length() > shortestLength) {
                    break;
                }

                for (var direction : Direction.values()) {
                    var to = direction.move(from);
                    if (!keypad.contains(to)) {
                        continue;
                    }
                    if (fromTo.toIsEqual(to)) {
                        shortestLength = moves.length() + 1;
                        shortestSequences.add(moves + direction.identifier + 'A');
                    } else {
                        queue.add(new MovesToPosition(to, moves + direction.identifier));
                    }
                }
            }
            return shortestSequences;
        }

        long complexitiesOf(List<String> codes) {
            return codes.stream().mapToLong(this::complexityOf).sum();
        }

        long complexityOf(String code) {
            var matcher = CODE_PATTERN.matcher(code);

            if (matcher.find()) {
                return parseLong(matcher.group(1)) * lengthOfShortestSequenceFor(code);
            } else {
                throw new IllegalArgumentException("No code found in '%s'".formatted(code));
            }
        }

        long lengthOfShortestSequenceFor(String code) {
            return shortestSequencesOnNumericalKeypadFor("A" + code)
                    .stream()
                    .mapToLong(seq -> lengthOfShortestSequencesOnDirectionalKeypadFor("A" + seq, numberOfRobots - 1))
                    .min()
                    .orElseThrow();
        }

        private List<String> shortestSequencesOnNumericalKeypadFor(String code) {
            return Lists.cartesianProduct(
                            code
                                    .chars()
                                    .mapToObj(c -> (char) c)
                                    .gather(Gatherers.windowSliding(2))
                                    .map(fromTo -> shortestNumericalSeqs.get(new CharacterFromTo(fromTo.getFirst(), fromTo.getLast())))
                                    .toList())
                    .stream()
                    .map(option -> Joiner.on("").join(option))
                    .toList();
        }

        private long lengthOfShortestSequencesOnDirectionalKeypadFor(String code, int remainingRobots) {
            var fromTos = code.chars()
                    .mapToObj(c -> (char) c)
                    .gather(Gatherers.windowSliding(2))
                    .toList();

            long sumOfFromToLengths = 0;
            for (var fromTo : fromTos) {
                var sequences = shortestDirectionalSeqs.get(new CharacterFromTo(fromTo.getFirst(), fromTo.getLast()));
                if (remainingRobots == 1) {
                    sumOfFromToLengths += sequences.stream()
                            .mapToInt(String::length)
                            .min()
                            .orElseThrow();
                } else {
                    sumOfFromToLengths += sequences.stream()
                            .mapToLong(seq -> getFromCacheOrPutIntoCache("A" + seq, remainingRobots - 1))
                            .min()
                            .orElseThrow();
                }
            }
            return sumOfFromToLengths;
        }

        @SneakyThrows
        private Long getFromCacheOrPutIntoCache(String sequence, int remainingRobots) {
            return lengthOfShortestSequenceCache.get(Pair.of(sequence, remainingRobots));
        }
    }

    @RequiredArgsConstructor
    static class Keypad {
        private final Map<Position, Character> map;

        boolean contains(Position position) {
            return map.containsKey(position);
        }

        char charAtPosition(Position position) {
            return map.get(position);
        }

        Set<PositionFromTo> allCombinationsOfFromTo() {
            return Sets.cartesianProduct(map.keySet(), map.keySet())
                    .stream()
                    .map(fromTo -> new PositionFromTo(fromTo.getFirst(), fromTo.getLast()))
                    .collect(toSet());
        }
    }

    static class NumericalKeypad extends Keypad {

        public NumericalKeypad() {
            super(createMap());
        }

        private static Map<Position, Character> createMap() {
            return new ImmutableMap.Builder<Position, Character>()
                    .put(new Position(0, 0), '7')
                    .put(new Position(1, 0), '8')
                    .put(new Position(2, 0), '9')

                    .put(new Position(0, 1), '4')
                    .put(new Position(1, 1), '5')
                    .put(new Position(2, 1), '6')

                    .put(new Position(0, 2), '1')
                    .put(new Position(1, 2), '2')
                    .put(new Position(2, 2), '3')

                    .put(new Position(1, 3), '0')
                    .put(new Position(2, 3), 'A')

                    .build();
        }
    }

    static class DirectionalKeypad extends Keypad {

        DirectionalKeypad() {
            super(createMap());
        }

        private static Map<Position, Character> createMap() {
            return new ImmutableMap.Builder<Position, Character>()
                    .put(new Position(1, 0), '^')
                    .put(new Position(2, 0), 'A')

                    .put(new Position(0, 1), '<')
                    .put(new Position(1, 1), 'v')
                    .put(new Position(2, 1), '>')

                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum Direction {
        UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');

        private final char identifier;

        Position move(Position from) {
            return switch (this) {
                case UP -> new Position(from.x, from.y - 1);
                case DOWN -> new Position(from.x, from.y + 1);
                case LEFT -> new Position(from.x - 1, from.y);
                case RIGHT -> new Position(from.x + 1, from.y);
            };
        }
    }

    record MovesToPosition(Position position, String moves) {
    }

    record PositionFromTo(Position from, Position to) {

        boolean isDestinationReached() {
            return from.equals(to);
        }

        boolean toIsEqual(Position to) {
            return this.to.equals(to);
        }
    }

    record CharacterFromTo(char from, char to) {
    }

    record Position(int x, int y) {
    }
}
