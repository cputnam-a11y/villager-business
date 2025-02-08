package net.marum.villagebusiness.pricing;

import net.minecraft.item.Item;

public class ItemPrice {
    private final Item item;
    private final int sellAmount;
    private final int price;
    private final int saleChance;
    private final int requestChance;
    private final int cooldown;

    public ItemPrice(Item item, int price) {
        this.item = item;
        this.price = price;
        this.sellAmount = 1;
        this.saleChance = 50;
        this.requestChance = 10;
        this.cooldown = 120;
    }

    public ItemPrice(Item item, int price, int sellAmount) {
        this.item = item;
        this.sellAmount = sellAmount;
        this.price = price;
        this.saleChance = 50;
        this.requestChance = 10;
        this.cooldown = 120;
    }

    public ItemPrice(Item item, int price, int sellAmount, int saleChance) {
        this.item = item;
        this.sellAmount = sellAmount;
        this.price = price;
        this.saleChance = saleChance;
        this.requestChance = 10;
        this.cooldown = 120;
    }

    public ItemPrice(Item item, int price, int sellAmount,  int saleChance, int requestChance) {
        this.item = item;
        this.sellAmount = sellAmount;
        this.price = price;
        this.saleChance = saleChance;
        this.requestChance = requestChance;
        this.cooldown = 120;
    }

    public ItemPrice(Item item, int price, int sellAmount,  int saleChance, int requestChance, int cooldown) {
        this.item = item;
        this.sellAmount = sellAmount;
        this.price = price;
        this.saleChance = saleChance;
        this.requestChance = requestChance;
        this.cooldown = cooldown;
    }

    public int getSellAmount(int priceSetting) {
        if (priceSetting == 0) {
            if (price < 72 && sellAmount <= item.getMaxCount()/2) {
                return sellAmount*2;
            }
        }
        return sellAmount;
    }

    
    public int getPrice(int priceSetting) {
        switch (priceSetting) {
            case 0:
                if (price < 72 && sellAmount <= item.getMaxCount()/2) {
                    return price;
                }
                return price/2;
            case 1:
                return price;
            case 2:
                return price*2;     
        }
        return price;
    }
    
    
    public int getCooldown(int priceSetting) {
        switch (priceSetting) {
            case 0:
                return (int)Math.ceil(cooldown/2);
            case 1:
                return cooldown;
            case 2:
                return cooldown*4;     
        }
        return cooldown;
    }

    
    public int getSaleChance(int priceSetting) {
        switch (priceSetting) {
            case 0:
                return Math.round((100+saleChance*2)/3);
            case 1:
                return saleChance;
            case 2:
                return saleChance/4;     
        }
        return saleChance;
    }

    public int getRequestChance() {
        return requestChance;
    }
}
