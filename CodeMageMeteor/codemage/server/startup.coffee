addLibrarySpell = (spellName, tomeId, code, userId, preprocessPriority) ->
  spells.update spellName,
    $set:
      userId: userId
      tomeId: tomeId
      name: spellName
      code: code
      message: ""
      status: "creating"
      library: true
      preprocessPriority: preprocessPriority
      version: share.codeMageConstants.currentVersion
      namespace: share.codeMageConstants.defaultNamespace
      itemMaterial: null
      action: null
  ,
    upsert: true

Meteor.startup ->
  console.log "CodeMage Module Startup"

  defaultTome = 'GameRules'
  tomes.update defaultTome, {$set:{ name: defaultTome, userId: Meteor.users.findOne({username:'admin'})._id }},  upsert: true
  addLibrarySpell('preCode', defaultTome, Assets.getText('codemage/python/preCode.py'), Meteor.users.findOne({username:'admin'})._id, 2)
  addLibrarySpell('mpApi', defaultTome, Assets.getText('codemage/python/mpApi.py'), Meteor.users.findOne({username:'admin'})._id, 0)
  addLibrarySpell('xpRequirements', defaultTome, Assets.getText('codemage/python/xpRequirements.py'), Meteor.users.findOne({username:'admin'})._id, 1)