Template.group.helpers
  wrapperNameFromId: (id) ->
    console.log id
    return spells.findOne({_id: id}).name

  userNameFromId: (id) ->
    console.log id
    return Meteor.users.findOne({_id: id}).username

  isUngrouped: (id) ->
    groupsArray = groups.find({}).fetch()
    groupedIds = []
    for group in groupsArray
      groupedIds.push id for id in group.groupMembers
    return !(id in groupedIds)

  wrapperSpells: ->
    return spells.find {wrapper: true}

  allUsers: ->
  	return Meteor.users.find {}

Template.group.events

  'click .add-wrapper': (e,t) ->
    dropdown = (e.target.parentElement.getElementsByClassName "wrapper-dropdown")[0]
    wrapperId = dropdown.options[dropdown.options.selectedIndex].value
    Meteor.call 'addWrapperToGroup', this._id, wrapperId

  'click .remove-wrapper': (e,t) ->
    wrapperId = e.target.classList[1]
    Meteor.call 'removeWrapperFromGroup', t.data._id, wrapperId #"this" doesn't work here for some reason

  'click .add-member': (e,t) ->
    console.log "add member"
    dropdown = (e.target.parentElement.getElementsByClassName "member-dropdown")[0]
    memberId = dropdown.options[dropdown.options.selectedIndex].value
    Meteor.call 'addMemberToGroup', this._id, memberId

  'click .remove-member': (e,t) ->
    memberId = e.target.classList[1]
    Meteor.call 'removeMemberFromGroup', t.data._id, memberId