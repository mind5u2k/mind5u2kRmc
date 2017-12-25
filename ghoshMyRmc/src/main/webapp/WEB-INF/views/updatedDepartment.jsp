<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<select id="approverDepartmentVal" name="approverDepartmentVal"
	style="width: 100%; margin: 0; height: 23px; padding: 0px 7px;"><option
		value="0">All</option>
	<c:forEach items="${departments}" var="department">
		<option value="${department.id}">${department.name}</option>
	</c:forEach>
</select>