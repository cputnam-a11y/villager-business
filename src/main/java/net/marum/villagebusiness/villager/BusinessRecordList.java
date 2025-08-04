package net.marum.villagebusiness.villager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;

public record BusinessRecordList(ImmutableList<BusinessRecord> records) {
    public static final Codec<BusinessRecordList> CODEC = Codec.unboundedMap(Codec.STRING, Codec.LONG).xmap(
                    entries -> entries.entrySet().stream().map(e -> new BusinessRecord(e.getKey(), e.getValue())).toList(),
                    list -> list.stream().collect(ImmutableMap.toImmutableMap(BusinessRecord::itemId, BusinessRecord::timestamp))
            )
            .xmap(
                    ImmutableList::copyOf,
                    Function.identity()
            )
            .xmap(
                    BusinessRecordList::new,
                    BusinessRecordList::records
            );

    public BusinessRecordList() {
        this(ImmutableList.of());
    }

    public BusinessRecordList add(String itemId, long timestamp) {
        ArrayList<BusinessRecord> newRecords = new ArrayList<>(this.records);
        newRecords.add(new BusinessRecord(itemId, timestamp));
        return new BusinessRecordList(ImmutableList.copyOf(newRecords));
    }

    public BusinessRecordList remove(String itemId) {
        ArrayList<BusinessRecord> newRecords = new ArrayList<>(this.records);
        newRecords.removeIf(record -> record.itemId().equals(itemId));
        return new BusinessRecordList(ImmutableList.copyOf(newRecords));
    }

    public boolean contains(String itemId) {
        return this.records.stream().anyMatch(record -> record.itemId().equals(itemId));
    }

    @Nullable
    public BusinessRecord get(String itemId) {
        return this.records.stream()
                .filter(record -> record.itemId().equals(itemId))
                .findFirst()
                .orElse(null);
    }
}
