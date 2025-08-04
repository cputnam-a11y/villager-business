package net.marum.villagebusiness.attachment;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.marum.villagebusiness.VillagerBusiness;
import net.marum.villagebusiness.villager.BusinessRecordList;

public class VillagerBusinessAttachmentInit {
    public static final AttachmentType<Long> LAST_LURED_BY_BUSINESS = AttachmentRegistry.create(
            VillagerBusiness.id("last_lured_by_business"),
            builder -> builder
                    .initializer(() -> 0L)
                    .persistent(Codec.LONG)
    );

    public static final AttachmentType<BusinessRecordList> BUSINESS_RECORDS = AttachmentRegistry.create(
            VillagerBusiness.id("business_records"),
            builder -> builder
                    .initializer(BusinessRecordList::new)
                    .persistent(BusinessRecordList.CODEC)
    );
}
