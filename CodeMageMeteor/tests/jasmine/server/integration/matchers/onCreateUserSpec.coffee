describe 'onCreateUser', ->

  beforeEach ->
    Meteor.users.remove({})
    badgeAssertions.remove({})

  it 'updates existing assertions with email', ->
    createBadgeAssertion = ->
      Meteor.call 'createBadgeAssertion', this.userId, "a", "b"
    email = 'nate@gamestartschool.org'

    badgeAssertions.insert { email: email }
    badgeAssertions.insert { email: 'nothis' }
    badgeAssertions.insert { email: 'notthiseither'}
    badgeAssertions.insert { email: email }
    userId = Accounts.createUser
      email: email
    expect(badgeAssertions.find({userId:userId}).count()).toBe(2)
    Meteor.call 'createBadgeAssertion', userId, "a", "b"
    Meteor.call 'createBadgeAssertion', undefined, "a", "b", email
    expect(badgeAssertions.find({userId:userId}).count()).toBe(4)
    Meteor.call 'createBadgeAssertion', undefined, "a", "b"
    expect(badgeAssertions.find({userId:userId}).count()).toBe(4)