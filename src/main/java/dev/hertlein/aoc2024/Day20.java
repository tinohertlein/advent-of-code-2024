package dev.hertlein.aoc2024;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static java.util.function.Predicate.not;

class Day20 implements Day<Day20.MinimumNumberOfPicosecondsToSave> {

    record MinimumNumberOfPicosecondsToSave(int value) {
    }

    @Override
    public Object part1(List<String> inputLines, MinimumNumberOfPicosecondsToSave minimumNumberOfPicosecondsToSave) {
        return new Racetrack(inputLines)
                .numberOfCheatsSavingMoreThan(minimumNumberOfPicosecondsToSave.value);
    }

    @Override
    public Object part2(List<String> inputLines, MinimumNumberOfPicosecondsToSave minimumNumberOfPicosecondsToSave) {
        return new Racetrack(inputLines)
                .numberOfCheatsSavingMoreThanWithinBounds(minimumNumberOfPicosecondsToSave.value, 2, 20);
    }

    @AllArgsConstructor
    static class Racetrack {
        private final Map<Position, Character> positions;
        private final int maxX;
        private final int maxY;
        private Position start;
        @Getter
        private Position end;

        Racetrack(List<String> inputLines) {
            this.maxY = inputLines.size() - 1;
            this.maxX = inputLines.getFirst().length() - 1;
            this.positions = new TreeMap<>();

            for (int y = 0; y < inputLines.size(); y++) {
                for (int x = 0; x < inputLines.get(y).length(); x++) {
                    var charAt = inputLines.get(y).charAt(x);
                    var position = new Position(x, y);
                    positions.put(position, charAt);
                    if (charAt == 'S') {
                        start = position;
                    } else if (charAt == 'E') {
                        end = position;
                    }
                }
            }
        }

        private boolean isWithinRacetrack(Position position) {
            return position.y > 0
                    && position.y < maxY
                    && position.x > 0
                    && position.x < maxX;
        }

        private boolean isWall(Position position) {
            return positions.get(position) == '#';
        }

        private long numberOfCheatsSavingMoreThan(int picoseconds) {
            var distancesToStartPosition = distancesToStartPosition();

            long count = 0;
            for (var position : positions.keySet()) {
                if (isWall(position)) {
                    continue;
                }
                count += Stream.of(
                                new Position(position.x, position.y + 2),
                                new Position(position.x, position.y - 2),
                                new Position(position.x - 2, position.y),
                                new Position(position.x + 2, position.y)
                        )
                        .filter(this::isWithinRacetrack)
                        .filter(not(this::isWall))
                        .map(candidate -> distancesToStartPosition.get(position) - distancesToStartPosition.get(candidate))
                        .filter(savedPicoseconds -> savedPicoseconds >= (picoseconds + 2))
                        .count();
            }
            return count;
        }

        private long numberOfCheatsSavingMoreThanWithinBounds(int picoseconds, int lowerBound, int upperBound) {
            var distancesToStartPosition = distancesToStartPosition();

            long count = 0;
            for (var position : positions.keySet()) {
                if (isWall(position)) {
                    continue;
                }

                var distance = new AtomicInteger(0);
                for (int d = lowerBound; d <= upperBound; d++) {
                    for (int distanceY = 0; distanceY <= d; distanceY++) {
                        distance.set(d);
                        var distanceX = distance.get() - distanceY;

                        var candidates = new HashSet<Position>();
                        candidates.add(new Position(position.x + distanceX, position.y + distanceY));
                        candidates.add(new Position(position.x + distanceX, position.y - distanceY));
                        candidates.add(new Position(position.x - distanceX, position.y + distanceY));
                        candidates.add(new Position(position.x - distanceX, position.y - distanceY));

                        count += candidates.stream()
                                .filter(this::isWithinRacetrack)
                                .filter(not(this::isWall))
                                .map(candidate -> distancesToStartPosition.get(position) - distancesToStartPosition.get(candidate))
                                .filter(savedPicoseconds -> savedPicoseconds >= (picoseconds + distance.get()))
                                .count();
                    }
                }
            }
            return count;
        }

        Map<Position, Long> distancesToStartPosition() {
            Map<Position, Long> distanceToStart = new HashMap<>();
            var queue = new ArrayList<Position>();

            queue.add(start);
            distanceToStart.put(start, 0L);

            while (!queue.isEmpty()) {
                var current = queue.removeFirst();
                if (current.equals(end)) {
                    return distanceToStart;
                }

                var neighbours = Arrays.stream(Direction.values())
                        .map(direction -> direction.forwardFrom(current))
                        .filter(not(this::isWall))
                        .filter(not(distanceToStart::containsKey))
                        .toList();

                neighbours.forEach(neighbour -> distanceToStart.put(neighbour, distanceToStart.get(current) + 1));
                queue.addAll(neighbours);
            }
            return emptyMap();
        }
    }

    private record Position(int x, int y) implements Comparable<Position> {

        @Override
        public String toString() {
            return x + "," + y;
        }

        @Override
        public int compareTo(Position other) {
            if (this.y == other.y) {
                return this.x - other.x;
            } else {
                return this.y - other.y;
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum Direction {
        NORTH, SOUTH, WEST, EAST;

        Position forwardFrom(Position position) {
            return switch (this) {
                case NORTH -> new Position(position.x, position.y - 1);
                case SOUTH -> new Position(position.x, position.y + 1);
                case WEST -> new Position(position.x - 1, position.y);
                case EAST -> new Position(position.x + 1, position.y);
            };
        }
    }
}
