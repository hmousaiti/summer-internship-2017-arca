angular.module('find-my-car.controller', ['ngRoute', 'ngSanitize', 'find-my-car.service' ]).controller('FindMyCarController', FindMyCarController);
var latt=0;
var lon=0;
var geocoder;
var map;
var from;
var to;
var locations = [];
function FindMyCarController($scope, $http) {
	
	
	$http.get("rest/location")
	.then(function(response) {
		console.log(response);
		$scope.location = "Latest Location";
		lon= response.data.long;
		latt= response.data.lat;
		myMap();
	},function(response) {
		//Second function handles error
		$scope.found = "Something went wrong";
		lon= 50.0192871;
		latt=50.0752169;
		myMap();
		});
	
	$scope.GetPath = function() {
		TakeTime();
		
		    
		$http.get("rest/location/path?from="+from+"&to="+to)
		.then(function(response) {
			var jsonData = response.data.path;
			for(var i = 0; i< jsonData.length ; i++)
				{
					locations.push(jsonData[i]);
				}
			initialize();
			$scope.location = "Requested Path";
		},function(response) {
			//Second function handles error

		});
		
	}
	
	$scope.SendSMS = function() {
		
		var phone = document.getElementById("phone").value;
		$http.get("https://arca-location-webservice.mybluemix.net/location?lat="+latt+"&lng="+lon+"&number="+phone)
		.then(function(response) {
			//alert("SMS Sent  !");
				
		},function(response) {
			//Second function handles error
			//alert("SMS Failed !");
			});
		
	}
	
}
	function initialize() {
	  directionsDisplay = new google.maps.DirectionsRenderer();
	  var directionsDisplay;
	  var directionsService = new google.maps.DirectionsService();
	  var map = new google.maps.Map(document.getElementById('googleMap'), {
	    zoom: 14,
	    center: new google.maps.LatLng(locations[locations.length-1].lat, locations[locations.length-1].long),
	    mapTypeId: google.maps.MapTypeId.ROADMAP
	  });
	  directionsDisplay.setMap(map);
	  var infowindow = new google.maps.InfoWindow();

	  	  
	   marker1 = new google.maps.Marker({
	      position: new google.maps.LatLng(locations[0].lat, locations[0].long),
	      
	      map : map
	    });
	   marker2 = new google.maps.Marker({
	      position: new google.maps.LatLng(locations[locations.length -1].lat, locations[locations.length -1].long),
	      icon: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png',
	      map : map
	    });
	  
	  
	  for (var i = 0, parts = [], max = 25 ; i < locations.length; i = i + max)
	        parts.push(locations.slice(i, i + max + 1));

	    // Service callback to process service results
	    var service_callback = function(response, status) {
	        if (status != 'OK') {
	            console.log('Directions request failed due to ' + status);
	            return;
	        }
	        var renderer = new google.maps.DirectionsRenderer;
	        renderer.setMap(map);
	        renderer.setOptions({ suppressMarkers: true, preserveViewport: true });
	        renderer.setDirections(response);
	    };

	    // Send requests to service to get route (for stations count <= 25 only one request will be sent)
	    for (var i = 0; i < parts.length; i++) {
	        // Waypoints does not include first station (origin) and last station (destination)
	        var waypoints = [];
	        var pos;
	        for (var j = 1; j < parts[i].length - 1; j++)
	        	pos =	new google.maps.LatLng(parts[i][j].lat, parts[i][j].long);
	            waypoints.push({location: pos, stopover: true});
	        // Service options
	        var posStart =	new google.maps.LatLng(parts[i][0].lat, parts[i][0].long);
	        var posEnd =	new google.maps.LatLng(parts[i][parts[i].length - 1].lat, parts[i][parts[i].length - 1].long);
	        var service_options = {
	        	origin:  posEnd,
	            destination: posStart,
	            waypoints: waypoints,
	            travelMode: 'DRIVING'
	        };
	        // Send request
	        directionsService.route(service_options, service_callback);
	    }


	  locations = [];
	}
	

	function TakeTime()
	{
		var x = document.getElementById("from");
		var time = x.value;
		var myDate = new Date(time);
		from = myDate.getTime();
		x = document.getElementById("to");
		time = x.value;
		myDate = new Date(time);
		to=myDate.getTime();
	}
   

function myMap() {
		
		var mapProp= {
		    zoom: 15,
		    center: {lat: latt, lng: lon}
		};
		var map=new google.maps.Map(document.getElementById("googleMap"),mapProp);
		var marker = new google.maps.Marker({
		    // The below line is equivalent to writing:
		    // position: new google.maps.LatLng(-34.397, 150.644)
		    position: {lat:latt, lng:lon},
		    map: map
		});
		var infowindow = new google.maps.InfoWindow({
		    content: '<p>Marker Location:' + marker.getPosition() + '</p>'
		  });

		  google.maps.event.addListener(marker, 'click', function() {
		    infowindow.open(map, marker);
		  });
		}