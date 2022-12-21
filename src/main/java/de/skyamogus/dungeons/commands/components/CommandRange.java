package de.skyamogus.dungeons.commands.components;

public class CommandRange {

    private final int min;
    private final int max;

    public CommandRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean inRange(int length) {
        return length >= min && (length <= max || max == -1);
    }

}
