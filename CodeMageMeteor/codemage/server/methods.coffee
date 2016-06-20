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

setSpellItemMaterial = (spellId, material) ->
  check(spellId, String)
  check(material, String)
  spells.update spellId, {$set: {itemMaterial: material}}

setSpellAction = (spellId, action) ->
  check(spellId, String)
  check(action, String)
  spells.update spellId, {$set: {action: action}}

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

addSpell = (tomeId, userId, name, code) ->
  check(tomeId, String)
  check(name, String)
  check(code, String)
  check(userId, String)
  spells.insert
    itemMaterial: share.codeMageConstants.itemMaterials[0]
    action: share.codeMageConstants.actions[0]
    tomeId: tomeId
    userId: userId
    name: name
    code: code
    message: ""
    status: "creating"
    preprocess: false
    preprocessPriority: -1
    version: share.codeMageConstants.currentVersion
    namespace: share.codeMageConstants.defaultNamespace

updateSpell = (spellId, data) ->
  check(spellId, String)
  check(data, Object)
  spells.update spellId, {$set:data}

removeSpell = (spellId) ->
  check(spellId, String)
  console.log "Removing spell: " + spellId
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

  addSpell: addSpell
  updateSpell: updateSpell
  removeSpell: removeSpell
  spellException: spellException
  setSpellAction: setSpellAction
  setSpellItemMaterial: setSpellItemMaterial

  updateMinecraftPlayerId: updateMinecraftPlayerId
  serverStatus: serverStatus

