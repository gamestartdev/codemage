Template.aceEditor.onRendered ->
  spell = this.data
  editor = MandrillAce.getInstance();
  editor.ace.setOptions
    maxLines:200
    minLines:5
    theme: 'ace/theme/monokai'
    mode: 'ace/mode/python'
    showPrintMargin: true
  editor.setValue(spell.code, -1)
  editor.ace.getSession().on 'change', (e) ->
    Meteor.call 'updateSpell', spell._id, {code: editor.value()}

#Template.aceEditor.onRendered ->
#  this.autorun ->
#    editor = MandrillAce.getInstance();
#    editor.setValue(Template.currentData().code)