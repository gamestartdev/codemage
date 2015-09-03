addTome = (userId, name) ->
  check(userId, String)
  check(name, String)
  tomes.insert
    userId: userId
    name: name

removeTome = (tomeId) ->
  check(tomeId, String)
  console.log "Removing tome: " + tomeId
  for spell in spells.find({tomeId: tomeId}).fetch()
    removeSpell(spell._id)
  tomes.remove tomeId

updateTomeName = (tomeId, tomeName) ->
  check(tomeId, String)
  check(tomeName, String)
  tomes.update tomeId, {$set: {name: tomeName}}

addEnchantment = (userId, name, itemMaterial, action, spellIds) ->
  check(userId, String)
  check(name, String)
  check(itemMaterial, String)
  check(action, String)
  check(spellIds, Match.Optional(Array))
  enchantments.insert
    userId: userId
    name: name
    itemMaterial: itemMaterial
    action: action
    spellIds: spellIds? or []
    code: "from codemage import *"
    version: share.codeMageConstants.currentVersion
    namespace: share.codeMageConstants.defaultNamespace

removeEnchantment = (enchantmentId) ->
  check(enchantmentId, String)
  console.log "Removing enchantment: " + enchantmentId
  enchantments.remove enchantmentId

updateEnchantment = (enchantmentId, data) ->
  check(enchantmentId, String)
  check(data, Object)
  console.log data
  enchantments.update enchantmentId, {$set: data }

addSpellToEnchantment = (spellId, enchantmentId) ->
  check(spellId, String)
  check(enchantmentId, String)
  enchantments.update enchantmentId, { $addToSet: { spellIds: spellId }}

removeSpellFromEnchantment = (spellId, enchantmentId) ->
  check(spellId, String)
  check(enchantmentId, String)
  enchantments.update enchantmentId, { $pull: { spellIds: spellId }}

addSpell = (tomeId, name, code) ->
  check(tomeId, String)
  check(name, String)
  check(code, String)
  spells.insert
    tomeId: tomeId
    name: name
    code: code
    message: ""
    status: "creating"
    version: share.codeMageConstants.currentVersion
    namespace: share.codeMageConstants.defaultNamespace

updateSpell = (spellId, code) ->
  check(spellId, String)
  check(code, String)
  spells.update spellId, {$set:{code: code, status: false}}

updateSpellName = (spellId, spellName) ->
  check(spellId, String)
  check(spellName, String)
  spells.update spellId, {$set: {name: spellName}}

spellMessage = (spellId, message) ->
  check(spellId, String)
  check(message, String)
  spells.update spellId, {$set:{message: message}}

spellStatus = (spellId, status) ->
  check(spellId, String)
  check(status, Boolean)
  console.log "SpellStatus " + spellId + " " + status
  spells.update spellId, {$set:{status: status}}

removeSpell = (spellId) ->
  check(spellId, String)
  console.log "Removing spell: " + spellId
  enchantments.update {}, { $pull: { spellIds: spellId }}, { multi: true}
  spells.remove spellId

serverStatus = (codeMageServerIp, codeMageServerPort, status) ->
  check(codeMageServerIp, String)
  check(codeMageServerPort, String)
  check(status, String)
  console.log "User: #{Meteor.user()?.username} has a server at #{codeMageServerIp}:#{codeMageServerPort}. Status: #{status}"

updateMinecraftPlayerId = (minecraftPlayerId) ->
  check(minecraftPlayerId, String)
  Meteor.users.update Meteor.userId(), {$set: {'minecraftPlayerId': minecraftPlayerId}}

Meteor.methods
  addTome: addTome
  removeTome: removeTome
  updateTomeName: updateTomeName

  addEnchantment: addEnchantment
  removeEnchantment: removeEnchantment
  updateEnchantment: updateEnchantment

  addSpell: addSpell
  updateSpell: updateSpell
  updateSpellName: updateSpellName
  removeSpell: removeSpell
  spellMessage: spellMessage
  spellStatus: spellStatus

  addSpellToEnchantment: addSpellToEnchantment
  removeSpellFromEnchantment: removeSpellFromEnchantment

  updateMinecraftPlayerId: updateMinecraftPlayerId
  serverStatus: serverStatus

