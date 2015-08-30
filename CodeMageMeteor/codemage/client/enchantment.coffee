Template.enchantment.helpers
  bindings: -> share.codeMageConstants.bindings
  triggers: -> share.codeMageConstants.triggers
  isBindingSelected: (enchantment) ->
    return enchantment.binding is String(this)
  isTriggerSelected: (enchantment) ->
    return enchantment.trigger is String(this)

Template.enchantment.events
  'input .updateEnchantmentBinding': (e,t) ->
    selectedValue = e.target.value
    Meteor.call 'updateEnchantment', this._id, {binding: selectedValue}
  'input .updateEnchantmentTrigger': (e,t) ->
    selectedValue = e.target.value
    Meteor.call 'updateEnchantment', this._id, {trigger: selectedValue}
  'input .enchantment-name': (e,t) ->
    Meteor.call 'updateEnchantment', this._id, {name: e.target.value}
  'click .addSpellToEnchantment': (e,t) ->
    Meteor.call 'addSpellToEnchantment', this._id, t.data._id
  'click .remove-enchantment':(e,t) ->
    if share.confirm "Delete #{this.name}?"
      Meteor.call 'removeEnchantment', this._id