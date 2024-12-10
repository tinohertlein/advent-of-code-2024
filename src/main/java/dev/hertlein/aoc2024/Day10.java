package dev.hertlein.aoc2024;

import dev.hertlein.aoc2024.Day10.TopologyMap.HikingPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day10 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return new TopologyMap(inputLines).sumOfTrailheadsScores();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return new TopologyMap(inputLines).sumOfTrailheadsRatings();
    }

    @AllArgsConstructor
    static class TopologyMap {

        private final Map<Coordinate, HikingPoint> availableHikingPoints;

        TopologyMap(List<String> inputLines) {
            var points = new HashMap<Coordinate, HikingPoint>();

            for (int y = 0; y < inputLines.size(); y++) {
                for (int x = 0; x < inputLines.get(y).length(); x++) {
                    var height = inputLines.get(y).charAt(x);
                    if (height != '.') {
                        var coordinate = new Coordinate(x, y);
                        points.put(coordinate, new HikingPoint(coordinate, height));
                    }
                }
            }
            this.availableHikingPoints = Map.copyOf(points);
        }

        long sumOfTrailheadsScores() {
            return findTrailHeads().mapToLong(HikingPoint::score).sum();
        }

        long sumOfTrailheadsRatings() {
            return findTrailHeads().mapToLong(HikingPoint::rating).sum();
        }

        private Stream<HikingPoint> findTrailHeads() {
            return availableHikingPoints.values().stream().filter(HikingPoint::isTrailHead);
        }

        @AllArgsConstructor
        @Getter
        @ToString
        class HikingPoint {

            private final Coordinate coordinate;
            private final int height;

            HikingPoint(Coordinate coordinate, char height) {
                this(coordinate, Character.getNumericValue(height));
            }

            boolean isTrailHead() {
                return height == 0;
            }

            boolean isTrailEnd() {
                return height == 9;
            }

            private int heightIncreaseTo(HikingPoint other) {
                return other.height - this.height;
            }

            boolean isHeightIncreaseBy1To(HikingPoint other) {
                return heightIncreaseTo(other) == 1;
            }

            long score() {
                return findTrailEnds(Set.of(this)).size();
            }

            long rating() {
                var start = List.of(new HikingTrail(List.of(this)));
                return findTrails(start).stream().distinct().count();
            }

            private Set<HikingPoint> findTrailEnds(Set<HikingPoint> hikingPoints) {
                if (hikingPoints.stream().allMatch(HikingPoint::isTrailEnd)) {
                    return hikingPoints;
                }

                var nextHikingPoints = hikingPoints.stream()
                        .flatMap(HikingPoint::nextPoints)
                        .collect(Collectors.toSet());

                return findTrailEnds(nextHikingPoints);
            }

            private List<HikingTrail> findTrails(List<HikingTrail> trails) {
                if (trails.stream().allMatch(HikingTrail::hasEnded)) {
                    return trails;
                }

                var spawnedTrails = trails.stream()
                        .map(trail -> Pair.of(trail, trail.lastPoint()))
                        .map(trailAndLastPoint -> Pair.of(trailAndLastPoint.getLeft(), trailAndLastPoint.getRight().nextPoints()))
                        .flatMap(trailAndNextPoints -> trailAndNextPoints.getLeft().spawnTrailsWith(trailAndNextPoints.getRight()))
                        .toList();

                return findTrails(spawnedTrails);
            }

            private Stream<HikingPoint> nextPoints() {
                return Direction.valuesAsStream()
                        .map(direction -> direction.next(coordinate))
                        .map(availableHikingPoints::get)
                        .filter(Objects::nonNull)
                        .filter(this::isHeightIncreaseBy1To);
            }
        }
    }

    record HikingTrail(List<HikingPoint> points) {

        Stream<HikingTrail> spawnTrailsWith(Stream<HikingPoint> points) {
            return points.map(this::newTrailWithAppended);
        }

        HikingTrail newTrailWithAppended(HikingPoint point) {
            return new HikingTrail(Stream.concat(
                            points.stream(),
                            Stream.of(point))
                    .toList());
        }

        HikingPoint lastPoint() {
            return points.getLast();
        }

        boolean hasEnded() {
            return points.getLast().isTrailEnd();
        }
    }

    record Coordinate(int x, int y) {
    }

    enum Direction {
        NORTH, SOUTH, WEST, EAST;

        Coordinate next(Coordinate current) {
            return switch (this) {
                case NORTH -> new Coordinate(current.x, current.y - 1);
                case SOUTH -> new Coordinate(current.x, current.y + 1);
                case WEST -> new Coordinate(current.x - 1, current.y);
                case EAST -> new Coordinate(current.x + 1, current.y);
            };
        }

        static Stream<Direction> valuesAsStream() {
            return Arrays.stream(Direction.values());
        }
    }
}
