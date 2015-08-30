
Router.route('/openbadges/:o/:_id',
  ->
    contentType = 'application/json'
    data = switch @params.o
      when 'issuerOrganization'
        org = issuerOrganizations.findOne {_id: @params._id}
        name: org.name
        url: org.url
      when 'badgeClass'
        badge = badgeClasses.findOne {_id: @params._id}
        name: badge.name
        description: badge.description
        criteria: share.openBadgesUrl 'criteria', badge._id
        image: share.openBadgesUrl 'image', badge.image
        issuer: share.openBadgesUrl 'issuerOrganization', badge.issuer
      when 'criteria'
        badgeClasses.findOne({_id: @params._id})?.criteria
      when 'evidence'
        badgeAssertions.findOne({_id: @params._id})?.evidence
      when 'badgeAssertion'
        assertion = badgeAssertions.findOne {_id: @params._id}
        uid: assertion._id
        issuedOn: assertion.issuedOn.toISOString()
        badge: share.openBadgesUrl 'badgeClass', assertion.badgeId
        evidence: share.openBadgesUrl 'evidence', assertion._id
        verify:
          type: 'hosted'
          url: share.openBadgesUrl 'badgeAssertion', assertion._id
        recipient:
          type: 'email'
          hashed: false
          identity: share.determineEmail Meteor.users.findOne(assertion.userId)
      when 'image'
        contentType = 'image/png'
        image = images.findOne({_id: @params._id})
        new Buffer(image.data.substr(image.data.indexOf(",") + 1), 'base64')

    @response.writeHead(200, { 'Content-Type': contentType })
    @response.end(if data instanceof Buffer then data else JSON.stringify data)
  where: 'server',
)

hashStuff_notusedyet = ->
  salt = CryptoJS.enc.Hex.stringify(CryptoJS.lib.WordArray.random(16))
  id = "sha256$" +
    CryptoJS.enc.Hex.stringify(CryptoJS.SHA256(userData.email + salt))
  identityObjectID = identityObjects.insert({
    identity: id,
    type: "email",
    hashed: true,
    salt: salt
  })
