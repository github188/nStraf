
jQuery.fn.contextPopup = function(menuData) {
	var settings = {
		contextMenuClass: 'contextMenuPlugin',
		gutterLineClass: 'gutterLine',
		headerClass: 'header',
		seperatorClass: 'divider',
		title: '',
		items: []
	};
	
	// merge them
	$.extend(settings, menuData);

  function createMenu(e) {
    var menu = $('<ul class="' + settings.contextMenuClass + '"><div class="' + settings.gutterLineClass + '"></div></ul>').appendTo(document.body);
    if (settings.title) {
      $('<li class="' + settings.headerClass + '"></li>').text(settings.title).appendTo(menu);
    }
    
    $(settings.items).each(function() {
      var item=$(this).get(0);
      if (item) {
        var rowCode = '<li><a href="javascript:return false;"><span></span></a></li>';
        var row = $(rowCode).appendTo(menu);
        row.find('span').text(item.label);
        if (item.isEnabled != undefined && !item.isEnabled()) {
            row.addClass('disabled');
        } else if (item.action) {
            row.find('a').click(function () { item.action(e); });
        }

      } else {
        $('<li class="' + settings.seperatorClass + '"></li>').appendTo(menu);
      }
    });
    
    return menu;
  }


  this.bind('contextmenu', function(e) {	
    var menu = createMenu(e).show();
    
    var left = e.pageX + 5, /* nudge to the right, so the pointer is covering the title */
        top = e.pageY;
    if (top + menu.height() >= $(window).height()) {
        top -= menu.height();
    }
    if (left + menu.width() >= $(window).width()) {
        left -= menu.width();
    }

    // Create and show menu
    menu.css({zIndex:1000001, left:left, top:top}).bind('contextmenu', function() { return false; });

    // Cover rest of page with invisible div that when clicked will cancel the popup.
    var bg = $('<div></div>')
      .css({left:0, top:0, width:'100%', height:'100%', position:'absolute', zIndex:1000000})
      .appendTo(document.body)
      .bind('contextmenu click', function() {
        // If click or right click anywhere else on page: remove clean up.
        bg.remove();
        menu.remove();
        return false;
      });

    // When clicking on a link in menu: clean up (in addition to handlers on link already)
    menu.find('a').click(function() {
      bg.remove();
      menu.remove();
    });

    // Cancel event, so real browser popup doesn't appear.
    return false;
  });

  return this;
};

