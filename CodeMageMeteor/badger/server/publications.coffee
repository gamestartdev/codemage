Meteor.publish 'users', ->
  return Meteor.users.find {}, {fields: {username:1, role:1, email:1, minecraftPlayerId:1} }

Meteor.publish 'issuerOrganizations', ->
  return issuerOrganizations.find()

Meteor.publish 'badgeClasses', ->
  return badgeClasses.find()

Meteor.publish 'badgeAssertions', ->
  return badgeAssertions.find()