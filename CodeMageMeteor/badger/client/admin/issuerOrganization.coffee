Template.issuerOrganization.rendered = ->
  $(".organization-form").validate
    rules:
      name:
        required: true
      url:
        required: true
      email:
        required: true
        email: true
      description:
        required: false
    messages:
      email: "The email address you have entered is invalid"
      name: "Please enter a name for your organization"
      url: "Full URL including 'http://'"

Template.issuerOrganization.helpers
  isAdmin: -> Meteor.user().isAdmin

Template.issuerOrganization.events
  'submit .organization-form': (e) ->
    organization =
      _id: e.target._id.value
      name: e.target.name.value
      url: e.target.url.value
      email: e.target.email.value
      description: e.target.description.value
      image: ""
    Meteor.call 'createOrganization', organization, share.alertProblem
    Router.go('admin')
    return false

  'click .deleteOrg': ->
    if share.confirm "Perminantly Remove "+this.name + "?"
      console.log "Removing "+this
      Meteor.call "removeOrganization", this._id
      Router.go('admin')
