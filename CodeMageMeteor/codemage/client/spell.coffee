Template.spell.helpers
  enchantmentsForSpell: ->
    enchantments.find { userId: tomes.findOne(this.tomeId).userId }
  assignedEnchantments: -> enchantments.find { spellIds: this._id }
  isSpellAssignedToEnchantment: (spell) ->
    enchantmentId = this._id
    return enchantments.find({_id:enchantmentId, spellIds: spell._id}).count() != 0

Template.spell.events
  'click .spell-name': (e, t) ->
    newName = prompt("Spell name:", this.name)
    Meteor.call 'updateSpellName', this._id, newName

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


