Template.badge_builder.helpers
  badge: ->
    return Router.current().data()
  organization: ->
    issuerOrganizations.findOne this.issuer

Template.badge_builder.events
  'click .removeBadgeClass': ->
    if window.confirm "Perminantly Remove "+this.name + "?"
      console.log "Removing "+this
      Meteor.call "removeBadgeClass", this._id
      Router.go('/admin')

  'click .useBadgeBuilder': (e,t) ->
    openDesigner = ->
      URL = 'https://www.openbadges.me/designer.html?origin=' + Meteor.absoluteUrl()
      URL = URL+'&email=badges@gamestartschool.org'
      URL = URL+'&close=true&format=json'
      options = 'width=400,height=400,location=0,menubar=0,status=0,toolbar=0'
      designerWindow = window.open(URL,'',options)
    openDesigner()

  'change #cam': (e) ->
    f = e.target.files[0]
    reader = new FileReader()
    reader.onload = (e) ->
      imageURI = e.target.result
      drawToCanvasAsResizedPng(imageURI)
    reader.readAsDataURL(f)

  'click .cameraSubmit': (e) ->
    commitBadge($(e.target).attr('data-_id'))

Template.badge_builder.rendered = ->
  window.onmessage = (e) ->
    if e.origin == 'https://www.openbadges.me'
      imageURI = JSON.parse(e.data).image
      drawToCanvasAsResizedPng(imageURI)

  badge = Router.current().data()
  drawToCanvasAsResizedPng('/openbadges/image/'+ badge.image) if badge.image?

commitBadge = (_id) ->
  canvas = $("#badgeCanvas")[0]
  imageFromCanvas = canvas.toDataURL("image/png")

  badgeData =
    image: imageFromCanvas
    origin: Meteor.absoluteUrl()
    name: $("#badgename").val()
    description: $("#badge-description").val()
    issuer: $("#selectedOrganization").val()
    tags: share.splitCommas($("#badge-tags").val())
    criteria: $("#badge-criteria").val()
    _id: _id or false

  if badgeData.name and badgeData.description and badgeData.issuer and badgeData.image
    cameraSubmitButtons = $('.cameraSubmit')
    cameraSubmitButtons.css('background-color', 'darkgrey')
    cameraSubmitButtons.html('Submitting...')
    #cameraSubmitButtons.toggleClass( "cameraSubmit" )


    Meteor.call "createBadgeClass", badgeData, (error, reason) ->
      if error
        alert error
      else
        Router.go('/admin')
  else
    alert('Please enter a name and provide a description.')

drawToCanvasAsResizedPng = (imageURI) ->
  image = new Image()
  image.onload = ->
    canvas = $("#badgeCanvas")[0]
    canvas.className += ' ready'
    canvas.width = 300
    canvas.height = 300
    ctx = canvas.getContext("2d")
    exif = EXIF.getData(image, ->
      imageData = this
      orientation = EXIF.getTag(imageData, "Orientation")
      switch orientation
        when 6
          ctx.rotate(90*Math.PI/180)
          ctx.translate(0,-canvas.height)
        when 3
          ctx.rotate(180*Math.PI/180)
          ctx.translate(-canvas.width,-canvas.height)
        when 8
          ctx.rotate(-90*Math.PI/180)
          ctx.translate(-canvas.width,0)
    )
    ctx.drawImage(image, 0, 0, canvas.width, canvas.height)
  image.src = imageURI
