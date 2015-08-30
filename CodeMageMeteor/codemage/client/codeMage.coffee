Template.codeMage.helpers
  enchantmentsForUser: (user) ->
    return enchantments.find {userId: user._id}

Template.codeMage.events
  'click .minecraft-id-input': (e, t) ->
    newName = prompt('GameStart Player Id:', Meteor.user()?.minecraftPlayerId)
    Meteor.call 'updateMinecraftPlayerId', newName
  'click .add-tome': (e,t) ->
    user = t.data.user
    tomeName = user?.username + " Tome " + tomes.find({userId: user._id}).count()
    Meteor.call 'addTome', user._id, tomeName, (error, newTomeId) -> Router.go('codeMage.tome', tomes.findOne(newTomeId))
  'click .add-enchantment': (e,t) ->
    user = t.data.user
    enchantmentName = user.username + "'s New Enchantment"
    Meteor.call 'addEnchantment', user._id, enchantmentName, "wooden_sword", "primary"