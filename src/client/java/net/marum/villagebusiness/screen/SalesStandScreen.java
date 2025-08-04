package net.marum.villagebusiness.screen;

import net.marum.villagebusiness.VillagerBusiness;
import net.marum.villagebusiness.pricing.ItemPrice;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SalesStandScreen extends HandledScreen<SalesStandScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(VillagerBusiness.MOD_ID, "textures/gui/sales_stand_gui.png");

    public SalesStandScreen(SalesStandScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    ButtonWidget lowPriceButton;
    ButtonWidget normalPriceButton;
    ButtonWidget highPriceButton;

    @Override
    protected void init() {
        super.init();

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        int BUTTON_WIDTH = 18;
        int BUTTON_HEIGHT = 18;

        lowPriceButton = ButtonWidget.builder(
                        Text.of("/2"),
                        button -> onPriceSetLow()
                )
                .dimensions(x + 117 - 18, y + 50, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(lowPriceButton);
        lowPriceButton.active = handler.blockEntity.getPriceSetting() != 0;

        normalPriceButton = ButtonWidget.builder(
                        Text.of("x1"),
                        button -> onPriceSetNormal()
                )
                .dimensions(x + 117, y + 50, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(normalPriceButton);
        normalPriceButton.active = handler.blockEntity.getPriceSetting() != 1;

        highPriceButton = ButtonWidget.builder(
                        Text.of("x2"),
                        button -> onPriceSetHigh()
                )
                .dimensions(x + 117 + 18, y + 50, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(highPriceButton);
        highPriceButton.active = handler.blockEntity.getPriceSetting() != 2;
    }

    private void onPriceSetLow() {
        lowPriceButton.active = false;
        normalPriceButton.active = true;
        highPriceButton.active = true;
        handler.blockEntity.sendPriceSettingToServer(0);
    }

    private void onPriceSetNormal() {
        lowPriceButton.active = true;
        normalPriceButton.active = false;
        highPriceButton.active = true;
        handler.blockEntity.sendPriceSettingToServer(1);
    }

    private void onPriceSetHigh() {
        lowPriceButton.active = true;
        normalPriceButton.active = true;
        highPriceButton.active = false;
        handler.blockEntity.sendPriceSettingToServer(2);
    }


    @Override
    protected void drawBackground(DrawContext context, float tickProgress, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickProgress) {
        renderBackground(context, mouseX, mouseY, tickProgress);
        super.render(context, mouseX, mouseY, tickProgress);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawText(textRenderer, Text.translatable("village_business.price"), x + 78, y + 41, 0xFF444444, false);

        if (handler.blockEntity.hasProduct()) {
            if (handler.blockEntity.getItemPrice() != null) {
                int priceSetting = handler.blockEntity.getPriceSetting();
                ItemPrice itemPrice = handler.blockEntity.getItemPrice();
                boolean enoughProduct = handler.blockEntity.hasEnoughProduct();
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("x" + itemPrice.getSellAmount(priceSetting)), x + 48, y + 25, enoughProduct
                                                                                                                                            ? 0xFFffffff
                                                                                                                                            : 0xFFff8888);
                boolean enoughSpace = handler.blockEntity.canInsertAmountIntoOutputSlot();
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("x" + getConvertedPrice(itemPrice.getPrice(priceSetting))), x + 93, y + 25, enoughSpace
                                                                                                                                                          ? 0xFFffffff
                                                                                                                                                          : 0xFFff8888);
                context.drawCenteredTextWithShadow(textRenderer, getSaleChanceText(itemPrice.getSaleChance(priceSetting)), x + 48, y + 43, 0xFFffffff);
                context.drawCenteredTextWithShadow(textRenderer, getSaleFrequencyText(itemPrice.getCooldown(priceSetting)), x + 48, y + 57, 0xFFffffff);
            } else {
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x + 48, y + 25, 0xFFff8888);
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x + 93, y + 25, 0xFFff8888);
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("0%"), x + 48, y + 43, 0xFFff8888);
                context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x + 48, y + 57, 0xFFff8888);
            }
        } else {
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x + 48, y + 25, 0xFFcccccc);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x + 93, y + 25, 0xFFcccccc);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x + 48, y + 43, 0xFFcccccc);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), x + 48, y + 57, 0xFFcccccc);
        }

        tooltip("village_business.chance_tip", 15, 40, 50, 12, context, mouseX, mouseY, textRenderer);
        tooltip("village_business.return_tip", 15, 54, 50, 12, context, mouseX, mouseY, textRenderer);
    }

    private void tooltip(String translationKey, int x, int y, int w, int h, DrawContext context, int mouseX, int mouseY, TextRenderer textRenderer) {
        if (isPointWithinBounds(x, y, w, h, mouseX, mouseY))
            context.drawTooltip(textRenderer, Text.translatable(translationKey), mouseX, mouseY);
    }

    private String getConvertedPrice(int price) {
        if (price % 9 == 0) return "" + (price / 9);
        return "" + Math.round((100 * price) / 9.0f) / 100f;
    }

    private Text getSaleChanceText(int saleChance) {
        return Text.of(saleChance + "%");
    }

    private Text getSaleFrequencyText(int cooldown) {
        String timeString = cooldown + "s";
        if (cooldown >= 60 && cooldown % 60 == 0) {
            timeString = cooldown / 60 + "m";
        }
        return Text.of(timeString);
    }
}
