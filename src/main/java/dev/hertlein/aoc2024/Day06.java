package dev.hertlein.aoc2024;

import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day06 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return new Lab(inputLines).patrolPath().length();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        Lab initialLab = new Lab(inputLines);

        return initialLab.patrolPath().stream().parallel()
                .map(initialLab::newLabWithObstructionAt)
                .filter(lab -> lab.patrolPath().isStuck())
                .count();
    }

    @EqualsAndHashCode
    static class Lab {
        private final Set<Coordinate> obstructions;
        private final int maxY;
        private final int maxX;
        private Guard guard;

        private Lab(int maxY, int maxX, Set<Coordinate> obstructions, GuardPosition guardPosition) {
            this.maxY = maxY;
            this.maxX = maxX;
            this.obstructions = Set.copyOf(obstructions);
            this.guard = new Guard(guardPosition);
        }

        Lab(List<String> inputLines) {
            Set<Coordinate> obstructions = new HashSet<>();
            this.maxY = inputLines.size();
            this.maxX = inputLines.getFirst().length();

            for (int y = 0; y < inputLines.size(); y++) {
                for (int x = 0; x < inputLines.get(y).length(); x++) {
                    var symbol = new Symbol(inputLines.get(y).charAt(x));

                    if (symbol.isObstruction()) {
                        obstructions.add(new Coordinate(x, y));
                    } else if (symbol.isGuard()) {
                        this.guard = new Guard(new GuardPosition(new Coordinate(x, y), symbol.toGuardDirection()));
                    }
                }
            }
            this.obstructions = Set.copyOf(obstructions);
        }

        PatrolPath patrolPath() {
            return guard.patrolPath();
        }

        Lab newLabWithObstructionAt(Coordinate coordinate) {
            return new Lab(
                    this.maxY,
                    this.maxX,
                    Sets.union(this.obstructions, Set.of(coordinate)),
                    this.guard.guardPositions.getFirst());
        }

        private boolean isWithinLab(GuardPosition guardPosition) {
            return guardPosition.coordinate.y >= 0
                    && guardPosition.coordinate.y < maxY
                    && guardPosition.coordinate.x >= 0
                    && guardPosition.coordinate.x < maxX;
        }

        private boolean isObstruction(Coordinate coordinate) {
            return obstructions.contains(coordinate);
        }

        @EqualsAndHashCode
        class Guard {
            private final List<GuardPosition> guardPositions;

            Guard(GuardPosition firstPosition) {
                this.guardPositions = new ArrayList<>();
                this.guardPositions.add(firstPosition);
            }

            private PatrolPath patrolPath() {
                var currentPosition = guardPositions.getLast();
                var peek = currentPosition.moveForward();

                while (isWithinLab(peek)) {
                    if (isObstruction(peek.coordinate)) {
                        peek = currentPosition.turnRight();
                    } else {
                        currentPosition = peek;
                        guardPositions.add(currentPosition);
                        peek = currentPosition.moveForward();
                    }

                    if (wasAlreadyVisited(peek)) {
                        return PatrolPath.STUCK;
                    }
                }
                return PatrolPath.of(guardPositions);
            }

            private boolean wasAlreadyVisited(GuardPosition nextPosition) {
                return guardPositions.contains(nextPosition);
            }
        }
    }

    private record GuardPosition(Coordinate coordinate, Direction direction) {

        GuardPosition turnRight() {
            return new GuardPosition(coordinate, direction.turnRight());
        }

        GuardPosition moveForward() {
            return new GuardPosition(direction.forwardFrom(coordinate), direction);
        }
    }

    record PatrolPath(Set<Coordinate> guardPositions) {

        static PatrolPath STUCK = new PatrolPath(Collections.emptySet());

        static PatrolPath of(List<GuardPosition> positions) {
            return new PatrolPath(positions.stream().map(pos -> pos.coordinate).collect(Collectors.toSet()));
        }

        public long length() {
            return guardPositions.size();
        }

        public Stream<Coordinate> stream() {
            return guardPositions.stream();
        }

        public boolean isStuck() {
            return this.equals(STUCK);
        }
    }

    record Coordinate(int x, int y) {
    }

    private record Symbol(char value) {

        boolean isGuard() {
            return Direction.IDENTIFIERS.containsKey(value);
        }

        Direction toGuardDirection() {
            return Direction.IDENTIFIERS.get(value);
        }

        boolean isObstruction() {
            return value == '#';
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum Direction {
        NORTH('^'), SOUTH('v'), WEST('<'), EAST('>');

        private final char identifier;

        static final Map<Character, Direction> IDENTIFIERS = Arrays.stream(Direction.values())
                .collect(Collectors.toMap(Direction::getIdentifier, Function.identity()));

        Direction turnRight() {
            return switch (this) {
                case NORTH -> Direction.EAST;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.NORTH;
                case EAST -> Direction.SOUTH;
            };
        }

        Coordinate forwardFrom(Coordinate currentPosition) {
            return switch (this) {
                case NORTH -> new Coordinate(currentPosition.x, currentPosition.y - 1);
                case SOUTH -> new Coordinate(currentPosition.x, currentPosition.y + 1);
                case WEST -> new Coordinate(currentPosition.x - 1, currentPosition.y);
                case EAST -> new Coordinate(currentPosition.x + 1, currentPosition.y);
            };
        }
    }
}
