Template.group.helpers
  wrapperNameFromId: (id) ->
    console.log id
    return spells.findOne({_id: id}).name

  userNameFromId: (id) ->
    console.log id
    return Meteor.users.findOne({_id: id}).username

  wrapperSpells: ->
    return spells.find {wrapper: true}

  allUsers: ->
  	return Meteor.users.find {}

Template.group.events

  'click .add-wrapper': (e,t) ->
    console.log e
    console.log t
    dropdown = (e.target.parentElement.getElementsByClassName "wrapper-dropdown")[0]
    console.log dropdown.options
    wrapperId = dropdown.options[dropdown.options.selectedIndex].value
    console.log wrapperId
    console.log this._id
    Meteor.call 'addWrapperToGroup', this._id, wrapperId

  'click .remove-wrapper': (e,t) ->
    wrapperId = e.target.classList[1]
    Meteor.call 'removeWrapperFromGroup', t.data._id, wrapperId #"this" doesn't work here for some reason

  'click .add-member': (e,t) ->
    console.log e.target.parentElement.getElementsByClassName "member-dropdown"
    dropdown = (e.target.parentElement.getElementsByClassName "member-dropdown")[0]
    console.log dropdown
    memberId = dropdown.options[dropdown.options.selectedIndex].value
    console.log memberId
    console.log this._id
    Meteor.call 'addMemberToGroup', this._id, memberId

  'click .remove-member': (e,t) ->
    memberId = e.target.classList[1]
    Meteor.call 'removeMemberFromGroup', t.data._id, memberId