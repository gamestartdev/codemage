Meteor.publish 'tomes', -> return tomes.find {}
Meteor.publish 'spells', -> return spells.find {}
Meteor.publish 'enchantments', -> return enchantments.find {}