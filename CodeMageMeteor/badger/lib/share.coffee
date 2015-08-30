share.splitCommas = (s)->
  return (v for v in s.replace(',', ' ').split(' ') when v)

share.userHasBadge = (user, badge) ->
  badgeAssertions.find(
    badgeId: badge?._id
    userId: user?._id
  ).count() != 0

share.confirm = (s) ->
  return window.confirm s

share.isAdmin = (user) ->
  return user?.isAdmin
share.isIssuer = (user) ->
  return user?.isIssuer

share.badgesForOrgs = (user) ->
  orgIds = _.pluck(issuerOrganizations.find({users: user._id}).fetch(), '_id')
  return badgeClasses.find {issuer: { $in: orgIds}}

share.badgesForUser = (user) ->
  assertions = badgeAssertions.find({userId: user?._id}).fetch()
  earned_badge_ids = _.pluck(assertions, 'badgeId')
  return badgeClasses.find {_id: { $in: earned_badge_ids} }

share.determineEmail = (user)->
  console.log user
  if user.emails?
    return user.emails[0].address

  else if user.services
    services = user.services
    emailAddress = switch
      when services.persona then services.persona.email
      when services.facebook then services.facebook.email
      when services.github then services.github.email
      when services.google then services.google.email
      when services.twitter then null
      else null
  else
    null

share.openBadgesUrl = (m, id) ->
  path = 'openbadges/' + m + '/' + if typeof id is 'string' then id else id?['_id']
  return Meteor.absoluteUrl path, {replaceLocalhost:true}


share.alertProblem = (error, response) ->
  if error
    alert(error.reason)
  else
    if response.error
      alert(response.error)