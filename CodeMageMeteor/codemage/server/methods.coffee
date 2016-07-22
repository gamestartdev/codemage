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

spellPrint = (print, spellId) ->
  check(print, String)
  check(spellId, String)
  prints = (spells.findOne {_id: spellId}).prints or []
  prints.push print
  spells.update spellId, {$set: {prints: prints}}

clearPrint = (spellId) ->
  check(spellId, String)
  spell = spells.findOne {_id: spellId}
  spells.update spellId, {$set: {prints: []}}

spellException = (stacktrace, spellId) ->
  check(stacktrace, String)
  check(spellId, String)
  if stacktrace.startsWith("Library error:")
    spells.update spellId, {$set: {errorMessage: stacktrace, errorOnly: "Error in library:", line: -1}}
    return
  if stacktrace.startsWith("Wrapper error:")
    spells.update spellId, {$set: {errorMessage: stacktrace, errorOnly: "Internal error:", line: -1}}
    return
  if stacktrace == ""
    spells.update spellId, {$set: {errorMessage: null, errorOnly: null, line: -1}}
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
  spells.update spellId, {$set: {errorMessage: stacktrace, errorOnly: errorOnly, line: lineNumber - 1}} #the one line is def studentCode():, and is injected from Java.

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
    prints: []
    library: false
    wrapper: false
    wrapperEnabled: false
    wrapperDescription: ""
    wrapperPriority: "-1"
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

addGroup = (name) ->
  check(name, String)
  groups.insert
    name: name
    wrappers: []
    groupMembers: []

removeGroup = (id) ->
  check(id, String)
  groups.remove id

addWrapperToGroup = (groupId, wrapperId) ->
  check(groupId, String)
  check(wrapperId, String)
  group = groups.findOne {_id: groupId}
  wrappers = group.wrappers
  if wrappers.indexOf wrapperId == -1
    wrappers.push(wrapperId)
    groups.update groupId, {$set: {wrappers: wrappers}}

removeWrapperFromGroup = (groupId, wrapperId) ->
  check(groupId, String)
  check(wrapperId, String)
  group = groups.findOne {_id: groupId}
  wrappers = group.wrappers
  if wrappers.indexOf wrapperId != -1
    wrappers.splice (wrappers.indexOf wrapperId), 1
    groups.update groupId, {$set: {wrappers: wrappers}}

addMemberToGroup = (groupId, userId) ->
  check(groupId, String)
  check(userId, String)
  group = groups.findOne {_id: groupId}
  groupMembers = group.groupMembers
  if groupMembers.indexOf userId == -1
    groupMembers.push userId
    groups.update groupId, {$set: {groupMembers: groupMembers}}

removeMemberFromGroup = (groupId, userId) ->
  check(groupId, String)
  check(userId, String)
  group = groups.findOne {_id: groupId}
  groupMembers = group.groupMembers
  if groupMembers.indexOf userId != -1
    groupMembers.splice (groupMembers.indexOf userId), 1
    groups.update groupId, {$set: {groupMembers: groupMembers}}



Meteor.methods
  addTome: addTome
  removeTome: removeTome
  updateTomeName: updateTomeName

  addGroup: addGroup
  removeGroup: removeGroup
  addWrapperToGroup: addWrapperToGroup
  removeWrapperFromGroup: removeWrapperFromGroup
  addMemberToGroup: addMemberToGroup
  removeMemberFromGroup: removeMemberFromGroup

  addSpell: addSpell
  updateSpell: updateSpell
  removeSpell: removeSpell
  spellException: spellException
  spellPrint: spellPrint
  clearPrint: clearPrint
  setSpellAction: setSpellAction
  setSpellItemMaterial: setSpellItemMaterial

  updateMinecraftPlayerId: updateMinecraftPlayerId
  serverStatus: serverStatus

