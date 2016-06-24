addLibrarySpell = (spellName, tomeId, code, userId, priority) ->
  spells.update spellName,
    $set:
      userId: userId
      tomeId: tomeId
      name: spellName
      code: code
      message: ""
      status: "creating"
      library: false
      wrapper: true
      wrapperEnabled: true
      wrapperText: false
      wrapperDescription: ""
      wrapperPriority: priority
      version: share.codeMageConstants.currentVersion
      namespace: share.codeMageConstants.defaultNamespace
      itemMaterial: share.codeMageConstants.itemMaterials[20]
      action: share.codeMageConstants.actions[0]
  ,
    upsert: true

Meteor.startup ->
  console.log "CodeMage Module Startup"

  defaultTome = 'GameRules'
  tomes.update defaultTome, {$set:{ name: defaultTome, userId: Meteor.users.findOne({username:'admin'})._id }},  upsert: true
  addLibrarySpell('preCode', defaultTome, Assets.getText('codemage/python/preCode.py'), Meteor.users.findOne({username:'admin'})._id, "-2")
  addLibrarySpell('mpApi', defaultTome, Assets.getText('codemage/python/mpApi.py'), Meteor.users.findOne({username:'admin'})._id, "-3")
  addLibrarySpell('xpRequirements', defaultTome, Assets.getText('codemage/python/xpRequirements.py'), Meteor.users.findOne({username:'admin'})._id, "-4")
  addLibrarySpell('ManaGame', defaultTome, Assets.getText('codemage/python/ManaGame.py'), Meteor.users.findOne({username:'admin'})._id, "-1")