var pass = 0; 
var fail = 0;
var skip = 0;
var assertError = 0;
var otherError = 0;
var seconds = [];
var numberOfRecords = 0;
var totalExecutionTime = 0;
var endPointNames=[];
var methodTypeNames=[];
var totalEndPoints = data[0].totalEndPointCount;

function removeNull(jsonData) {
	return jsonData === null ? "" : jsonData
}

function addTableRowsFromJSON() {
	var newTableRow;
	var newTableCell;
	var tableBody = document.getElementById("BodyRows");
	var mainTable = tableBody.parentNode;
	for (var r in data) {

		newTableRow = tableBody.insertRow(r);
		var testResult = removeNull(data[r].status);
		testResult === "SUCCESS" ? (newTableRow.className = "SUCCESS", pass += 1) : 
			testResult === "FAILED" ? (newTableRow.className = "FAILED", fail += 1, failedData(removeNull(data[r].exception))) : 
				(newTableRow.className = "SKIPPED", skip += 1);
			
		for (j = 0; j < 10; j++) {
			newTableCell = newTableRow.insertCell(newTableRow.cells.length);
			var jsonDataFromDataFile;
			var newTableCellDiv = document.createElement("div");
			
			switch (j) {
			case 0:
				jsonDataFromDataFile = removeNull(data[r].testCaseSequence);
				break;
			case 1:
				jsonDataFromDataFile = removeNull(data[r].fileName);
				break;
			case 2:
				jsonDataFromDataFile = removeNull(data[r].endPoint);
				endPointNames.push(jsonDataFromDataFile);
				break;
			case 3:
				jsonDataFromDataFile = removeNull(data[r].methodType);
				methodTypeNames.push(jsonDataFromDataFile);
				break;
			case 4:
				jsonDataFromDataFile = removeNull(data[r].queryParam);
				break;
			case 5:
				jsonDataFromDataFile = removeNull(data[r].pathParam);
				break;
			case 6:
				jsonDataFromDataFile = removeNull(data[r].dbQuery);
				break;
			case 7:
				jsonDataFromDataFile = removeNull(data[r].completionTimeForIndividualTestCase);
				totalExecutionTime = totalExecutionTime + jsonDataFromDataFile;
				jsonDataFromDataFile = jsonDataFromDataFile/1000;
				seconds.push(jsonDataFromDataFile);
				break;
			case 8:
				jsonDataFromDataFile = removeNull(data[r].status);
				break;
			case 9:
				jsonDataFromDataFile = removeNull(data[r].exception);
				break;	
			}
			newTableCellDiv.innerHTML = jsonDataFromDataFile,
			j != 1 && j != 2 || (newTableCellDiv.className = "class"),
			j != 3 && j != 4 || (newTableCellDiv.className = "result"), 
			newTableCell.appendChild(newTableCellDiv)
		}
	}
	mainTable.appendChild(tableBody)
}

function status() {
	var e = {
			type: "pie",
			data: {
				datasets: [{
					data: [assertError, otherError],
					backgroundColor: ["#6c8e5e", "#41c1f4"],
					label: "Dataset 1"
				}],
				labels: ["Assert Fail", "Other Exceptions"]
			},
			options: {
				pieceLabel: {
					render: "percentage",
					fontColor: ["white", "white", "white"],
					precision: 1,
					fontSize: 11,
					fontStyle: "bold",
					overlap: !0
				},
				title: {
					display: !1,
					text: "Type of Execptions"
				},
				legend: {
					labels: {
						boxWidth: 10,
						padding: 5,
						fontSize: 12
					}
				}
			}
	},
	t = {
			type: "pie",
			data: {
				datasets: [{
					data: [pass, fail, skip],
					backgroundColor: ["#82b74b", "#f7786b", "#ffcc5c"],
					label: "Dataset 1"
				}],
				labels: ["PASSED", "FAILED", "SKIPPED"]
			},
			options: {
				pieceLabel: {
					render: "percentage",
					fontColor: ["white"],
					precision: 1,
					fontSize: 11,
					fontStyle: "bold",
					overlap: !0
				},
				title: {
					display: !1,
					text: "Total Test Result"
				},
				legend: {
					labels: {
						boxWidth: 25,
						padding: 7,
						fontSize: 12
					}
				}
			}
	},
	a = document.getElementById("test-status").getContext("2d");
	window.myPie = new Chart(a, t);
	var o = document.getElementById("fail-result").getContext("2d");
	window.myPie = new Chart(o, e)
}

function StatusTable() {
	var e = document.getElementById("statusTable");
	var t = e.insertRow(1);
	t.setAttribute("style", "background-color: #8cf2a2;");
	
	var a = t.insertCell(0);
	var o = t.insertCell(1);
	a.innerHTML = "Passed";
	o.innerHTML = pass;
	
	var r = e.insertRow(2);
	r.setAttribute("style", "background-color: #f7786b;");
	
	var n = r.insertCell(0);
	var l = r.insertCell(1);
	n.innerHTML = "Failed";
	l.innerHTML = fail;
	
	var i = e.insertRow(3);
	i.setAttribute("style", "background-color: #f9ef7f;");
	
	var d = i.insertCell(0);
	var s = i.insertCell(1);
	d.innerHTML = "Skipped";
	s.innerHTML = skip;
	
	var c = e.insertRow(4);
	c.setAttribute("style", "background-color: #87cefa;");
	
	var m = c.insertCell(0);
	var u = c.insertCell(1);
	m.innerHTML = "Total";
	u.innerHTML = skip + pass + fail;
	
	document.getElementById("time").innerHTML = millisToHoursMinutesSeconds(totalExecutionTime) + " (hh:mm:ss)";
	
	var execStartTime = data[0].executionStartTime;
	document.getElementById("executionStartDateTime").innerHTML = formatMillisToDateandTime(execStartTime);
	
	document.getElementById("testVMName").innerHTML = data[0].testVMName;
}

function formatMillisToDateandTime(startTime) {
	var date = new Date(startTime);
	return date.toLocaleString()
}

function millisToHoursMinutesSeconds(millis) {
	var sec = Math.floor(millis/1000);
	var hrs = Math.floor(sec/3600);
	sec = sec - hrs * 3600;
	var min = Math.floor(sec / 60);
	sec = sec - min * 60;
	return (hrs < 10 ? '0' : '') + hrs + ":" + (min < 10 ? '0' : '') + min + ":" + (sec < 10 ? '0' : '') + sec;
}

function failedData(e) {
	e.indexOf("expected [") > -1 && e.indexOf("but found [") > -1 ? assertError += 1 : otherError += 1
}

function lineGraph(e, t) {
	const labels = seconds.slice(e, t).map(p => "")
	var data = {
		labels: labels,
		datasets: [{
			label: "Execution Time (Sec) / Test",
			data: seconds.slice(e, t),
			backgroundColor: "lightblue",
			pointBackgroundColor: "#0099e6"
		}],
	};
	new Chart(document.getElementById("myChart"), {
		type: 'line',
		data: data,
		options: {
			tooltips: {
				callbacks: {
					label: function(tooltipItem, data) {
						return [methodTypeNames[tooltipItem.index] + " : " + endPointNames[tooltipItem.index],  "Execution Time(in sec) : " + tooltipItem.yLabel];
					}
				}
			}
		}
	});
}

function autoRefresh(){
	location.reload(true);
}

window.addEventListener("load", function() {
	addTableRowsFromJSON(), status(), lineGraph(0, 49), autoRefresh()
});


function startReport() {
	var table = $('#MainTable').DataTable({
		"sScrollX": true,
		"scrollCollapse": true,
		"bAutoWidth": true,
		"paging": true,
		"fixedColumns": {
			leftColumns: 3
		},
		"columns": [{
			"width": "15%"
		}, {
			"width": "20%"
		}, {
			"width": "25%"
		}, {
			"width": "25%"
		}, {
			"width": "25%"
		}, {
			"width": "25%"
		}, {
			"width": "30%"
		}, {
			"width": "10%"
		}, {
			"width": "25%"
		}, {
			"width": "60%"
		}],
		"dom": 'Bfrtip',
		"buttons": [
			{ extend: 'copyHtml5', className: 'btn btn-primary glyphicon glyphicon-list-alt' },
			{ extend: 'excel',
				customize: function( xlsx ) {
					var sheet = xlsx.xl.worksheets['sheet1.xml'];
					$('row c[r^="I"]', sheet).each( function () {
						var status=$(this);
						if ( $(this).text().includes("FAILED")) {
							/* $('row:eq() c', sheet).attr( 's', '35' );
                        	$('row c[r^="A"]', sheet).css( 'background-color', 'red' );
                        	$('row c[r^="B"]', sheet).attr( 's', '35' );
                        	$('row c[r^="B"]', sheet).css( 'background-color', 'red' );*/

						}
					});
				},
				className: 'btn btn-primary glyphicon glyphicon-list-alt' 
			},
			{
				extend : 'pdfHtml5',
				customize: function(doc) {
					var status = table.column(8).data().toArray();
					for (var i = 0; i < status.length; i++) {
						if (status[i].includes("FAILED")) {
							doc.content[1].table.body[i+1][0].fillColor = '#f5a3a3';
							doc.content[1].table.body[i+1][1].fillColor = '#f5a3a3';
							doc.content[1].table.body[i+1][2].fillColor = '#f5a3a3';
							doc.content[1].table.body[i+1][3].fillColor = '#f5a3a3';
							doc.content[1].table.body[i+1][4].fillColor = '#f5a3a3';
							doc.content[1].table.body[i+1][5].fillColor = '#f5a3a3';
							doc.content[1].table.body[i+1][6].fillColor = '#f5a3a3';
							doc.content[1].table.body[i+1][7].fillColor = '#f5a3a3';
							doc.content[1].table.body[i+1][8].fillColor = '#f5a3a3';
							doc.content[1].table.body[i+1][9].fillColor = '#f5a3a3';
						}
					}
				},
				className: 'btn btn-primary glyphicon glyphicon-file pdfButton',
				orientation : 'landscape',
				pageSize : 'A3',
				text : '<i class="fa fa-file-pdf-o"> PDF</i>',
				titleAttr : 'PDF'
			},
			{ extend: 'print', className: 'btn btn-primary glyphicon glyphicon-print' }
			],
			"fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
				var resultColumn = aData[8];
				if(resultColumn.includes("FAILED")) {
					$('td', nRow).css('background-color', '#f5a3a3');
				}
			}
	});
	StatusTable();
	numberOfRecords = table.page.info().recordsTotal;
	if(numberOfRecords >= totalEndPoints){
		$('.pdfButton').click();
		$('#myCheck').prop("checked", false);

		window.stop();
	}
}
$('#MainTable td').css('white-space','initial');
