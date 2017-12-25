<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<select id="approverLOBVal" name="approverLOBVal"
	onchange="updateapproverLocation();"
	style="width: 100%; margin: 0; height: 23px; padding: 0px 7px;"><option
		value="0">All</option>
	<c:forEach items="${lobs}" var="lob1">
		<option value="${lob1.id}">${lob1.name}</option>
	</c:forEach>
</select>