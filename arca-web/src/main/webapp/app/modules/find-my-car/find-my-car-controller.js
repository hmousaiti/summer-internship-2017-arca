angular.module('find-my-car.controller', ['ngRoute', 'ngSanitize', 'find-my-car.service' ]).controller('FindMyCarController', FindMyCarController);var lon=0;
var latt=0;
var lon=0;
function FindMyCarController($scope, $http) {
	
		$http.get("rest/location")
		.then(function(response) {
			console.log(response);
    		$scope.location = response.data;
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
		
		 $scope.SendSMS = function() {
			
			$http.get("https://arca-location-webservice.mybluemix.net/location?lat="+latt+"&lng="+lon)
			.then(function(response) {
//				alert("SMS Sent  !");
					
			},function(response) {
				//Second function handles error
//				alert("SMS Failed !");
				});
			
		}
   
}
function myMap() {
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