// Chart.js scripts
// -- Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';
// -- Area Chart Example
/*-var ctx = document.getElementById("myAreaChart");
 var myLineChart = new Chart(ctx, {
 type : 'line',
 data : {
 labels : [ "Mar 1", "Mar 2", "Mar 3", "Mar 4", "Mar 5", "Mar 6",
 "Mar 7", "Mar 8", "Mar 9", "Mar 10", "Mar 11", "Mar 12",
 "Mar 13" ],
 datasets : [ {
 label : "Sessions",
 lineTension : 0.3,
 backgroundColor : "rgba(2,117,216,0.2)",
 borderColor : "rgba(2,117,216,1)",
 pointRadius : 5,
 pointBackgroundColor : "rgba(2,117,216,1)",
 pointBorderColor : "rgba(255,255,255,0.8)",
 pointHoverRadius : 5,
 pointHoverBackgroundColor : "rgba(2,117,216,1)",
 pointHitRadius : 20,
 pointBorderWidth : 2,
 data : [ 10000, 30162, 26263, 18394, 18287, 28682, 31274, 33259,
 25849, 24159, 32651, 31984, 38451 ],
 } ],
 },
 options : {
 scales : {
 xAxes : [ {
 time : {
 unit : 'date'
 },
 gridLines : {
 display : false
 },
 ticks : {
 maxTicksLimit : 7
 }
 } ],
 yAxes : [ {
 ticks : {
 min : 0,
 max : 40000,
 maxTicksLimit : 5
 },
 gridLines : {
 color : "rgba(0, 0, 0, .125)",
 }
 } ],
 },
 legend : {
 display : false
 }
 }
 });*/
// -- Bar Chart Example
var ctx = document.getElementById("categoryWiseRiskChart");
var clabel = categories.split(",");
var cdata = categoryRisk.split(",");
var myLineChart = new Chart(ctx, {
	type : 'bar',
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
		responsive : true,
		scales : {
			xAxes : [ {
				gridLines : {
					display : false,
					color : "#131c2b"
				},
				ticks : {
					maxTicksLimit : 16,
					fontColor : "#131c2b",
				}
			} ],
			yAxes : [ {
				display : false,
				ticks : {
					min : 0,
					maxTicksLimit : 5
				},
				gridLines : {
					display : false
				}
			} ],
		},
		legend : {
			display : true
		}
	}
});
// -- Pie Chart Example
var ctx = document.getElementById("myPieChart");
var plabel = intExtRisk.split(",");
var pdata = intExtRiskVal.split(",");
var myPieChart = new Chart(ctx, {
	type : 'pie',
	data : {
		labels : plabel,
		datasets : [ {
			data : pdata,
			borderWidth : 1,
			backgroundColor : [ 'rgba(0,123,255,0.7)', 'rgba(220,53,69,0.7)',
					'rgba(255,193,7,0.7)', 'rgba(40,167,69,0.7)' ],
			borderColor : [ 'rgba(0,123,255,1)', 'rgba(220,53,69,1)',
					'rgba(255,193,7,1)', 'rgba(40,167,69,1)' ]
		} ],
	},
});

/*-var ctx = document.getElementById("categoryWiseRiskChart1");
 var clabel = categories.split(",");
 var cdata = categoryRisk.split(",");
 var myLineChart = new Chart(ctx, {
 type : 'horizontalBar',
 data : {
 labels : clabel,
 datasets : [ {
 label : "Risk Factor",
 backgroundColor : "rgba(2,117,216,1)",
 borderColor : "rgba(2,117,216,1)",
 data : cdata,
 } ],
 },
 options : {
 scales : {
 xAxes : [ {
 time : {
 unit : 'month'
 },
 gridLines : {
 display : false
 },
 ticks : {
 maxTicksLimit : 16
 }
 } ],
 yAxes : [ {
 ticks : {
 min : 0,
 max : 5,
 maxTicksLimit : 5
 },
 gridLines : {
 display : true
 }
 } ],
 },
 legend : {
 display : true
 }
 }
 });*/
