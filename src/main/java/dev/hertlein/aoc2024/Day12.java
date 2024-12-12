package dev.hertlein.aoc2024;

import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

class Day12 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return new Garden(inputLines).fenceCost();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return new Garden(inputLines).fenceCostWithBulkDiscount();
    }

    static class Garden {

        private final Map<Coordinate, Plant> allPlants;

        Garden(List<String> inputLines) {
            var gardenPlants = new HashMap<Coordinate, Plant>();

            for (int y = 0; y < inputLines.size(); y++) {
                for (int x = 0; x < inputLines.get(y).length(); x++) {
                    var coordinate = new Coordinate(x, y);
                    var plant = new Plant(coordinate, inputLines.get(y).charAt(x));
                    gardenPlants.put(coordinate, plant);
                }
            }
            this.allPlants = gardenPlants;
        }

        long fenceCost() {
            return regions().stream().mapToLong(PlantRegion::fenceCost).sum();
        }

        long fenceCostWithBulkDiscount() {
            return regions().stream().mapToLong(PlantRegion::fenceCostWithBulkDiscount).sum();
        }

        List<PlantRegion> regions() {
            var plantsToCheck = new ArrayList<>(allPlants.values());
            var regions = new ArrayList<PlantRegion>();

            while (!plantsToCheck.isEmpty()) {
                Plant plantToCheck = plantsToCheck.getFirst();
                var region = new PlantRegion();
                fillRegionWithPlantsOfSameType(plantToCheck, region);
                plantsToCheck.removeAll(region.plants);
                regions.add(region);
            }
            return regions;
        }

        void fillRegionWithPlantsOfSameType(Plant plant, PlantRegion plantRegion) {
            var neighboursOfSameType = plant.coordinate.neighbours().stream()
                    .map(allPlants::get)
                    .filter(Objects::nonNull)
                    .filter(neighbour -> neighbour.type == plant.type)
                    .filter(not(plantRegion::contains))
                    .toList();

            if (plantRegion.contains(plant) && neighboursOfSameType.isEmpty()) {
                return;
            }

            plantRegion.addPlant(plant);
            for (var nextPlant : neighboursOfSameType) {
                fillRegionWithPlantsOfSameType(nextPlant, plantRegion);
            }
        }

        @AllArgsConstructor
        class PlantRegion {
            private Set<Plant> plants;

            PlantRegion() {
                this(new HashSet<>());
            }

            long fenceCostWithBulkDiscount() {
                long area = area();
                long sides = numberOfSides();
                return area * sides;
            }

            long fenceCost() {
                return area() * perimeter();
            }

            long area() {
                return plants.size();
            }

            long perimeter() {
                return plants.stream()
                        .reduce(0L,
                                (perimeter, plant) -> perimeter + Coordinate.MAX_NEIGHBOURS - neighboursInSameRegionOf(plant),
                                Long::sum);
            }

            long numberOfSides() {
                // number of sides in a polygon equals the number of corners
                return numberOfCorners();
            }

            long numberOfCorners() {
                var cornerCandidates = plants.stream()
                        .flatMap(plant -> plant.coordinate.corners().stream())
                        .collect(Collectors.toSet());

                var corners = 0;
                for (var cornerCandidate : cornerCandidates) {
                    var arePlantsInSameRegionAdjacent = cornerCandidate
                            .connectedCoordinates()
                            .stream().map(this::contains)
                            .toList();
                    var numberOfAdjacentPlantsInSameRegion = arePlantsInSameRegionAdjacent.stream().filter(b -> b).count();

                    if (numberOfAdjacentPlantsInSameRegion == 1) {
                        corners++;
                    }
                    if (numberOfAdjacentPlantsInSameRegion == 2) {
                        if (areOpposing(arePlantsInSameRegionAdjacent)) {
                            // Opposing plants count as two corners.
                            corners = corners + 2;
                        }
                    }
                    if (numberOfAdjacentPlantsInSameRegion == 3) {
                        corners++;
                    }
                }
                return corners;
            }

            boolean contains(Plant plant) {
                return plants.contains(plant);
            }

            private boolean contains(Coordinate coordinate) {
                return plants.stream().anyMatch(plant -> plant.coordinate.equals(coordinate));
            }

            void addPlant(Plant plant) {
                plants.add(plant);
            }

            private boolean areOpposing(List<Boolean> adjacentPlants) {
                return adjacentPlants.equals(List.of(true, false, true, false))
                        || adjacentPlants.equals(List.of(false, true, false, true));
            }

            private long neighboursInSameRegionOf(Plant plant) {
                return plant.coordinate.neighbours().stream().map(allPlants::get).filter(this::contains).count();
            }
        }
    }

    private static final double CORNER_ADJUSTMENT = 0.5;

    record Coordinate(int x, int y) {
        static final int MAX_NEIGHBOURS = 4;

        List<Coordinate> neighbours() {
            return List.of(
                    new Coordinate(x - 1, y),
                    new Coordinate(x + 1, y),
                    new Coordinate(x, y - 1),
                    new Coordinate(x, y + 1)
            );
        }

        List<PlantCorner> corners() {
            return List.of(
                    new PlantCorner(x - CORNER_ADJUSTMENT, y - CORNER_ADJUSTMENT),
                    new PlantCorner(x + CORNER_ADJUSTMENT, y - CORNER_ADJUSTMENT),
                    new PlantCorner(x + CORNER_ADJUSTMENT, y + CORNER_ADJUSTMENT),
                    new PlantCorner(x - CORNER_ADJUSTMENT, y + CORNER_ADJUSTMENT));
        }
    }

    record Plant(Coordinate coordinate, char type) {
    }

    record PlantCorner(double x, double y) {

        List<Coordinate> connectedCoordinates() {
            return List.of(
                    new Coordinate((int) (x - CORNER_ADJUSTMENT), (int) (y - CORNER_ADJUSTMENT)),
                    new Coordinate((int) (x + CORNER_ADJUSTMENT), (int) (y - CORNER_ADJUSTMENT)),
                    new Coordinate((int) (x + CORNER_ADJUSTMENT), (int) (y + CORNER_ADJUSTMENT)),
                    new Coordinate((int) (x - CORNER_ADJUSTMENT), (int) (y + CORNER_ADJUSTMENT)));
        }
    }
}
