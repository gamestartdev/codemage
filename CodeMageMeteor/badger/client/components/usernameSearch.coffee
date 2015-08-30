Template.usernameSearch.helpers
  users: ->
    usernameSearch = Session.get("usernameSearch")
    if not usernameSearch or usernameSearch.length <= 1
      return []
    query = new RegExp( Session.get("usernameSearch"), 'i' );
    return Meteor.users.find { $or: [ {'username': query}, {'password': query} ] }

Template.usernameSearch.events
  'keyup input.usernameSearchField': (evt) ->
    Session.set("usernameSearch", evt.currentTarget.value);
