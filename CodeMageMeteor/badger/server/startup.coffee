Meteor.startup ->
  #Need to manually reboot the sever for edits here
  console.log "Badger Module Startup"
  if Meteor.users.find().count() > 0
    return
  Meteor.users.remove {}
  users = [
    { username:"admin", email: "admin@gamestartschool.org", password: "asdf", roles:['admin', 'issuer'], minecraftPlayerId: "GameStartSchool"},
    { username:"gss1", email: "denrei@gamestartschool.org", password: "asdf", roles:['admin', 'issuer'], minecraftPlayerId: "gss1"},
    { username:"gss2", email: "denrei@gamestartschool.org", password: "asdf", roles:['admin', 'issuer'], minecraftPlayerId: "gss2"},
    { username:"gss3", email: "denrei@gamestartschool.org", password: "asdf", roles:['admin', 'issuer'], minecraftPlayerId: "gss3"},

  ]
  for user in users
    userId = Accounts.createUser
      email: user.email
      password: user.password
      username: user.username
    Meteor.users.update userId, {$set: {isAdmin: true, isIssuer: 'issuer' in user.roles, minecraftPlayerId: user.minecraftPlayerId}}