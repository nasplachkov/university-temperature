(function ($) {
	"use strict";
	
	var startButton = $("#start"),
		log = $("#log"),
		intervalField = $("#post-interval"),
		running = false,
		timerId = null,
		updateStatus,
		createStatus;
	
	updateStatus = function (status) {
		$.ajax("/temperature/twitter?update=true", {
			type: "POST",
			data: {
				status: status
			}
		}).done(function () {
			log.append("Posting success!<br/>");
		}).fail(function () {
			log.append("Posting failure.<br/>");
		});
	};
	
	createStatus = function () {
		$.getJSON("/temperature/rest/get", function (data) {
			var twitterStatus = "";
			
			for (var i in data.list) {
				var temp = data.list[i];
				
				twitterStatus += temp.placeName + ": " + temp.temperature + " °C, " + temp.humidity + " %.\n";
			}
			
			updateStatus(twitterStatus);
		});
	};
	
	startButton.click(function () {
		if (running) {
			clearInterval(timerId);
			log.append("Timer stopped.<br/>");
			
			intervalField.prop("disabled", false);
			startButton.text("Start");
		} else {
			log.append("Timer started.<br/>");
			
			// Update immediately
			createStatus();
			
			// Get the input value and convert it from minutes to milliseconds
			timerId = setInterval(createStatus, intervalField.val() * 60 * 1000);
			
			intervalField.prop("disabled", true);
			startButton.text("Stop");
		}
		running = !running;
	});
	
}(jQuery));