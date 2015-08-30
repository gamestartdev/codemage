// A dumb hack to get Foundation's JS features to work.
// https://github.com/ewall/meteor-foundation/issues/6

$(function() {
    $(document).foundation();
});

Meteor.startup(function(){
    var reflow = _.debounce(function() {
        Tracker.afterFlush(function() {
            $(document).foundation('reflow');
        });
    }, 100);

    for (var property in Template) {
        if (Blaze.isTemplate(Template[property])) {
            Template[property].onRendered(reflow);
        }
    }
});