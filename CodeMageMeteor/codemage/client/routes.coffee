Router.route '/user/:_id?',
  name: 'codeMage.user'
  template: 'codeMage'
  data: ->
    user = Meteor.users.findOne {username: @params._id} or Meteor.user()
    return { groups: groups.find({}), user: user, tomes: tomes.find {userId: user?._id} }

Router.route '/tome/:_id?',
  name: 'codeMage.tome'
  template: 'codeMage'
  data: ->
    tome = tomes.findOne @params._id
    user = Meteor.users.findOne tome?.userId
    return { groups: groups.find({}), user: user, tome: tome, tomes: tomes.find {userId: user?._id} }

Router.route '/group/:_id?',
  name: 'codeMage.group'
  template: 'codeMage'
  data: ->
    group = groups.findOne @params._id
    return { group: group, groups: groups.find({}), user: Meteor.user(), users: Meteor.users.find({}), tomes: tomes.find {userId: Meteor.user()._id} }

Router.route '/spell/:_id?',
  name: 'codeMage.spell'
  template: 'codeMage'
  layoutTemplate: 'layoutStretch'
  data: ->
    spell = spells.findOne @params._id
    tome = tomes.findOne(spell?.tomeId)
    user = Meteor.users.findOne(tome?.userId) or Meteor.user()
    return { groups: groups.find({}), user: user, tome: tome, spell: spell, tomes: tomes.find {userId: user?._id} }