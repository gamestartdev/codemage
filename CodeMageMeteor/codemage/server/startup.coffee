addPreprocessSpell = (spellName, tomeId, code) ->
  spells.update spellName,
    $set:
      tomeId: tomeId
      name: spellName
      code: code
      message: ""
      status: "creating"
      preprocess: true
      version: share.codeMageConstants.currentVersion
      namespace: share.codeMageConstants.defaultNamespace
  ,
    upsert: true

Meteor.startup ->
  console.log "CodeMage Module Startup"

  defaultTome = 'GameRules'
  tomes.update defaultTome, {$set:{ name: defaultTome, userId: Meteor.users.findOne({username:'admin'})._id }},  upsert: true
  addPreprocessSpell('preCode', defaultTome, Assets.getText('codemage/python/preCode.py'))
  addPreprocessSpell('mpApi', defaultTome, Assets.getText('codemage/python/mpApi.py'))
  addPreprocessSpell('xpRequirements', defaultTome, Assets.getText('codemage/python/xpRequirements.py'))