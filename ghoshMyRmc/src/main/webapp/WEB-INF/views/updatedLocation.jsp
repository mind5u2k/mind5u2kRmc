<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<select id="approverLocationVal" name="approverLocationVal"
	onchange="updateapproverDepartment();"
	style="width: 100%; margin: 0; height: 23px; padding: 0px 7px;"><option
		value="0">All</option>
	<c:forEach items="${locations}" var="location">
		<option value="${location.id}">${location.name}</option>
	</c:forEach>
</select>