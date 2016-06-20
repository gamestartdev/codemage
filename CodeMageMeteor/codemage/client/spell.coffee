Template.spell.helpers
  itemMaterials: -> share.codeMageConstants.itemMaterials
  actions: -> share.codeMageConstants.actions
  isCurrentItemMaterial: (spell) ->
    return spell.itemMaterial is String(this)
  isCurrentAction: (spell) ->
    return spell.action is String(this)
  lineNumber: ->
    if this.line < 0
      return ""
    else
      return "at line " + this.line

Template.spell.events

  'input .itemMaterialSelector': (e,t) ->
    Meteor.call 'setSpellItemMaterial', this._id, e.target.value

  'input .actionSelector': (e,t) ->
    Meteor.call 'setSpellAction', this._id, e.target.value

  'click .togglePreprocess': (e,t) ->
    preprocess = spells.findOne(this._id).preprocess
    console.log preprocess
    Meteor.call 'updateSpell', this._id, {preprocess: !preprocess}

  'click .spell-name': (e, t) ->
    newName = prompt("Spell name:", this.name) or this.name
    Meteor.call 'updateSpell', this._id, {name: newName}

  'click .spellStatus': (e,t) ->
    Meteor.call 'spellStatus', this._id, "executeRequest"

  'click .remove-spell': (e,t) ->
    if share.confirm("Are you sure you want to delete this spell?")
      tomeId = this.tomeId
      Meteor.call 'removeSpell', this._id, ->
        Router.go('codeMage.tome', {_id: tomeId} )


