addWrapperSpell = (spellName, tomeId, code, userId, priority) ->
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
  addWrapperSpell 'preCode', defaultTome, Assets.getText('codemage/python/preCode.py'), Meteor.users.findOne({username:'admin'})._id, "-2"
  addWrapperSpell 'mpApi', defaultTome, Assets.getText('codemage/python/mpApi.py'), Meteor.users.findOne({username:'admin'})._id, "-3"
  addWrapperSpell 'xpRequirements', defaultTome, Assets.getText('codemage/python/xpRequirements.py'), Meteor.users.findOne({username:'admin'})._id, "-4"
  addWrapperSpell 'ManaGame', defaultTome, Assets.getText('codemage/python/ManaGame.py'), Meteor.users.findOne({username:'admin'})._id, "-1"
  spells.update 'runWithStudentCode',
    $set:
      userId: Meteor.users.findOne({username:'admin'})._id
      tomeId: defaultTome
      code: Assets.getText 'codemage/python/runWithStudentCode.py'
      message: ""
      status: "creating"
      library: false
      wrapper: false
      wrapperEnabled: false
      wrapperText: false
      wrapperDescription: ""
      wrapperPriority: -1
      version: share.codeMageConstants.currentVersion
      namespace: share.codeMageConstants.defaultNamespace
      itemMaterial: share.codeMageConstants.itemMaterials[20]
      action: share.codeMageConstants.actions[0]
  ,
    upsert: true
