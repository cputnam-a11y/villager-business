package net.marum.villagebusiness.villager;

/**
 *
 * @param itemId the first buy item of this trade offer.
 * @param timestamp the time of the purchase.
 */
public record BusinessRecord(String itemId, long timestamp) {
//    /**
//     * Returns the first buy item of this trade offer.
//     */
//    public String itemId() {
//        return this.itemId;
//    }
//
//    /**
//     * Returns the time of the purchase.
//     */
//    public long timestamp() {
//        return this.timestamp;
//    }
}

