Template.spell.helpers
  itemMaterials: -> share.codeMageConstants.itemMaterials
  actions: -> share.codeMageConstants.actions
  enchantmentsForSpell: ->
    enchantments.find { userId: tomes.findOne(this.tomeId).userId }
  assignedEnchantments: -> enchantments.find { spellIds: this._id }
  isSpellAssignedToEnchantment: (spell) ->
    enchantmentId = this._id
    return enchantments.find({_id:enchantmentId, spellIds: spell._id}).count() != 0
  isCurrentItemMaterial: (spell) ->
    return enchantments.findOne({spellIds: spell._id}).itemMaterial is String(this)
  isCurrentAction: (spell) ->
    return enchantments.findOne({spellIds: spell._id}).action is String(this)
  lineNumber: ->
    if this.line < 0
      return ""
    else
      return "at line " + this.line

Template.spell.events

  'input .itemMaterialSelector': (e,t) ->
    Meteor.call 'setSpellItemMaterial', this._id, e.target.value
    ###userId = (tomes.findOne this.tomeId).userId
    enchantment = enchantments.findOne {userId: userId}
    console.log enchantment
    if enchantment
      Meteor.call "removeSpellFromEnchantment", this._id, enchantment._id
    else
      enchantment = {action: share.codeMageConstants.actions[0]}
    if !enchantments.findOne {itemMaterial: e.target.value, action: enchantment.action}
      Meteor.call 'addEnchantment', userId, "meta", e.target.value, enchantment.action, [this._id]
    #Ignore WebStorm saying there's an error, it works fine.
    else
      console.log enchantments.findOne {itemMaterial: e.target.value, action: enchantment.action}
      Meteor.call 'addSpellToEnchantment', this._id, (enchantments.findOne {itemMaterial: e.target.value, action: enchantment.action})._id###

  'input .actionSelector': (e,t) ->
    Meteor.call 'setSpellAction', this._id, e.target.value
    ###userId = (tomes.findOne this.tomeId).userId
    enchantment = enchantments.findOne {userId: userId}
    console.log enchantment
    if enchantment
      Meteor.call "removeSpellFromEnchantment", this._id, enchantment._id
    else
      enchantment = {itemMaterials: share.codeMageConstants.itemMaterials[0]}
    if !enchantments.findOne {itemMaterial: enchantment.itemMaterial, action: e.target.value}
      Meteor.call 'addEnchantment', userId, "meta", enchantment.itemMaterial, e.target.value, [this._id]
#Ignore WebStorm saying there's an error, it works fine.
    else
      Meteor.call 'addSpellToEnchantment', this._id, (enchantments.findOne {itemMaterial: enchantment.itemMaterial, action: e.target.value})._id###

  'click .togglePreprocess': (e,t) ->
    preprocess = spells.findOne(this._id).preprocess
    console.log preprocess
    Meteor.call 'updateSpell', this._id, {preprocess: !preprocess}

  'click .spell-name': (e, t) ->
    newName = prompt("Spell name:", this.name) or this.name
    Meteor.call 'updateSpell', this._id, {name: newName}

  'click .addSpellToEnchantment': (e,t) ->
    Meteor.call 'addSpellToEnchantment', t.data._id, this._id
  'click .removeSpellFromEnchantment': (e,t) ->
    Meteor.call 'removeSpellFromEnchantment', t.data._id, this._id

  'click .addSpellToEnchantment' : (e,t) ->
    spellId = t.data._id
    enchantmentId = this._id
    Meteor.call "addSpellToEnchantment", spellId, enchantmentId

  'click .removeSpellFromEnchantment' : (e,t) ->
    spellId = t.data._id
    enchantmentId = this._id
    Meteor.call "removeSpellFromEnchantment", spellId, enchantmentId

  'click .spellStatus': (e,t) ->
    Meteor.call 'spellStatus', this._id, "executeRequest"

  'click .remove-spell': (e,t) ->
    if share.confirm("Are you sure you want to delete this spell?")
      tomeId = this.tomeId
      Meteor.call 'removeSpell', this._id, ->
        Router.go('codeMage.tome', {_id: tomeId} )


