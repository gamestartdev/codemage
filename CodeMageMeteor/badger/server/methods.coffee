Meteor.methods

  createBadgeClass: (badgeData) ->
    check(badgeData, {name: String, image: String, \
                      origin: Match.Any, issuer: String, description: String, _id: Match.Any,
                      tags: Array, criteria: String})

    console.log "Creating Badge " + badgeData['_id']
    console.log badgeData

    badge =
      name: badgeData.name
      image: images.insert({data: badgeData.image})
      criteria: badgeData.criteria
      issuer: badgeData.issuer
      description: badgeData.description
      alignment: []
      tags: badgeData.tags

    if badgeData._id
      badgeClasses.update badgeData._id, badge
    else
      badgeClasses.insert badge

  removeBadgeClass: (badgeId) ->
    check(badgeId, String)
    badgeAssertions.remove {badgeId: badgeId}
    badgeClasses.remove {_id: badgeId}

  createOrganization: (org) ->
    check(org, {_id: String, name: String, url: String, email: String, description: String, image: Match.Any})
    if share.isAdmin(Meteor.user()) or share.isIssuer(Meteor.user())
      organizationData =
        name: org.name
        url: org.url
        email: org.email
        description: org.description
        image: org.image
        users: [ Meteor.userId() ]
      if org._id
        issuerOrganizations.update org._id, organizationData
      else
        issuerOrganizations.insert organizationData


  removeOrganization: (orgId) ->
    check(orgId, String)
    console.log Meteor.user().username + " is Removing organization "+ orgId
    if share.isAdmin(Meteor.user())
      org = issuerOrganizations.findOne({_id: orgId})
      for badge in badgeClasses.find({issuer: org._id})
        badgeAssertions.remove {badgeId: badge._id}
      badgeClasses.remove {issuer: org._id}
      issuerOrganizations.remove(org)

  toggleIssuerRole: (userId) ->
    check(userId, String)
    if share.isAdmin(Meteor.user())
      user = Meteor.users.findOne userId
      if share.isIssuer(user)
        Meteor.users.update userId, {$pull: {roles: 'issuer'}}
      else
        Meteor.users.update userId, {$addToSet: {roles: 'issuer'}}

  toggleAdminRole: (userId) ->
    check(userId, String)
    if share.isAdmin(Meteor.user())
      console.log "ADMIN"
      user = Meteor.users.findOne userId
      if share.isAdmin(user)
        Meteor.users.update userId, {$pull: {roles: 'admin'}}
      else
        Meteor.users.update userId, {$addToSet: {roles: 'admin'}}

  addUserToOrganization: (userId, orgId) ->
    check(userId, String)
    check(orgId, String)
    if share.isAdmin(Meteor.user()) or (Meteor.userId() in issuerOrganizations.findOne(orgId).users)
      issuerOrganizations.update orgId, { $addToSet: { users: userId } }

  removeUserFromOrganization: (userId, orgId) ->
    check(userId, String)
    check(orgId, String)
    if share.isAdmin(Meteor.user()) or (Meteor.userId() in issuerOrganizations.findOne(orgId).users)
      issuerOrganizations.update {_id: orgId}, { $pull: { users: userId } }

  createBadgeAssertion: (userId, badgeId, evidence, email) ->
    check(userId, Match.Any)
    check(badgeId, String)
    check(evidence, String)
    check(email, Match.Optional(String))

    badgeAssertions.insert
      badgeId: badgeId
      userId: userId or Meteor.users.findOne({ 'emails.address': email })?._id
      issuedOn: new Date()
      evidence: evidence
      email: email

  removeBadgeAssertion: (assertionId) ->
    check(assertionId, String)
    badgeAssertions.remove assertionId

#  removeUser: (userId) ->
#    check(userId, String)
#    user = Meteor.users.findOne {_id: userId}
#    if share.isAdmin(Meteor.user()) and user
#      badgeAssertions.remove {userId: user._id}
#      Meteor.users.remove user

  sendMail: (options) ->
    check options, Object
    if share.isIssuer(Meteor.user())
      console.log "Sending email: " + options.to
      this.unblock()
      process.env.MAIL_URL = 'smtp://postmaster@mailgun.gamestartschool.org:a473788c2caf7849d90165e993b1cb36@smtp.mailgun.org:587'
      Email.send options
