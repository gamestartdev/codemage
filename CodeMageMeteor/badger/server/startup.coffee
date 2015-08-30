Meteor.startup ->
  #Need to manually reboot the sever for edits here
  console.log "Main Server Startup"
  if Meteor.users.find().count() > 0
    return
  Meteor.users.remove {}
  users = [
    { username:"admin", email: "admin@gamestartschool.org", password: "asdf", roles:['admin', 'issuer'], minecraftPlayerId: "GameStartSchool"},
    { username:"denrei", email: "denrei@gamestartschool.org", password: "asdf", roles:['admin', 'issuer'], minecraftPlayerId: "denrei"},
  ]
  for user in users
    userId = Accounts.createUser
      email: user.email
      password: user.password
      username: user.username
    Meteor.users.update userId, {$set: {isAdmin: true, isIssuer: 'issuer' in user.roles, minecraftPlayerId: user.minecraftPlayerId}}