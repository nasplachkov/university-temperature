(function ($, q) {
	"use strict";
	
	var chartDiv = null,
	
		fetchData,
		_fetchData,
		initializeChart,
		drawChart,
		stopFetching,
		
		Data = null,
		Chart = null,
		
		running = false,
		maxTicks = 12,	// 1 tick each 5 seconds = 1 minute
		tickCount = 0,
		secondsPerTick = 5,
		timerId = null;
	
	stopFetching = function () {
		clearInterval(timerId);
		tickCount = 0;
		timerId = null;
	};
	
	fetchData = function () {
		if (++tickCount === maxTicks && timerId) {
			stopFetching();
		}
		
		_fetchData().then(function (data) {
			//var date = new Date();
			var row = [((tickCount - 1) * secondsPerTick) + " sec"];
			
			for (var i in data.list) {
				row.push(data.list[i].temperature);
			}
			
			Data.addRow(row);
			
			drawChart();
		}, function () {
			console.error("Could not insert row data.");
		}).done();
	};
	
	_fetchData = function () {
		var dataPromise = q.defer();
		
		$.getJSON("/temperature/rest/get", function (data) {
			dataPromise.resolve(data);
		}).fail(function () {
			dataPromise.reject();
		});
		
		return dataPromise.promise;
	};

	initializeChart = function() {
		_fetchData().then(function (data) {
			Data = new google.visualization.DataTable();
			Chart = new google.visualization.ColumnChart(chartDiv);

			Data.addColumn("string", "Date");
			
			for (var i in data.list) {
				Data.addColumn("number", data.list[i].placeName);
			}
			
			fetchData();
			timerId = setInterval(fetchData, secondsPerTick * 1000);
		}, function () {
			console.error("Cannot load the columns of the chart.");
		}).done();
	};
	
	drawChart = function () {
        var options = {
                height: 300
            };

        Chart.draw(Data, options);
	};
	
	$("#start").click(function () {
		if (!running) {
			
			maxTicks = ($("#get-interval").val() * 60) / secondsPerTick;
			
			q.fcall(function () {
				var chartP = $("#chart");
				
				if (chartP.length > 0) {
					return chartP.get(0);
				}
				
				throw new Error("Could not find the destination div.");
			}).then(function (chartP) {
				chartDiv = chartP;
			}).then(function () {
				google.load("visualization", "1", {
					packages: ["corechart"],
					callback: initializeChart
				});
			}).fail(function (err) {
				console.error(err);
			}).done();
			$(this).text("Stop");
			$("#chartRow").show("fast");
		} else {
			stopFetching();
			$(this).text("Start");
		}
		
		running = !running;
	});
	
}(jQuery, Q));