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
  isChecked: ->
    if this.wrapperEnabled
      return "checked"
    else
      return ""
  gameWrapperText: ->
    text = ""
    wrappers = spells.find({wrapper: true}).fetch()
    wrapperTexts = ""
    wrapperTexts += spell.wrapperDescription for spell in wrappers when spell.wrapperDescription isnt "" #yay, coffeescript!

Template.spell.events

  'input .itemMaterialSelector': (e,t) ->
    Meteor.call 'setSpellItemMaterial', this._id, e.target.value

  'input .actionSelector': (e,t) ->
    Meteor.call 'setSpellAction', this._id, e.target.value

  'click .toggleGameWrapper': (e,t) ->
    wrapper = spells.findOne(this._id).wrapper
    console.log wrapper
    Meteor.call 'updateSpell', this._id, {wrapper: !wrapper}

  'click .wrapperEnabled': (e,t) ->
    enabled = spells.findOne(this._id).wrapperEnabled
    Meteor.call 'updateSpell', this._id, {wrapperEnabled: !enabled}

  'input .wrapperDesc': (e,t) ->
    Meteor.call 'updateSpell', this._id, {wrapperDescription: e.target.value}

  'input .wrapperPriority': (e,t) ->
    Meteor.call 'updateSpell', this._id, {wrapperPriority: e.target.value}

  'click .toggleLibrary': (e,t) ->
    library = spells.findOne(this._id).library
    Meteor.call 'updateSpell', this._id, {library: !library}

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