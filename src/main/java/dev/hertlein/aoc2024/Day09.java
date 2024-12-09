package dev.hertlein.aoc2024;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.hertlein.aoc2024.Day09.BlockType.DATA;
import static dev.hertlein.aoc2024.Day09.BlockType.FREE;
import static java.util.stream.Collectors.*;

class Day09 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return Disk.of(inputLines.getFirst())
                .compactByMovingDataBlocks()
                .checkSum();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return Disk.of(inputLines.getFirst())
                .compactByMovingFiles()
                .checkSum();
    }

    record Disk(List<Block> blocks) {

        static Disk of(String inputLine) {
            List<DenseBlock> denseBlocks = new ArrayList<>();

            for (int i = 0; i < inputLine.length() / 2; i++) {
                denseBlocks.add(DenseBlock.of(i, inputLine.charAt(i * 2), DATA));
                denseBlocks.add(DenseBlock.of(i, inputLine.charAt(i * 2 + 1), FREE));
            }
            if (inputLine.length() % 2 != 0) {
                denseBlocks.add(DenseBlock.of(inputLine.length() / 2, inputLine.charAt(inputLine.length() - 1), DATA));
            }

            return new Disk(
                    denseBlocks
                            .stream()
                            .flatMap(block -> block.uncompress().stream())
                            .toList());
        }

        @Override
        public String toString() {
            return blocks.stream().map(Block::toString).collect(joining(""));
        }

        Disk compactByMovingDataBlocks() {
            var rearrangedBlocks = new ArrayList<>(blocks);

            for (int fromFront = 0, fromBack = rearrangedBlocks.size() - 1; fromFront < fromBack; ) {
                var frontBlock = rearrangedBlocks.get(fromFront);
                var backBlock = rearrangedBlocks.get(fromBack);

                if (frontBlock.isData()) {
                    // continue loop with next block from front direction, while holding the block from back direction
                    fromFront++;
                } else if (frontBlock.isFree() && backBlock.isFree()) {
                    // continue loop with next block from back direction, while holding the block from front direction
                    fromBack--;
                } else if (frontBlock.isFree() && backBlock.isData()) {
                    // switch places
                    rearrangedBlocks.set(fromFront, backBlock);
                    rearrangedBlocks.set(fromBack, frontBlock);
                    // continue loop with next block from front direction and next block from back direction
                    fromFront++;
                    fromBack--;
                }
            }
            return new Disk(rearrangedBlocks);
        }

        Disk compactByMovingFiles() {
            var initialFiles = blocks.stream()
                    .collect(groupingBy(Block::fileId))
                    .entrySet()
                    .stream()
                    .map(fileIdAndBlocks -> File.of(fileIdAndBlocks.getKey(), fileIdAndBlocks.getValue()))
                    .toList();

            var rearrangedFiles = new ArrayList<>(initialFiles);

            for (int fromBack = rearrangedFiles.size() - 1; fromBack >= 0; fromBack--) {
                for (int fromFront = 0; fromFront < fromBack; fromFront++) {

                    var frontFile = rearrangedFiles.get(fromFront);
                    var backFile = rearrangedFiles.get(fromBack);

                    if (frontFile.hasEnoughFreeSpaceForDataOf(backFile)) {
                        var filesWithRearrangedBlocks = frontFile.rearrangeBlocksOf(backFile);

                        rearrangedFiles.set(fromFront, filesWithRearrangedBlocks.getLeft());
                        rearrangedFiles.set(fromBack, filesWithRearrangedBlocks.getRight());
                        rearrangedFiles.add(fromFront + 1, filesWithRearrangedBlocks.getMiddle());
                        fromBack++;
                        break;
                    }
                }
            }
            return new Disk(
                    rearrangedFiles
                            .stream()
                            .flatMap(file -> file.toBlocks().stream())
                            .toList());
        }

        long checkSum() {
            return IntStream
                    .range(0, blocks.size())
                    .mapToObj(index -> Pair.of(index, blocks.get(index)))
                    .filter(indexAndBlock -> indexAndBlock.getRight().isData())
                    .mapToLong(indexAndBlock -> (long) indexAndBlock.getLeft() * indexAndBlock.getRight().fileId())
                    .sum();
        }
    }

    enum BlockType {
        DATA, FREE
    }

    private record Block(int fileId, BlockType type) {

        boolean isData() {
            return type == DATA;
        }

        boolean isFree() {
            return type == FREE;
        }

        @Override
        public String toString() {
            return type == FREE ? "." : String.valueOf(fileId);
        }
    }

    private record DenseBlock(int fileId, int length, BlockType type) {

        static DenseBlock of(int fileId, char dataSpace, BlockType type) {
            return new DenseBlock(fileId, Character.getNumericValue(dataSpace), type);
        }

        List<Block> uncompress() {
            return Collections.nCopies(length, new Block(fileId, type));
        }
    }

    private record File(int id, int dataSpace, int freeSpace) {

        static File of(int fileId, List<Block> blocks) {
            var countByBlockType = blocks.stream().collect(groupingBy(Block::type, counting()));

            return new File(fileId,
                    countByBlockType.getOrDefault(DATA, 0L).intValue(),
                    countByBlockType.getOrDefault(FREE, 0L).intValue());
        }

        List<Block> toBlocks() {
            var dataBlocks = Collections.nCopies(dataSpace, new Block(id, DATA));
            var freeBlocks = Collections.nCopies(freeSpace, new Block(id, FREE));

            return Stream
                    .concat(dataBlocks.stream(), freeBlocks.stream())
                    .toList();
        }

        boolean hasEnoughFreeSpaceForDataOf(File other) {
            return this.freeSpace >= other.dataSpace;
        }

        Triple<File, File, File> rearrangeBlocksOf(File other) {
            var fileWithRemovedFreeSpace = new File(this.id, this.dataSpace, 0);
            var fileWithMovedDataSpace = new File(other.id, other.dataSpace, this.freeSpace - other.dataSpace);
            var fileWithNowFreeSpace = new File(Integer.MIN_VALUE, 0, other.freeSpace + other.dataSpace);

            return Triple.of(fileWithRemovedFreeSpace, fileWithMovedDataSpace, fileWithNowFreeSpace);
        }
    }
}