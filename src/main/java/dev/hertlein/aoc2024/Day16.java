package dev.hertlein.aoc2024;

import dev.hertlein.aoc2024.Day16.Maze.Reindeer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static dev.hertlein.aoc2024.Day16.Symbol.END;
import static dev.hertlein.aoc2024.Day16.Symbol.WALL;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

class Day16 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return new Maze(inputLines).lowestScore();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return new Maze(inputLines).numberOfTilesInBestPaths();
    }

    static class Maze {
        private final Map<Tile, Symbol> map;
        private Reindeer start;

        Maze(List<String> inputLines) {
            map = new HashMap<>();

            for (int y = 0; y < inputLines.size(); y++) {
                for (int x = 0; x < inputLines.get(y).length(); x++) {
                    var symbol = Symbol.of(inputLines.get(y).charAt(x));
                    if (symbol.isPresent()) {
                        Tile tile = new Tile(x, y);
                        if (symbol.get() == Symbol.START) {
                            start = new Reindeer(tile, Direction.EAST);
                        } else {
                            map.put(tile, symbol.get());
                        }
                    }
                }
            }
        }

        long lowestScore() {
            PriorityQueue<ReindeerAndScore> queue = new PriorityQueue<>();
            Set<Reindeer> alreadySeen = new HashSet<>();
            queue.add(new ReindeerAndScore(start, Score.empty()));

            while (!queue.isEmpty()) {
                var popped = queue.poll();
                var symbol = map.get(popped.reindeer.tile);

                if (symbol == END) {
                    return popped.scoreValue();
                }
                if (!alreadySeen.contains(Reindeer.of(popped))) {
                    if (symbol != WALL) {
                        queue.addAll(popped.next());
                    }
                    alreadySeen.add(Reindeer.of(popped));
                }
            }
            return 0;
        }

        long numberOfTilesInBestPaths() {
            PriorityQueue<ReindeerAndScore> queue = new PriorityQueue<>();
            queue.add(new ReindeerAndScore(start, Score.empty()));

            return findReindeersInBestPaths(queue)
                    .stream()
                    .map(Reindeer::tile)
                    .collect(toSet())
                    .size();
        }

        private Set<Reindeer> findReindeersInBestPaths(PriorityQueue<ReindeerAndScore> queue) {
            Set<Reindeer> ends = new HashSet<>();
            Map<Reindeer, Set<Reindeer>> backtrackMapping = new HashMap<>();
            Map<Reindeer, Integer> lowestScorePerReindeer = new HashMap<>();
            var bestScore = MAX_VALUE;

            while (!queue.isEmpty()) {
                var popped = queue.poll();
                var symbol = map.get(popped.reindeer.tile);
                var reindeer = Reindeer.of(popped);

                if (popped.score().score() <= lowestScorePerReindeer.getOrDefault(reindeer, MAX_VALUE)) {
                    if (symbol == END) {
                        if (popped.score().score() > bestScore) {
                            // All shortest paths are found, so no need to continue
                            break;
                        } else {
                            bestScore = popped.score().score();
                            backtrackMapping.get(reindeer).add(reindeer);
                            ends.add(reindeer);
                        }
                    } else {
                        popped.next().forEach(next -> {
                            if (map.get(next.reindeer.tile) != WALL) {
                                queue.add(next);

                                Reindeer nextReindeer = Reindeer.of(next);
                                var nextScore = next.score();
                                var lowestScore = lowestScorePerReindeer.getOrDefault(nextReindeer, MAX_VALUE);

                                if (nextScore.score() == lowestScore) {
                                    backtrackMapping.get(nextReindeer).add(reindeer);
                                } else if (nextScore.score() < lowestScore) {
                                    lowestScorePerReindeer.put(nextReindeer, nextScore.score());

                                    var backtrack = new HashSet<Reindeer>();
                                    backtrack.add(reindeer);
                                    backtrackMapping.put(nextReindeer, backtrack);
                                }
                            }
                        });
                    }
                }
            }
            return backtrack(ends, backtrackMapping);
        }

        private Set<Reindeer> backtrack(Set<Reindeer> ends, Map<Reindeer, Set<Reindeer>> backtracks) {
            var queue = new ArrayList<>(ends);
            var reindeers = new HashSet<Reindeer>();

            while (!queue.isEmpty()) {
                var popped = queue.removeFirst();
                for (var backtrack : backtracks.getOrDefault(popped, emptySet())) {
                    if (!reindeers.contains(backtrack)) {
                        reindeers.add(backtrack);
                        queue.add(backtrack);
                    }
                }
            }
            return reindeers;
        }

        record Reindeer(Tile tile, Direction direction) {

            static Reindeer of(ReindeerAndScore reindeerAndScore) {
                return new Reindeer(reindeerAndScore.reindeer.tile(), reindeerAndScore.reindeer.direction());
            }
        }
    }

    record ReindeerAndScore(Reindeer reindeer, Score score) implements Comparable<ReindeerAndScore> {

        @Override
        public int compareTo(ReindeerAndScore other) {
            return this.score.compareTo(other.score);
        }

        int scoreValue() {
            return score.score();
        }

        List<ReindeerAndScore> next() {
            var forward = new ReindeerAndScore(
                    new Reindeer(reindeer.direction.forwardFrom(reindeer.tile),
                    reindeer.direction),
                    score.increaseSteps()
                    );
            var left = new ReindeerAndScore(
                    new Reindeer(reindeer.tile,
                    reindeer.direction.turnLeft()),
                    score.increaseTurns()
                    );
            var right = new ReindeerAndScore(
                    new Reindeer(reindeer.tile,
                    reindeer.direction.turnRight()),
                    score.increaseTurns());

            return List.of(forward, left, right);
        }
    }

    private record Score(int steps, int turns) implements Comparable<Score> {

        static Score empty() {
            return new Score(0, 0);
        }

        int maxScore() {
            return Integer.MAX_VALUE;
        }

        int score() {
            return steps + turns * 1000;
        }

        Score increaseSteps() {
            return new Score(steps + 1, turns);
        }

        Score increaseTurns() {
            return new Score(steps, turns + 1);
        }

        @Override
        public int compareTo(Score o) {
            return this.score() - o.score();
        }
    }

    @RequiredArgsConstructor
    @Getter
    enum Symbol {
        START('S'), END('E'), WALL('#');

        private final char identifier;

        private static final Map<Character, Symbol> IDENTIFIERS = stream(Symbol.values())
                .collect(toMap(Symbol::getIdentifier, identity()));

        static Optional<Symbol> of(char value) {
            return Optional.ofNullable(IDENTIFIERS.get(value));
        }
    }

    record Tile(int x, int y) {
    }

    @Getter
    @RequiredArgsConstructor
    private enum Direction {
        NORTH(), SOUTH(), WEST(), EAST();

        Direction turnRight() {
            return switch (this) {
                case NORTH -> Direction.EAST;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.NORTH;
                case EAST -> Direction.SOUTH;
            };
        }

        Direction turnLeft() {
            return switch (this) {
                case NORTH -> Direction.WEST;
                case SOUTH -> Direction.EAST;
                case WEST -> Direction.SOUTH;
                case EAST -> Direction.NORTH;
            };
        }

        Tile forwardFrom(Tile tile) {
            return switch (this) {
                case NORTH -> new Tile(tile.x, tile.y - 1);
                case SOUTH -> new Tile(tile.x, tile.y + 1);
                case WEST -> new Tile(tile.x - 1, tile.y);
                case EAST -> new Tile(tile.x + 1, tile.y);
            };
        }
    }
}