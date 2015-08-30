Template.email_badge_list.helpers
  isAdmin: ->
    return Meteor.user().username == 'admin'
  users: ->
    return Meteor.users.find {emailed:{$exists : false }}

Template.email_badge_list.events
  'click .sendEmail': ->

    user = this
    dataContext = badges: share.badgesForUser(user)
    html = Blaze.toHTMLWithData Template.badgeEmail, dataContext

    options =
      from: "info@gamestartschool.org"
      to: share.determineEmail(user)
      cc: "badges@gamestartschool.org"
      subject: "Digital Badges Recap for: " + user.username
      text: "Congrats on your badges!  GameStart classes start April 25th, check them out now!  http://www.gamestartschool.org/classes"
      html: html
    Meteor.call 'sendEmail', options

