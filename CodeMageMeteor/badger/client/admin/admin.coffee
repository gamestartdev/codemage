Template.admin.helpers
  isAdmin: -> share.isAdmin(Meteor.user())
  myOrganizations: ->
    if Meteor.user()?.isAdmin
      return issuerOrganizations.find()
    return issuerOrganizations.find({users: Meteor.userId()})

Template.admin_organization.helpers
  badgesForOrg: ->
    return badgeClasses.find { issuer:  this._id}
  usersForOrg: ->
    return Meteor.users.find { _id: {$in: this.users }}
  badge_image: -> share.openBadgesUrl 'image', this.image
  isAdmin: -> share.isAdmin(Meteor.user())

Template.admin_organization.events
  'click .createBadgeClass': ->
    console.log this
    Router.go 'create', {}, { query: { issuer: this._id } }
  'click .editBadge': ->
    Router.go 'create', { badgeId: this._id }
  'click .removeUserFromOrganization': (e,t) ->
    user = this
    organization = t.data
    if share.confirm "Remove #{user.username} from #{organization.name}?"
      Meteor.call "removeUserFromOrganization", user._id, organization._id, share.alertProblem

Template.addUserToOrganizationRow.events
  'submit .addUserToOrganizationRow': (e, t) ->
    e.preventDefault()
    user = this
    organization = Template.parentData(6) #Yikes.
    Meteor.call "addUserToOrganization", user._id, organization._id, share.alertProblem