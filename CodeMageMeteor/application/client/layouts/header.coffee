Template.header.helpers
  header_text: ->
    if Meteor.user() and Meteor.user().username
      Meteor.user().username
    else
      "GameStart CodeMage Alpha v0.0.1"

Template.header.events
  'click .logout': (e,t) ->
    Meteor.logout (error)->
      alert error.reason if error
      Router.go "/"
