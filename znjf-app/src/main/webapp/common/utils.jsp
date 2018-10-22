<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tziba" uri="/WEB-INF/tld/znjf.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.tziba.zookeeper.utils.EvnUtils" %>
<%
	/* 本地资源请求路径 */
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	request.setAttribute("basePath", basePath);
	
	/* 静态资源路径前缀 */
	String webSource = EvnUtils.getValue("static.http.domain");
	request.setAttribute("webSource", webSource);
%>
<script src="${webSource}/app/static/js/jquery-1.7.2.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${basePath}js/native-js.js" type="text/javascript"></script>