Template.profile.helpers
  profileUser: ->
    return Router.current().data().profileUser

Template.profileContent.helpers
  badge_data: ->
    user = this
    console.log "BADGE DATA"
    console.log user
    if user
      if user.username is "admin"
        badges = badgeClasses.find {}
      else
        badges = share.badgesForUser(user)
      data =
        badges: badges
        count: badges.count()
        total_badge_count: badgeClasses.find().count()
      return data
