Template.award.helpers
  allOrganizations: -> issuerOrganizations.find()
  myOrganizations: -> issuerOrganizations.find({users: Meteor.userId()})
  badgesForOrg: ->
    badges: badgeClasses.find { issuer:  this._id}

Template.award.events
  'click .awardBadge': ->
    Router.go('viewBadge', {_id: this._id })

