Template.tome.helpers
  spells: -> return spells.find { tomeId: this._id }

Template.tome.events
  'click .tome-name': (e, t) ->
    newName = prompt('Tome name:', this.name)
    if newName
      Meteor.call 'updateTomeName', this._id, newName
  'click .remove-tome':(e,t) ->
    spellCount = spells.find({tomeId: this._id}).count()
    if share.confirm "Delete #{this.name} and it's #{spellCount} spells?"
      username = Meteor.users.findOne(this.userId)?.username
      Meteor.call 'removeTome', this._id, -> Router.go('codeMage.user', {_id: username})
  'click .add-spell': (e,t) ->
    tome = this
    spellName = tome.name + " Spell " + spells.find({tomeId: tome._id}).count()
    Meteor.call 'addSpell', tome._id, tome.userId, spellName, share.codeMageConstants.defaultCode
  'click .spellStatus': (e,t) ->
    Meteor.call 'spellStatus', this._id, !this.status
