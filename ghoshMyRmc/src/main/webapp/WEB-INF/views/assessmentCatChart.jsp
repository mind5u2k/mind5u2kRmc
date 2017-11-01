<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty answerText}">
	<canvas id="categoryWiseRiskChart${assessmentCatId}" width="100%"
		height="35%"></canvas>
</c:if>

<c:if test="${empty answerText}">
	<div style="text-align: center; padding: 89px 0px; font-size: 26px;">No
		Risk Available</div>
</c:if>



<script>
	window.answerText = '${answerText}';
	window.answerVal = '${answerVal}';
	var ctx = document
			.getElementById("categoryWiseRiskChart${assessmentCatId}");
	var clabel = answerText.split(",");
	var cdata = answerVal.split(",");
	var myLineChart = new Chart(ctx, {
		type : 'horizontalBar',
		data : {
			labels : clabel,
			datasets : [ {
				label : "Risk Factor",
				backgroundColor : "rgba(7,121,243,0.5)",
				borderColor : "rgba(2,117,216,1)",
				borderWidth : 1,
				data : cdata,
			} ],
		},
		options : {
			tooltips : {
				callbacks : {

					// We'll edit the `title` string
					title : function(tooltipItem) {
						// `tooltipItem` is an object containing properties such as
						// the dataset and the index of the current item

						// Here, `this` is the char instance

						// The following returns the full string
						return this._data.labels[tooltipItem[0].index];
					}
				}
			},
			scales : {
				xAxes : [ {
					display : false,
					time : {
						unit : 'month'
					},
					display : false,
					gridLines : {
						color : "#131c2b",
						display : false
					},
					ticks : {
						maxTicksLimit : 16,
						callback : function(tick) {
							var characterLimit = 20;
							if (tick.length >= characterLimit) {
								return tick.slice(0, tick.length).substring(0,
										characterLimit - 1).trim()
										+ '...';
							}
							return tick;
						}
					}
				} ],
				yAxes : [ {
					ticks : {
						min : 0,
						max : 5,
						maxTicksLimit : 5,
						callback : function(tick) {
							var characterLimit = 20;
							if (tick.length >= characterLimit) {
								return tick.slice(0, tick.length).substring(0,
										characterLimit - 1).trim()
										+ '...';
							}
							return tick;
						}
					},
					gridLines : {
						color : "#131c2b",
						display : true
					}
				} ],
			},
			legend : {
				display : true
			}
		}
	});
</script>