package dev.hertlein.aoc2024;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static dev.hertlein.aoc2024.Day17.Register.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

class Day17 implements Day<Void> {

    @Override
    public String part1(List<String> inputLines, Void v) {
        return new Computer(inputLines).executeProgram();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return new Computer(inputLines.getLast()).quine();
    }

    @AllArgsConstructor
    @Data
    static class Computer {
        private List<Integer> program;
        private Map<Register, Long> storage;

        private static final Pattern REGEX_REGISTER = Pattern.compile("(\\d+)");
        private static final Pattern REGEX_INSTRUCTION = Pattern.compile("(\\d)");

        Computer(List<String> inputLines) {
            this.storage = Arrays.stream(Register.values())
                    .collect(toMap(identity(), type -> {
                        var matcher = REGEX_REGISTER.matcher(inputLines.get(type.ordinal()));
                        return matcher.find() ? Long.parseLong(matcher.group(1)) : 0;
                    }));

            this.program = REGEX_INSTRUCTION.matcher(inputLines.get(4))
                    .results()
                    .map(result -> Integer.parseInt(result.group(1)))
                    .toList();
        }

        Computer(String programLine) {
            clearStorage();
            this.program = REGEX_INSTRUCTION.matcher(programLine)
                    .results()
                    .map(result -> Integer.parseInt(result.group(1)))
                    .toList();
        }

        void clearStorage() {
            this.storage = Arrays.stream(Register.values())
                    .collect(toMap(identity(), _ -> 0L));
        }

        Long getValueAt(Register register) {
            return storage.get(register);
        }

        void setValueAt(Register register, Long value) {
            storage.put(register, value);
        }

        Long quine() {
            /*
                2,4 --> b = a % 8
                1,1 --> b = b ^ 1
                7,5 --> c = a / Math.pow(2, b)
                1,5 --> b = b ^ 5
                4,0 --> b = b ^ c
                0,3 --> a = a / 8
                5,5 --> output(b % 8)
                3,0 --> if a != 0 goto 0
            */

            var candidates = List.of(0L);

            for (int subProgramLength = 1; subProgramLength <= program.size(); subProgramLength++) {
                String subProgram = outputAsCsv(program.subList(
                        program.size() - subProgramLength,
                        program.size()));

                candidates = candidates
                        .stream()
                        .flatMap(candidate -> LongStream.range(0, 8).mapToObj(l -> candidate * 8 | l))
                        .filter(candidate -> {
                            clearStorage();
                            setValueAt(A, candidate);
                            return executeProgram().equals(subProgram);
                        })
                        .toList();
            }
            return candidates.getFirst();
        }

        String executeProgram() {
            var output = new ArrayList<>();
            var result = new ExecutionResult(ExecutionResult.DEFAULT_OUTPUT, 0, false);

            var instructionPointer = 0;

            while (instructionPointer < program.size()) {
                Integer opcode = program.get(instructionPointer);
                Integer operand = program.get(instructionPointer + 1);

                result = executeInstruction(result, opcode, operand);
                instructionPointer = result.instructionPointer;

                if (result.hasOutput()) {
                    output.add(result.output);
                }
            }
            return outputAsCsv(output);
        }

        private ExecutionResult executeInstruction(ExecutionResult previousResult, int opcode, int operand) {
            return switch (opcode) {
                case 0 -> {
                    setValueAt(A, (long) (getValueAt(A) / Math.pow(2, combo(operand))));
                    yield previousResult.simple();
                }
                case 1 -> {
                    setValueAt(B, getValueAt(B) ^ literal(operand));
                    yield previousResult.simple();
                }
                case 2 -> {
                    setValueAt(B, combo(operand) % 8);
                    yield previousResult.simple();
                }
                case 3 -> {
                    if (getValueAt(A) == 0) {
                        yield previousResult.simple();
                    } else {
                        if (previousResult.isJump) {
                            yield previousResult.withoutProceeding();
                        } else {
                            yield previousResult.withJump(literal(operand));
                        }
                    }
                }

                case 4 -> {
                    setValueAt(B, getValueAt(B) ^ getValueAt(C));
                    yield previousResult.simple();
                }

                case 5 -> previousResult.withOutput(String.valueOf(combo(operand) % 8));

                case 6 -> {
                    setValueAt(B, (long) (getValueAt(A) / Math.pow(2, combo(operand))));
                    yield previousResult.simple();
                }
                case 7 -> {
                    setValueAt(C, (long) (getValueAt(A) / Math.pow(2, combo(operand))));
                    yield previousResult.simple();
                }

                default -> throw new NotImplementedException("Unknown opcode: " + opcode);
            };
        }

        private int literal(int operand) {
            return operand;
        }

        private long combo(int operand) {
            return switch (operand) {
                case 0 -> 0;
                case 1 -> 1;
                case 2 -> 2;
                case 3 -> 3;
                case 4 -> storage.get(A);
                case 5 -> storage.get(B);
                case 6 -> storage.get(C);
                default -> throw new IllegalArgumentException("Operand invalid: '%s'".formatted(operand));
            };
        }

        @Override
        public String toString() {
            StringBuilder output = new StringBuilder();

            output.append(" a=").append(storage.getOrDefault(A, 0L));
            output.append(",b=").append(storage.getOrDefault(B, 0L));
            output.append(",c=").append(storage.getOrDefault(C, 0L));
            output.append(", program: ").append(program);

            return output.toString();
        }

        private String outputAsCsv(Collection<?> collection) {
            return Joiner.on(',').join(collection);
        }
    }

    record ExecutionResult(String output, int instructionPointer, boolean isJump) {
        private final static int DEFAULT_PROCEEDING = 2;
        private final static String DEFAULT_OUTPUT = "";

        ExecutionResult simple() {
            return new ExecutionResult(DEFAULT_OUTPUT, instructionPointer + DEFAULT_PROCEEDING, false);
        }

        ExecutionResult withOutput(String output) {
            return new ExecutionResult(output, instructionPointer + DEFAULT_PROCEEDING, false);
        }

        ExecutionResult withJump(int to) {
            return new ExecutionResult(DEFAULT_OUTPUT, to, true);
        }

        ExecutionResult withoutProceeding() {
            return new ExecutionResult(DEFAULT_OUTPUT, instructionPointer, false);
        }

        boolean hasOutput() {
            return !DEFAULT_OUTPUT.equals(output);
        }
    }

    enum Register {
        A, B, C
    }
}
