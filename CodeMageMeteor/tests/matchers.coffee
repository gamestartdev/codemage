# tail -f /Users/thedenrei/Badger/.meteor/local/log/jasmine-server-integration.log

beforeEach ->
  jasmine.addMatchers
    collectionCountEquals: ->
      return {
      compare: (actual, expected) ->
        return { pass: actual.find().count() == expected }
      }