
sendMail = (email, badgeName) ->
  options =
    from: "thardy@dptv.org"
    to: email
    cc: "badges@gamestartschool.org"
    subject: badgeName
    text: "Congrats on your badges!  Register or login now at http://badger.gamestartschool.org"
    html: Blaze.toHTMLWithData Template.badgeEmail, {}
  console.log "Sending email to " + email
  Meteor.call 'sendMail', options

Template.viewBadge.events
  'submit .submitManyUsers': (e, t) ->
    e.preventDefault()
    badge = t.data.badge
    evidence = e.target.evidence.value
    emailTextArea = $('.manyUsers')
    emails = share.splitCommas(emailTextArea.val())
    emailTextArea.val('')
    for email in emails
      user = Meteor.users.findOne {"emails.address": email}
      if email
        Meteor.call('createBadgeAssertion', user?._id or undefined, badge._id, evidence, email, share.alertProblem)
        sendMail(email, badge.name)

Template.viewBadge.helpers
  badge: ->
    return Router.current().data().badge
  badge_organization: ->
    return issuerOrganizations.findOne({_id: this.issuer})
  isIssuer: (badge) ->
    orgs = issuerOrganizations.find {_id: badge?.issuer, users: Meteor.userId()}
    return orgs.count() > 0
  badge_image: ->
    share.openBadgesUrl 'image', this.image
  perUserTemplate: ->
    return


Template.toggleBadgeAssertionForUser.helpers
  badge: ->
    return Router.current().data().badge
  userHasBadge: (badge) ->
    user = this
    return share.userHasBadge user, badge

Template.toggleBadgeAssertionForUser.events
  'submit .toggleBadgeAssertion': (e, t) ->
    e.preventDefault()
    user = this
    badge = Router.current().data().badge
    evidence = e.target.evidence.value

    assertion = badgeAssertions.findOne { userId: user?._id, badgeId: badge?._id }
    if assertion?
      Meteor.call 'removeBadgeAssertion', assertion._id, share.alertProblem
    else
      email = share.determineEmail(user)
      Meteor.call 'createBadgeAssertion', user._id, badge._id, evidence, email, share.alertProblem
      sendMail(email, badge.name)