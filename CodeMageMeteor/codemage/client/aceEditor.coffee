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
    range = new Range(1, 1, spell.line, 2)
    markerID = session.addMarker range, "highlight", "line", false
    Template.instance().markerID = markerID
    #selection = session.getSelection()
    #selection.moveCursorToPosition {row:spell.line, column:0}
    #selection.selectLine()

#Template.aceEditor.onRendered ->
#  this.autorun ->
#    editor = MandrillAce.getInstance();
#    editor.setValue(Template.currentData().code)