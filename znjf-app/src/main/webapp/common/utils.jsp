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