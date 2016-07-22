Template.group.helpers
  wrapperNameFromId: (id) ->
    return spells.findOne({_id: id}).name

  userNameFromId: (id) ->
    return Meteor.users.findOne({_id: id}).name

  wrapperSpells: ->
    return spells.find {wrapper: true}



Template.group.events

  'click .add-wrapper': (e,t) ->
