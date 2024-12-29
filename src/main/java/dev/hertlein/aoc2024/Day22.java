package dev.hertlein.aoc2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Gatherers.windowSliding;

class Day22 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return Buyers.of(inputLines)
                .nthSecretNumbers(2_000)
                .stream()
                .mapToLong(number -> number)
                .sum();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return Buyers.of(inputLines)
                .maxBananas(2_000);
    }

    record Buyers(List<Buyer> buyers) {

        static Buyers of(List<String> input) {
            return new Buyers(input.stream()
                    .map(initialNumber -> new Buyer(parseInt(initialNumber)))
                    .toList());
        }

        List<Long> nthSecretNumbers(int nth) {
            return buyers
                    .stream()
                    .map(buyer -> buyer.nthSecretNumber(nth))
                    .toList();
        }

        Long maxBananas(int nth) {
            return priceChanges(nth)
                    .stream()
                    .flatMap(map -> map.entrySet().stream())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum))
                    .values()
                    .stream()
                    .mapToLong(number -> number)
                    .max()
                    .orElseThrow();
        }

        private List<Map<PriceSequence, Long>> priceChanges(int nth) {
            return buyers
                    .stream()
                    .map(buyer -> buyer.priceChanges(nth))
                    .toList();
        }
    }

    record Buyer(long initialNumber) {

        long nthSecretNumber(int nth) {
            return LongStream.range(0, nth)
                    .reduce(initialNumber, (acc, _) -> Calculator.nextNumber(acc));
        }

        Map<PriceSequence, Long> priceChanges(int nth) {
            return prices(nth)
                    .stream()
                    .gather(windowSliding(5))
                    .collect(toMap(
                            window -> new PriceSequence(window.get(1) - window.get(0), window.get(2) - window.get(1), window.get(3) - window.get(2), window.get(4) - window.get(3)),
                            window -> window.get(4),
                            // First one wins
                            (price, _) -> price
                    ));
        }

        List<Long> prices(int nth) {
            List<Long> secretNumbers = new ArrayList<>();
            secretNumbers.add(initialNumber);
            return LongStream.range(0, nth - 1)
                    .boxed()
                    .reduce(secretNumbers,
                            (acc, _) -> {
                                acc.add(Calculator.nextNumber(acc.getLast()));
                                return acc;
                            },
                            (acc1, acc2) -> {
                                acc1.addAll(acc2);
                                return acc1;
                            }
                    )
                    .stream()
                    .map(secretNumber -> secretNumber % 10)
                    .toList();
        }
    }

    record PriceSequence(long a, long b, long c, long d) {
    }

    static class Calculator {

        static long nextNumber(long given) {
            return step3(step2(step1(given)));
        }

        static long step1(long given) {
            return prune(mix(given, given * 64));
        }

        static long step2(long given) {
            return prune(mix(given, given / 32));
        }

        static long step3(long given) {
            return prune(mix(given, given * 2_048));
        }

        static long mix(long given, long secret) {
            return given ^ secret;
        }

        static long prune(long secret) {
            return secret % 16_777_216;
        }
    }
}
