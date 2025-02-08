## What is this mod?

Ever wanted to run your own business? Are you tired of having to talk to every individual villager to trade with them? Do you feel like villager trading halls are inhumane and wish there was a better way? Do you want an excuse to just mass produce something and have something fun to do with the results? If so, this mod is for you!

Villager Business allows you to create Sales Stands, a new block where you can put (almost) any item for sale. Villagers will periodically visit your shop and have a chance to give you emeralds in exchange for the item.

Every item has its own price! Villagers are more likely to buy desirable items, and the time between visits to the shop depends on how frequently one would want to buy the item again (food sells frequently, but boats not so much).

This mod is meant for fun, it hasn't been tested to see if its balanced compared to normal villager trading, so it may be very inefficient or too efficient, I don't know. The goal is to have a fun block that lets you have your own store, market or automated item sink and trading.

## How to use

### Sales stand

![Crafting an emerald on top of a barrel makes a Sales Stand](https://cdn.modrinth.com/data/cached_images/a493a5044cb031551d2f614a1fb5dff0ea7d6b89.png)

Craft a Sales Stand and place it somewhere that has villagers nearby. Put the items you want to sell inside of the Sales Stand, and villagers may come to exchange emeralds for that item. You can also half or double the price of the item, which will change the chance of selling, and the time between customer visits.

### Request stand
![Crafting a gold ingot on top of a barrel makes a Request Stand](https://cdn.modrinth.com/data/cached_images/f7e27bf2b25b587df85a762b4b7b9fc954fd0c65.png)

Craft a Request Stand and place it somewhere that has villagers nearby. Set the requested item by placing an item in the Request slot. Load the request stand with emeralds. Villagers will ocationally come by and give you that item in exchange for emeralds. Villagers are unlikely to sell you items in this way, and the items will be more expensive than when you sell them.

### Emerald Nuggets

This mod adds Emerald Nuggets, which are just 1/9th of an emerald, which you can craft like any other nugget.

## Shortcomings

This is my first minecraft mod ever, and I don't have any previous experience with Java or Mixins. I would have loved to add Roughly Enough Items compatibility to the request stand slot, but I failed.

Villagers are forced to move to Sales Stands by repeatedly setting their walk position until they arrive, since I don't understand how Activities work.

Since the config file gets created only if the file doesn't exist already, if I ever update the config file generator, people that where already using the mod will not get the new config unless they delete their current config.

Porting this mod to other fabric versions, or to other mod loaders, is something I don't know how to do. If you know modding, please use the github repository to contribute fixes or improvements, or fork it and make your own version.

## Technical explanation

### Item prices

Item prices are stored in a config file. Prices have been set manually and may not be very balanced or sensible, if you find a price that doesn't make sense please let me know. If a specific item is not in the file, its price will be calculated from crafting recipes, takign into account the price of its ingredients. Anything that has to be gathered from the world, or needs to be created with a method other than crafting (right clicking, smelting, watering, etc.) needs to be added to the list or it wont have a price.

### Customer behavior

Sales Stands will attempt to attract all adult villagers in a 50 block radius every 5 seconds are not working or sleeping, with a 10% chance for each one. The villager will then try to get to the stand, and they will get frustrated if they cant reach it in 3 minutes. Once they arrive to the stand, they will inspect the item and have a chance to purchase it. This chance can be affected by doubling or halving the price of the item. After the villager either buys or rejects the item, they enter a cooldown for that specific item, and they wont be attracted by this item again for a minimum time, depending on the item and the price.
