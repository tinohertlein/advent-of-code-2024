package dev.hertlein.aoc2024;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.hertlein.aoc2024.Day05.PrintQueue.PageUpdates.sumOfMiddlePageNumbersOf;
import static java.util.Collections.emptySet;
import static java.util.Map.entry;
import static java.util.function.Predicate.not;
import static java.util.stream.Gatherers.windowSliding;

class Day05 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return new PrintQueue(inputLines).sumOfMiddlePageNumbersOfInitiallyOrderedUpdates();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return new PrintQueue(inputLines).sumOfMiddlePageNumbersOfFixedMisorderedUpdates();
    }

    static class PrintQueue {

        final PageOrderRules rules;
        final PageUpdates updates;

        PrintQueue(List<String> inputLines) {
            var pageRulesBlock = inputLines.stream().takeWhile(not(String::isEmpty));
            var pageUpdatesBlock = inputLines.stream().dropWhile(not(String::isEmpty)).dropWhile(String::isEmpty);

            this.rules = PageOrderRules.of(pageRulesBlock);
            this.updates = new PageUpdates(pageUpdatesBlock);
        }

        long sumOfMiddlePageNumbersOfInitiallyOrderedUpdates() {
            return sumOfMiddlePageNumbersOf(updates.initiallyOrderedOnes());
        }

        long sumOfMiddlePageNumbersOfFixedMisorderedUpdates() {
            return sumOfMiddlePageNumbersOf(updates.fixedMisorderedOnes());
        }

        record PageOrderRules(Map<Page, Set<Page>> pageToItsFollowers) {

            static PageOrderRules of(Stream<String> inputLines) {
                Pattern regex = Pattern.compile("(\\d+)\\|(\\d+)");

                var pageToItsFollowers = inputLines
                        .flatMap(line -> regex.matcher(line).results())
                        .map(matchResult -> entry(Page.of(matchResult.group(1)), Page.of(matchResult.group(2))))
                        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toSet())));

                return new PageOrderRules(pageToItsFollowers);
            }

            boolean isFirstPageFollowedBySecondPage(List<Page> pagePair) {
                return isFirstPageFollowedBySecondPage(pagePair.getFirst(), pagePair.getLast());
            }

            boolean isFirstPageFollowedBySecondPage(Page first, Page second) {
                return pageToItsFollowers.getOrDefault(first, emptySet()).contains(second);
            }
        }

        @RequiredArgsConstructor
        class PageUpdates {

            final List<PageUpdate> values;

            PageUpdates(Stream<String> inputLine) {
                this(inputLine.map(PageUpdate::new).toList());
            }

            Stream<PageUpdate> initiallyOrderedOnes() {
                return values.stream().filter(PageUpdate::isOrderedCorrectly);
            }

            Stream<PageUpdate> fixedMisorderedOnes() {
                return values.stream()
                        .filter(not(PageUpdate::isOrderedCorrectly))
                        .map(PageUpdate::fixOrder);
            }

            static long sumOfMiddlePageNumbersOf(Stream<PageUpdate> pageUpdates) {
                return pageUpdates.map(PageUpdate::pageInTheMiddle).mapToInt(Page::number).sum();
            }
        }

        @RequiredArgsConstructor
        class PageUpdate {

            private final Comparator<Page> comparator = (first, second) -> first.number == second.number ? 0 : rules.isFirstPageFollowedBySecondPage(first, second) ? -1 : 1;
            private final List<Page> pageNumbers;

            PageUpdate(String inputLine) {
                this(Arrays.stream(inputLine.split(",")).map(Page::of).toList());
            }

            Page pageInTheMiddle() {
                return pageNumbers.get(pageNumbers.size() / 2);
            }

            boolean isOrderedCorrectly() {
                return pageNumbers.stream()
                        .gather(windowSliding(2))
                        .allMatch(rules::isFirstPageFollowedBySecondPage);
            }

            PageUpdate fixOrder() {
                return new PageUpdate(pageNumbers.stream().sorted(comparator).toList());
            }
        }

        record Page(int number) {

            static Page of(String numberAsString) {
                return new Page(Integer.parseInt(numberAsString));
            }
        }
    }
}