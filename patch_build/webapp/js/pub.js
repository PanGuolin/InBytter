//window.onload = attachMenuEvent;
$(function(){ $("[name=popMenuSource]").bind("click", showMenu).attr("class", "popMenuSource");});

$(function(){
	var $_helpSources = $("[help_id]");
	//$_helpSources.append($("<img src='/patch/images/help.gif'/>"));
	$_helpSources.each(function() {
		$("#" + $(this).attr("help_id")).attr("class", "help_text").hide();
		$_img = $("<img src='/patch/images/help.gif'/>").bind("click", 
				function(){
					var $_helpText = $("#" + $(this).parent().attr("help_id"));
					$_helpText.is(":visible") ? $_helpText.hide("normal") : $_helpText.show("normal");
				}
		);
		$(this).append($_img);
	});
});


/**
 * 设置页面的title信息
 * @param name
 */
function setPgeTitle(name) {
	$("#pageTitle").text(name);
}

function setCurrentPage(id) {
	$("#btn_" + id).attr("class", "current");
}

function setMessage(msg) {
	$("message_span").html(msg);
}

var orgInHtml = null;
var lastMenuItem = null;
/**
 * 弹出菜单
 * @param ev
 */
function showMenu(ev) {
	var $_menu = $("#menuDiv");
	if (!$_menu[0]) return;
	if ($_menu.is(":visible")) {
		$_menu.hide();
	}
    //可能事件源是真正元素的子对象
    var $_obj = $(ev.target);
    while($_obj[0]) {
    	if ($_obj.attr("name") == "popMenuSource") 
    		break;
    	$_obj = $_obj.parent();
    }
    if (lastMenuItem) {
    	lastMenuItem.attr("class", "popMenuSource");
    }
    lastMenuItem = $_obj;
    lastMenuItem.attr("class", "popMenuSource_selected");
	
	if (orgInHtml == null) {
		orgInHtml = $_menu.html();
	}
	var inHtml = orgInHtml;
		
	var sPos = inHtml.indexOf("%")
	while(sPos != -1) {
		var ePos = inHtml.indexOf("%", sPos + 1);
		if (ePos == -1)
			break;
		var key = inHtml.substring(sPos+1, ePos);
		var value = $_obj.attr(key);
		if (value) {
			inHtml = inHtml.substring(0, sPos) + value + inHtml.substring(ePos+1);
			sPos = inHtml.indexOf("%", ePos + 1 + (value.length - key.length));
		} else {
			sPos = inHtml.indexOf("%", ePos + 1);
		}
	}
	$_menu.html(inHtml);
	
	var top;
	if (ev.pageY + $_menu.height() >=  window.screen.availHeight - document.body.scrollTop +10) {
		top = ev.pageY - $_menu.height();
	} else {
		top = ev.pageY;
	}
	$_menu.css({"position":"absolute", "left": (ev.pageX+1) + "px", "top": top + "px"});
	$_menu.fadeIn("normal");
	$("#menuDiv .tips").css("left", $_menu.width() + 4 + "px");
	
	ev.stopPropagation();
	ev.preventDefault();
	$(document).one("click", hideMenu);
}

function hideMenu(ev) {
	$("#menuDiv").hide();
	if (lastMenuItem)
	   lastMenuItem.attr("class", "popMenuSource");
}


/**
 * 在两个Select 控件之间移动选项(Option)，从左边移到右边
 * @param option 
 * @param leftSelect
 * @param rightSelect
 */
function move2Right(option, leftSelect, rightSelect) {
	if (typeof(leftSelect) == "string") 
		leftSelect = document.getElementById(leftSelect);
	if (typeof(rightSelect) == "string")
		rightSelect = document.getElementById(rightSelect);
	if (option.tagName == "SELECT") {
		option = option.options[option.selectedIndex];
		rightSelect.appendChild(option);
		return false;
	} else {
		option.ondblclick = function() {move2Left(option, leftSelect, rightSelect)};
		rightSelect.appendChild(option);
	}
	
}

function move2Left(option, leftSelect, rightSelect) {
	if (typeof(leftSelect) == "string") 
		leftSelect = document.getElementById(leftSelect);
	if (typeof(rightSelect) == "string")
		rightSelect = document.getElementById(rightSelect);
	if (option.tagName == "SELECT") {
		option = option.options[option.selectedIndex];
		leftSelect.appendChild(option);
	} else {
		option.ondblclick = function() {move2Right(option, leftSelect, rightSelect)};
		leftSelect.appendChild(option);
	}
}
