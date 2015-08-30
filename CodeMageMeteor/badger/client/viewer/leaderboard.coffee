INTERVAL = 10

Session.set("leaderboardIndex", 0)
increment = ->
  nextIndex = parseInt(Session.get "leaderboardIndex") + INTERVAL
  if INTERVAL > find_players().count()
    nextIndex = 0
  Session.set "leaderboardIndex", nextIndex

Meteor.setInterval(increment, 5000)

find_players = ->
  skip = parseInt(Session.get("leaderboardIndex"))
  return Meteor.users.find({}, {sort: {'username', 1}, limit: INTERVAL, skip: skip})


Template.leaderboard.helpers
  player_count: ->
    return Meteor.users.find().count()
  badge_count: ->
    return badgeClasses.find().count()
  badges_earned_count: ->
    return badgeAssertions.find().count()
  organization_count: ->
    return issuerOrganizations.find().count()

  players: ->
    return find_players()

  badge_data: ->
    badges = share.badgesForUser(this)
    badge_data = { badges: badges, count: badges.count() }
    return badge_data
  badge_image: ->
    share.openBadgesUrl 'image', this.image

