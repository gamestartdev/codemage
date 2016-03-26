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

spellException = (stacktrace, spellId) ->
  check(stacktrace, String)
  check(spellId, String)
  if stacktrace == ""
    spells.update spellId, {$set: {errorMessageLines: null}}
    return
  match = /([^]*?)at org\.python[^]*/g.exec stacktrace
  console.log match
  stacktrace = match[1]
  errorOnly = stacktrace.replace /Traceback[^]*studentCode[^]*?File "<string>", line \d*,/, ""
  stacktrace = stacktrace.split "\n"
  for line in stacktrace
    stacktrace[stacktrace.indexOf(line)] = {line: line}
  console.log stacktrace
  spells.update spellId, {$set: {errorMessageLines: stacktrace}}

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
    preprocess: false
    version: share.codeMageConstants.currentVersion
    namespace: share.codeMageConstants.defaultNamespace

updateSpell = (spellId, data) ->
  check(spellId, String)
  check(data, Object)
  spells.update spellId, {$set:data}

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
  console.log "ID! " + minecraftPlayerId
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
  removeSpell: removeSpell
  spellException: spellException

  addSpellToEnchantment: addSpellToEnchantment
  removeSpellFromEnchantment: removeSpellFromEnchantment

  updateMinecraftPlayerId: updateMinecraftPlayerId
  serverStatus: serverStatus

