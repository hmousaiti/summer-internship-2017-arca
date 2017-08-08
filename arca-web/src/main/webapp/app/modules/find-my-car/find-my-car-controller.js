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
		//myMap();
		myMap();
		});
	
	$scope.GetPath = function() {
		TakeTime();
		//for(var i in json_data)
		    
		$http.get("rest/location/path?from="+from+"&to="+to)
		.then(function(response) {
			var jsonData = response.data.path;
			//document.getElementById("display").innerHTML = jsonData.path[1].long;
			for(var i = 0; i< jsonData.length ; i++)
				{
					locations.push([i, jsonData[i]]);
				//	document.getElementById("display").innerHTML = jsonData.path[i];
				}
			initialize();
			document.getElementById("display").innerHTML = locations[0][1].lat;
			$scope.location = "Requested Path";
		},function(response) {
			//Second function handles error
//			alert("SMS Failed !");
		});
		
	}
	
	$scope.SendSMS = function() {
		
		var phone = document.getElementById("phone").value;
		$http.get("https://arca-location-webservice.mybluemix.net/location?lat="+latt+"&lng="+lon+"number="+phone)
		.then(function(response) {
//			alert("SMS Sent  !");
				
		},function(response) {
			//Second function handles error
//			alert("SMS Failed !");
			});
		
	}
	
}
	function initialize() {
	  directionsDisplay = new google.maps.DirectionsRenderer();
	  var directionsDisplay;
	  var directionsService = new google.maps.DirectionsService();
//	  var locations = [
//	    ['Manly Beach', -33.80010128657071, 151.28747820854187, 2],
//	    ['Bondi Beach', -33.890542, 151.274856, 4],
//	    ['Coogee Beach', -33.923036, 151.259052, 5],
//	    ['Maroubra Beach', -33.950198, 151.259302, 1],
//	    ['Cronulla Beach', -34.028249, 151.157507, 3]
//	  ];

	  var map = new google.maps.Map(document.getElementById('googleMap'), {
	    zoom: 10,
	    center: new google.maps.LatLng(latt, lon),
	    mapTypeId: google.maps.MapTypeId.ROADMAP
	  });
	  directionsDisplay.setMap(map);
	  var infowindow = new google.maps.InfoWindow();

	  var marker, i;
	  var request = {
	    travelMode: google.maps.TravelMode.DRIVING
	  };
	  for (i = 0; i < locations.length; i++) {
	    marker = new google.maps.Marker({
	      position: new google.maps.LatLng(locations[i][1].lat, locations[i][1].long),
	    });

	    google.maps.event.addListener(marker, 'click', (function(marker, i) {
	      return function() {
	        infowindow.setContent(locations[i][0]);
	        infowindow.open(map, marker);
	      }
	    })(marker, i));

	    if (i == 0) request.origin = marker.getPosition();
	    else if (i == locations.length - 1) request.destination = marker.getPosition();
	    else {
	      if (!request.waypoints) request.waypoints = [];
	      request.waypoints.push({
	        location: marker.getPosition(),
	        stopover: true
	      });
	    }

	  }
	  directionsService.route(request, function(result, status) {
	    if (status == google.maps.DirectionsStatus.OK) {
	      directionsDisplay.setDirections(result);
	    }
	  });
	}
	//google.maps.event.addDomListener(window, "load", initialize);

	function TakeTime()
	{
		var x = document.getElementById("from");
		var time = x.value;
		var myDate = new Date(time);
		from = myDate.getTime();
		//document.getElementById("display").innerHTML = n;
		x = document.getElementById("to");
		time = x.value;
		myDate = new Date(time);
		to=myDate.getTime();
	}
   

function myMap() {
		//directionsDisplay = new google.maps.DirectionsRenderer();
		var mapProp= {
		    //center:new google.maps.LatLng(30.0752169,31.0192871),
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