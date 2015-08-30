Meteor.subscribe 'users'
Meteor.subscribe 'issuerOrganizations'
Meteor.subscribe 'badgeClasses'
Meteor.subscribe 'badgeAssertions'

Router.route 'profile',
  path: '/profile/:username'
  data: ->
    return {
      profileUser: Meteor.users.findOne {username: @params.username}
    }

Router.route 'create',
  path: '/create/:badgeId?'
  data: ->
    return badgeClasses.findOne({_id: @params.badgeId}) or @params.query

Router.route 'leaderboard',
  path: '/leaderboard'
  layoutTemplate: 'layoutLeaderboard',

Router.route 'viewBadge',
  path: '/viewBadge/:_id'
  data: ->
    return {
      badge: badgeClasses.findOne({_id: @params._id})
    }
  onBeforeAction: ->
    Session.set 'usernameSearch', ''
    @next()

Router.route 'issuerOrganization',
  path: '/issuerOrganization/:_id?'
  data: ->
    return issuerOrganizations.findOne {_id: @params._id}

Router.route 'admin',
  path: '/admin'

Router.route 'award',
  path: '/award'

Router.route 'email_badge_list',
  path: '/email_badge_list'
