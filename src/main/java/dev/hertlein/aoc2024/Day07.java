package dev.hertlein.aoc2024;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static dev.hertlein.aoc2024.Day07.Operator.*;

class Day07 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return Equations.of(inputLines).calibrationResultWith(Set.of(ADD, MULTIPLY));
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return Equations.of(inputLines).calibrationResultWith(Set.of(ADD, MULTIPLY, CONCAT));
    }

    record Equations(List<Equation> equations) {

        static Equations of(List<String> inputLines) {
            return new Equations(
                    inputLines.stream()
                            .map(Equation::of)
                            .toList());
        }

        long calibrationResultWith(Set<Operator> operators) {
            return equations.stream()
                    .filter(equation -> equation.isTestResultPossiblyCorrectWith(operators))
                    .mapToLong(Equation::testResult)
                    .sum();
        }
    }

    record Equation(long testResult, List<Long> operands) {

        static Equation of(String inputLine) {
            var testResultAndOperands = inputLine.split(": ");

            var testResult = Long.parseLong(testResultAndOperands[0]);
            var operands = Arrays.stream(testResultAndOperands[1].split(" "))
                    .map(Long::parseLong)
                    .toList();

            return new Equation(testResult, operands);
        }

        boolean isTestResultPossiblyCorrectWith(Set<Operator> operators) {
            return isTestResultPossiblyCorrect(
                    testResult,
                    operands.getFirst(),
                    List.copyOf(operands.subList(1, operands.size())),
                    operators
            );
        }

        private boolean isTestResultPossiblyCorrect(long testResult, long accumulator, List<Long> operands, Set<Operator> operators) {
            if (operands.isEmpty()) {
                return testResult == accumulator;
            }
            var nextOperand = operands.getFirst();
            var remainingOperands = operands.subList(1, operands.size());

            return operators.stream()
                    .map(operator -> operator.apply(accumulator, nextOperand))
                    .anyMatch(acc -> isTestResultPossiblyCorrect(testResult, acc, remainingOperands, operators));
        }
    }

    enum Operator {
        ADD {
            @Override
            long apply(long operand1, long operand2) {
                return operand1 + operand2;
            }
        }, MULTIPLY {
            @Override
            long apply(long operand1, long operand2) {
                return operand1 * operand2;
            }
        }, CONCAT {
            @Override
            long apply(long operand1, long operand2) {
                return Long.parseLong("" + operand1 + operand2);
            }
        };

        abstract long apply(long operand1, long operand2);
    }
}
