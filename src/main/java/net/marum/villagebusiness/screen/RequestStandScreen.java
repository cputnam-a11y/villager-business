package net.marum.villagebusiness.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.marum.villagebusiness.VillageBusiness;
import net.marum.villagebusiness.init.VillagerBusinessItemInit;
import net.marum.villagebusiness.pricing.ItemPrice;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RequestStandScreen extends HandledScreen<RequestStandScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(VillageBusiness.MOD_ID, "textures/gui/request_stand_gui.png");
    boolean isREILoaded = false;

    public RequestStandScreen(RequestStandScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width-backgroundWidth)/2;
        int y = (height-backgroundHeight)/2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        
        // Render ghost slot item
        ItemStack filterItem = handler.getFilterItem();
        if (!filterItem.isEmpty()) {
            context.drawItem(filterItem, x+145, y+52);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        int x = (width-backgroundWidth)/2;
        int y = (height-backgroundHeight)/2;

        context.drawText(textRenderer, Text.translatable("village_business.request"), x+93, y+56, 0x444444, false);
        
        if (handler.blockEntity.hasFilter()) {
            if (handler.blockEntity.getItemPrice() != null) {
                ItemPrice itemPrice = handler.blockEntity.getItemPrice();
                boolean enoughEmeralds = handler.blockEntity.hasEnoughEmeralds();
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("x"+getConvertedPrice(handler.blockEntity.getRequestPrice())), x+48+36, y+25, enoughEmeralds ? 0xffffff : 0xff8888);
                boolean enoughSpace = handler.blockEntity.hasEnoughSpace();
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("x"+itemPrice.getSellAmount(1)), x+93+36, y+25, enoughSpace ? 0xffffff : 0xff8888);
                context.drawCenteredTextWithShadow(textRenderer, getSaleChanceText(handler.blockEntity.getRequestChance()), x+48, y+43, 0xffffff);
                context.drawCenteredTextWithShadow(textRenderer, getSaleFrequencyText(handler.blockEntity.getRequestCooldown()), x+48, y+57, 0xffffff);
            } else {
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x+48+36, y+25, 0xff8888);
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x+93+36, y+25, 0xff8888);
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("0%"), x+48, y+43, 0xff8888);
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x+48, y+57, 0xff8888);
            }
        } else {
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x+48+36, y+25, 0xcccccc);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x+93+36, y+25, 0xcccccc);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x+48, y+43, 0xcccccc);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x+48, y+57, 0xcccccc);
        }

        tooltip("village_business.seller_chance_tip", 15, 40, 50, 12, context, mouseX, mouseY, textRenderer);
        tooltip("village_business.seller_return_tip", 15, 54, 50, 12, context, mouseX, mouseY, textRenderer);

        tooltip("village_business.request_slot", 145, 51, 18, 18, context, mouseX, mouseY, textRenderer);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check if ghost slot is clicked
        if (isMouseOverGhostSlot(mouseX, mouseY)) { // Adjust position/size
            ItemStack cursorStack = this.client.player.currentScreenHandler.getCursorStack();
            if (cursorStack.getItem() == Items.EMERALD || cursorStack.getItem() == Items.EMERALD_BLOCK || cursorStack.getItem() == VillagerBusinessItemInit.EMERALD_NUGGET)
                return false;
                
            if (!cursorStack.isEmpty()) {
                handler.blockEntity.sendRequestToServer(cursorStack.copy());
            } else {
                handler.blockEntity.sendRequestToServer(ItemStack.EMPTY);
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void tooltip(String translationKey, int x, int y, int w, int h, DrawContext context, int mouseX, int mouseY, TextRenderer textRenderer) {
        if (isPointWithinBounds(x, y, w, h, mouseX, mouseY))
            context.drawTooltip(textRenderer, Text.translatable(translationKey), mouseX, mouseY);
    }

    private String getConvertedPrice(int price) {
        if (price%9 == 0) return ""+(price/9);
        return ""+Math.round((100*price)/9.0f)/100f;
    }

    private Text getSaleChanceText(int saleChance) {
        return Text.of(saleChance+"%");
    }

    private Text getSaleFrequencyText(float cooldown) {
        return getSaleFrequencyText(Math.round(cooldown));
    }

    private Text getSaleFrequencyText(int cooldown) {
        String timeString = cooldown+"s";
        if (cooldown >= 60 && cooldown%60 == 0) {
            timeString = cooldown/60+"m";
        }
        return Text.of(timeString);
    }

    private boolean isMouseOverGhostSlot(double mouseX, double mouseY) {
        return isPointWithinBounds(145, 51, 18, 18, (int) mouseX, (int) mouseY);
    }
}
