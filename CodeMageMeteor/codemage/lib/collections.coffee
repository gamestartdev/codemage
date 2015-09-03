@tomes = new Mongo.Collection "tomes"
@spells = new Mongo.Collection "spells"
@enchantments = new Mongo.Collection "enchantments"

createdAt = (userId, doc) ->
  doc.createdAt = new Date()
  doc.updatedAt = new Date()
updatedAt = (userId, doc, fieldNames, modifier, options) ->
  modifier.$set = modifier.$set or {}
  modifier.$set.updatedAt = new Date()

if Meteor.isServer
  collections = [tomes, spells, enchantments]
  for c in collections
    c.before.insert createdAt
    c.before.update updatedAt