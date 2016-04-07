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
    spells.update spellId, {$set: {errorMessage: null, errorOnly: null}}
    return
  match = /([^]*?)at org\.python[^]*/g.exec stacktrace
  stacktrace = match[1]
  #errorOnly = stacktrace.replace /Traceback[^]*studentCode[^]*?(File "<string>", line \d*,)*/, ""
  errorOnly = stacktrace.replace /[^]*line \d*, /, ""
  errorOnly = errorOnly.replace /in trace_function[\r\n]*Exception: |in sleep[\r\n]*Exception: /, ""
  lineNumber = 0
  if errorOnly.indexOf("SyntaxError") != -1
    match = /SyntaxError: \(['"]([^]*)['"], \('<string>',[^]*\)\)/.exec errorOnly
    errorOnly = "SyntaxError: " + match[1].replace /\\'/g, ""
    errorOnly = errorOnly.replace /'''/g, "'"
    match = /'<string>', (\d*)/.exec stacktrace
    lineNumber = (parseInt match[1])
  else
    match = /[^]* line (\d*), /.exec stacktrace
    lineNumber = parseInt match[1]
  preprocessSpells = spells.find( {preprocess:true} ).fetch()
  preprocSpellsLength = 0
  for spell in preprocessSpells
    preprocSpellsLength += (spell.code.split /\n/g).length
  
  spells.update spellId, {$set: {errorMessage: stacktrace, errorOnly: errorOnly, line: lineNumber - preprocSpellsLength - 1}} #the one line is def studentCode():, and is injected from Java.

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

