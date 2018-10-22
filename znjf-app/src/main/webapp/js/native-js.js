
/***************************************客户端属性****************************************/
var browser = {
	versions : function() {
		var u = navigator.userAgent, app = navigator.appVersion;
		return {//移动终端浏览器版本信息 
			trident : u.indexOf('Trident') > -1, //IE内核
			presto : u.indexOf('Presto') > -1, //opera内核
			webKit : u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
			gecko : u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
			ios : !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
			android : u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
			iPhone : u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
			iPad : u.indexOf('iPad') > -1, //是否iPad
			webApp : u.indexOf('Safari') == -1
		//是否web应该程序，没有头部与底部
		};
	}(),
	language : (navigator.browserLanguage || navigator.language).toLowerCase()
}

/**********************************获取项目路径*******************************************/

function getContextPath() {
	var curWwwPath = window.document.location.href;
	// 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	// 获取主机地址，如： http://localhost:8083
	var localhostPaht = curWwwPath.substring(0, pos);
	// 获取带"/"的项目名，如：/uimcardprj
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	var fullPath = localhostPaht + projectName;
	if (fullPath != '' && fullPath.indexOf('/service') != -1) {
		fullPath = fullPath.split('/service')[0];
	}
	return fullPath;
}

/******************************iOS原生与web交互相关事件*************************************/
var ZNJFBridgeEvent = {

	_listeners : {},

	addEvent : function(type, fn) {
		if (typeof this._listeners[type] === "undefined") {
			this._listeners[type] = [];
		}
		if (typeof fn === "function") {
			this._listeners[type].push(fn);
		}

		return this;
	},

	fireEvent : function(type, param) {
		var arrayEvent = this._listeners[type];
		if (arrayEvent instanceof Array) {
			for (var i = 0, length = arrayEvent.length; i < length; i += 1) {
				if (typeof arrayEvent[i] === "function") {
					arrayEvent[i](param);
				}
			}
		}

		return this;
	},

	removeEvent : function(type, fn) {
		var arrayEvent = this._listeners[type];
		if (typeof type === "string" && arrayEvent instanceof Array) {
			if (typeof fn === "function") {
				for (var i = 0, length = arrayEvent.length; i < length; i += 1) {
					if (arrayEvent[i] === fn) {
						this._listeners[type].splice(i, 1);
						break;
					}
				}
			} else {
				delete this._listeners[type];
			}
		}

		return this;
	}
};

var ZNJFEventHandler = {

	callNativeFunction : function(nativeMethodName, params, callBackID, callBack) {
		
		var message;

		if (!callBack) {

			message = {
				'methodName' : nativeMethodName,
				'params' : params
			};
			window.webkit.messageHandlers.ZNJFEventHandler.postMessage(message);

		} else {
			message = {
				'methodName' : nativeMethodName,
				'params' : params,
				'callBackID' : callBackID
			};
			if (!ZNJFBridgeEvent._listeners[callBackID]) {
				ZNJFBridgeEvent.addEvent(callBackID, function(data) {

					callBack(data);

				});
			}
			window.webkit.messageHandlers.ZNJFEventHandler.postMessage(message);
		}

	},

	callBack : function(callBackID, data) {

		ZNJFBridgeEvent.fireEvent(callBackID, data);

	},

	removeAllCallBacks : function(data) {
		
		ZNJFBridgeEvent._listeners = {};
	}

};

/******************************客户端原生与js交互方法*****************************************/

/**判断客户端是否登录示例方法*/
function judgeLoginMethodDemo() {
	ZNJFEventHandler.callNativeFunction("judgeLogin", null, "judgeLoginCallBack", function(token){
		if (token == "" || token == null) {
			//未登录逻辑处理！
			alert("未登录");
		} else {
			//已登录逻辑处理！
			alert("已登录");
		}
	});
}

/**判断客户端是否登录*/
function isClientLogined() {
	if (browser.versions.android) {
		return window.tziba.isLogin();
	} else if (browser.versions.iPhone || browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("judgeLogin", null, "judgeLoginCallBack", function(token){
			if (token == "" || token == null) {
				//未登录逻辑处理！
				return null;
			} else {
				//已登录逻辑处理！
				return token;
			}
		});
	}
}

/**去登陆*/
function gotoLogin() {
	if (browser.versions.android) {
		window.tziba.goNext("login","1");
	} else if (browser.versions.iPhone || browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoLogin", null, null, null);
	}
}

/**弹出【toast】*/
function gotoShowToast(params) {
	if (browser.versions.android) {
		window.tziba.showToast(params);
	} else if (browser.versions.iPhone || browser.versions.ios) {
		var iosParam = {
			'msg' : params
		};// 字符串转成map,android直接传字符串
		ZNJFEventHandler.callNativeFunction("gotoShowToast", iosParam, null, null);
	}
}
/**显示【toast】(兼容老版本)*/
function showError(params) {
	gotoShowToast(params);
}

/**刷新当前网页*/
function gotoReloadCurrentPage() {
if (browser.versions.android) {
		
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoReloadCurrentPage", null, null, null);
	}
}

/**去【积分商城】*/
function gotoJfCenter() {
	if (browser.versions.android) {
		window.tziba.goShop();
	} else if (browser.versions.iPhone || browser.versions.ios) {
		var iosParam = {
			'link' : getContextPath() + "/service/h5_jfhome"
		};
		ZNJFEventHandler.callNativeFunction("gotoNextPage", iosParam, null,
				null);
	}
}

/**去【首页】*/
function gotoHomePage() {
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'index',null);
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoHomePage", null, null, null);
	}
}

/**去【投资列表】*/
function gotoInvestList() {
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'projectList',null);
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoInvestList", null, null, null);
	}
}

/**去【我的账户中心】*/
function gotoMyAccountCenter() {
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'myAccount',null);
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoMyAccountCenter", null, null, null);	
	}
}

/**去【项目详情】*/
function gotoProjectDetail(params) {
	var map={
			'projectId':params
		};
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'projectDetail',JSON.stringify(map));
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoProjectDetail", map, null, null);
	}
}

/**去【提现记录】*/
function gotoWithdrawRecords() {
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'widthrawList',null);
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoWithdrawRecords", null, null, null);	
	}
}

/**弹出【建议评价】*/
function gotoShowAppraiseView() {
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'showEvaluate',null);
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoShowAppraiseView", null, null, null);
	}
}

/**返回上一级页面（原生）*/
function gotoLastPage() {
	if (browser.versions.android) {
		window.tziba.closeThird();
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoLastPage", null, null, null);
	}
}

/**去下一级页面（web）*/
function gotoNextPage(params) {
	if (browser.versions.android) {
		window.location.href = "http://next?" + params
	} else if (browser.versions.iPhone || browser.versions.ios) {
		var iosParam = {
			'link' : path
		};
		ZNJFEventHandler.callNativeFunction("gotoNextPage", iosParam, null, null);
	}
}

/**弹出【分享好友】*/
function gotoShowShareFriendsView(shareTitle, shareContent, shareImgUrl, shareUrl) {
	var map = {
		'shareTitle' : shareTitle,
		'shareContent' : shareContent,
		'shareUrl' : shareUrl,
		'shareImgUrl' : shareImgUrl,
		'isOpenFriends' : 0,
		'isNeedLogin' : 1,
		'dialogTitle' : '分享至:',
		'isNeedUserCode' : 1
	};
	if (browser.versions.android) {
		window.tziba.doShareForActivity(JSON.stringify(map));
	} else if (browser.versions.iPhone || browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoShowShareFriendsView", map, null, null);
	}
}

/**去【开通银行存管】*/
function gotoOpenAccountPage() {
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'myBank',null);
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoOpenAccountPage", null, null, null);
	}
}

/**返回【存管开户页面，再次开户】*/
function gotoOpenAccountPageAgain() {
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'backToOpenAccountAgain',null);
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoOpenAccountPageAgain", null, null, null);
	}
}

/**返回【设置】*/
function gotoSetting() {
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'mySetting',null);
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoSetting", null, null, null);
	}
}

/**返回【存管信息】*/
function gotoBankStorageInfoPage() {
	if (browser.versions.android) {
		window.tziba.openNewActivity(1,'myCgPage',null);
	} else if (browser.versions.iPhone ||browser.versions.ios) {
		ZNJFEventHandler.callNativeFunction("gotoBankStorageInfoPage", null, null, null);
	}
}
