Template.index.helpers
  tomes: -> tomes.find { userId: Meteor.userId() }