package net.marum.villagebusiness.util;

/**
 * A counter that accepts input values in blocks, unit, and nugget values, and allows extracting the total count in blocks, plus a minimal remainder in units or nuggets.
 * the total count is stored in nuggets, as a long value.
 */
public class ResourceCount {
    private long count;

    public ResourceCount() {
        this.count = 0;
    }

    /**
     * @param count the total count, in nuggets.
     */
    public ResourceCount(long count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        this.addNuggets(count);
    }

    public ResourceCount(long blocks, long units, long nuggets) {
        if (blocks < 0 || units < 0 || nuggets < 0) {
            throw new IllegalArgumentException("Blocks, units, and nuggets cannot be negative");
        }
        this.addBlocks(blocks);
        this.addUnits(units);
        this.addNuggets(nuggets);
    }


    public void addBlocks(long numBlocks) {
        if (numBlocks < 0) {
            throw new IllegalArgumentException("Number of blocks to add cannot be negative");
        }
        this.count += numBlocks * 81;
    }

    public void addUnits(long numUnits) {
        if (numUnits < 0) {
            throw new IllegalArgumentException("Number of units to add cannot be negative");
        }
        this.count += numUnits * 9;
    }

    public void addNuggets(long numNuggets) {
        if (numNuggets < 0) {
            throw new IllegalArgumentException("Number of nuggets to add cannot be negative");
        }
        this.count += numNuggets;
    }

    public void clear() {
        this.count = 0;
    }

    public boolean subtractBlocks(long numBlocks) {
        if (numBlocks < 0) {
            throw new IllegalArgumentException("Number of blocks to subtract cannot be negative");
        }

        var temp = this.count - (numBlocks * 81);
        if (temp < 0) {
            return false; // Not enough blocks to subtract
        }

        this.count -= numBlocks * 81;
        return true;
    }

    public boolean subtractUnits(long numUnits) {
        if (numUnits < 0) {
            throw new IllegalArgumentException("Number of units to subtract cannot be negative");
        }

        var temp = this.count - (numUnits * 9);
        if (temp < 0) {
            return false; // Not enough units to subtract
        }

        this.count -= numUnits * 9;
        return true;
    }

    public boolean subtractNuggets(long numNuggets) {
        if (numNuggets < 0) {
            throw new IllegalArgumentException("Number of nuggets to subtract cannot be negative");
        }

        if (this.count < numNuggets) {
            return false; // Not enough nuggets to subtract
        }

        this.count -= numNuggets;
        return true;
    }

    public long getBlocks() {
        return this.count / 81;
    }

    public long getUnits() {
        return (this.count % 81) / 9;
    }

    public long getNuggets() {
        return this.count % 9;
    }
}
