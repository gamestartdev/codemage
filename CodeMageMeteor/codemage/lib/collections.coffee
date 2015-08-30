@tomes = new Mongo.Collection "tomes"
@spells = new Mongo.Collection "spells"
@enchantments = new Mongo.Collection "enchantments"

if Meteor.isServer
  tomes.before.insert (userId, doc) ->
    doc.createdAt = new Date()
  tomes.before.update (userId, doc, fieldNames, modifier, options) ->
    modifier.$set = modifier.$set or {}
    modifier.$set.updatedAt = new Date()
