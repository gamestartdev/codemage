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
    spellId = this._id
    enchantment = enchantments.findOne {spellIds: spell._id}
    Meteor.call "removeSpellFromEnchantment", spellId, enchantment._id
    if !enchantments.findOne

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


