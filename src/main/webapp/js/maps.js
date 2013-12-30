(function($, q) {
	"use strict";
	
	var getCurrentLocation,
		initializeMap,
		initializeMarkers,
		setMarkerInfo;
	
	getCurrentLocation = function () {
		var locationPromise = q.defer();
		
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function (position) {
				locationPromise.resolve(position);
			}, function (error) {
				locationPromise.reject(error);
			});
		} else {
			locationPromise.reject(new Error("HTML5 Geo API is not supported."));
		}
		
		return locationPromise.promise;
	};

	initializeMap = function (position) {
		var mapOptions = {
				center : new google.maps.LatLng(position.coords.latitude, position.coords.longitude),
				zoom : 8
			},
			map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
		
		return map;
	};
	
	initializeMarkers = function (map) {
		//var currentMapPosition = map.getCenter();
		
		$.getJSON("/temperature/rest/get", function (data) {
			for (var i in data.list) {
				var temp = data.list[i],
					marker = new google.maps.Marker({
						position: new google.maps.LatLng(temp.location.latitude, temp.location.longitude),
						map: map,
						title: temp.placeName
					});
				
				setMarkerInfo(marker, temp.location);
			}
		});
	};
	
	setMarkerInfo = function (marker, location) {
		google.maps.event.addListener(marker, "click", function () {
			$.getJSON("/temperature/rest/get/location?lat=" + location.latitude + "&long=" + location.longitude, function (data) {
				alert("Current temperature: " + data.locations[0].temperature + " °C");
			});
		});
	};
	
	q.fcall(getCurrentLocation).then(function (position) {
		return initializeMap(position);
	}).then(initializeMarkers).fail(function (error) {
		alert(error);
	}).done();
	
}(jQuery, Q));