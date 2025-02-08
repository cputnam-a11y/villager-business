## What is this mod?

Ever wanted to run your own business? Are you tired of having to talk to every individual villager to trade with them? Do you feel like villager trading halls are inhumane and wish there was a better way? Do you want an excuse to just mass produce something and have something fun to do with the results? If so, this mod is for you!

Villager Business allows you to create Sales Stands, a new block where you can put (almost) any item for sale. Villagers will periodically visit your shop and have a chance to give you emeralds in exchange for the item.

Every item has its own price! Villagers are more likely to buy desirable items, and the time between visits to the shop depends on how frequently one would want to buy the item again (food sells frequently, but boats not so much).

## Shortcomings

This is my first minecraft mod ever, and I don't have any previous experience with Java or Mixins. I would have loved to add Roughly Enough Items compatibility to the request stand slot, but I failed. Also, porting this mod to other fabric versions, or to other mod loaders, is something I don't know how to do. If you know modding, please use the github repository to contribute fixes or improvements, or fork it and make your own version.

## Technical explanation

### Item prices

Item prices are stored in a config file. Prices have been set manually and may not be very balanced or sensible, if you find a price that doesn't make sense please let me know. If a specific item is not in the file, its price will be calculated from crafting recipes, takign into account the price of its ingredients. Anything that has to be gathered from the world, or needs to be created with a method other than crafting (right clicking, smelting, watering, etc.) needs to be added to the list or it wont have a price.

### Customer behavior

Sales Stands will attempt to attract all adult villagers in a 50 block radius every 5 seconds are not working or sleeping, with a 10% chance for each one. The villager will then try to get to the stand, and they will get frustrated if they cant reach it in 3 minutes. Once they arrive to the stand, they will inspect the item and have a chance to purchase it. This chance can be affected by doubling or halving the price of the item. After the villager either buys or rejects the item, they enter a cooldown for that specific item, and they wont be attracted by this item again for a minimum time, depending on the item and the price.