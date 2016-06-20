Meteor.publish 'tomes', -> return tomes.find {}
Meteor.publish 'spells', -> return spells.find {}