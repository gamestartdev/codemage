Template.enchantment.helpers
  itemMaterials: -> share.codeMageConstants.itemMaterials
  actions: -> share.codeMageConstants.actions
  isItemMaterialSelected: (enchantment) ->
    return enchantment.itemMaterial is String(this)
  isActionSelected: (enchantment) ->
    return enchantment.action is String(this)

Template.enchantment.events
  'input .updateEnchantmentItemMaterial': (e,t) ->
    selectedValue = e.target.value
    Meteor.call 'updateEnchantment', this._id, {itemMaterial: selectedValue}
  'input .updateEnchantmentAction': (e,t) ->
    selectedValue = e.target.value
    Meteor.call 'updateEnchantment', this._id, {action: selectedValue}
  'input .enchantment-name': (e,t) ->
    Meteor.call 'updateEnchantment', this._id, {name: e.target.value}
  'click .addSpellToEnchantment': (e,t) ->
    Meteor.call 'addSpellToEnchantment', this._id, t.data._id
  'click .remove-enchantment':(e,t) ->
    if share.confirm "Delete #{this.name}?"
      Meteor.call 'removeEnchantment', this._id