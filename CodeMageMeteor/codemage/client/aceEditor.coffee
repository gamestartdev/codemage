Template.aceEditor.onRendered ->
  spell = this.data
  editor = MandrillAce.getInstance();
  editor.ace.setOptions
    maxLines:20000
    minLines:5
    theme: 'ace/theme/monokai'
    mode: 'ace/mode/python'
    showPrintMargin: true
  editor.setValue(spell.code, -1)
  editor.ace.getSession().on 'change', (e) ->
    Meteor.call 'updateSpell', spell._id, {code: editor.value()}

  this.autorun () ->
    spell = Template.currentData()
    editor = MandrillAce.getInstance()
    Range = ace.require("ace/range").Range
    session = editor.ace.getSession()
    if Template.instance().markerID?
      session.removeMarker Template.instance().markerID
    console.log "test2"
    if spell.line != undefined
      range = new Range(spell.line - 1, 0, spell.line - 1, 2)
      markerID = session.addMarker range, "highlight", "fullLine", false
      Template.instance().markerID = markerID