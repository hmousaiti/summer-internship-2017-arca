'use strict';
(function () {
	var UIMessage = function (arg) {
        this.text = arg.text, this.message_side = arg.message_side;
        this.draw = function (_this) {
            return function () {
            	console.log(_this);
                var $message;
                $message = $($('.message_template').clone().html());
                $message.addClass(_this.message_side).find('.text').html(_this.text);
                $('.messages').append($message);
                return setTimeout(function () {
                    return $message.addClass('appeared');
                }, 0);
            };
        }(this);
        return this;
    };
    
    
    var renderMessage = function (txt, side) {
        var uiMessage = new UIMessage({
            text: txt,
            message_side: side
        });
        uiMessage.draw();
        $('.messages').animate({ scrollTop: $('.messages').prop('scrollHeight') }, 300);
    };
    
	function convertDateToMS(time)
	{
		return new Date(time).getTime();
	}
    
    var sendMessage = function ($scope, conversationService) {
    	 var userInput = $scope.userInput;
        if ($scope.userInput.trim() === '') {
            return;
        }
        $scope.userInput = "";
        
        renderMessage(userInput, 'left');

        conversationService.converse(userInput).then(function (response) {
    		renderMessage(response.output.text, 'right');
    		$scope.USER_INPUT = response.context.USER_INPUT;
    		$scope.SHOW_LOCATION = response.context.SHOW_LOCATION;
    		$scope.IMAGE_FIELD = response.context.IMAGE_FIELD;
    		if($scope.SHOW_LOCATION == "TRUE"){
    			latt =response.context.LAT ;
    			lon =response.context.LONG ;
    			myMap () ;
    			
    		}
    		$scope.SHOW_PATH = response.context.SHOW_PATH;
    		if($scope.SHOW_PATH == "TRUE"){
    			
    			var jsonData = response.context.PATH.Path;
    			for(var i = 0; i< jsonData.length ; i++)
				{
					locations.push(jsonData[i]);
				}
			initialize();
    		}
    		$scope.SHOW_LOCATION = response.context.SHOW_LOCATION;
    		
    		console.log($scope.SHOW_LOCATION);
    		
    	});
        
    };
    var sendDate = function ($scope, conversationService) {
//      if ($scope.userInput.trim() === '') {
//          return;
//      }
//      $scope.userInput = "";
      
      renderMessage("FROM"+ $scope.fromDate +" TO "+ $scope.toDate, 'left');
      conversationService.converse(null, convertDateToMS($scope.fromDate), convertDateToMS($scope.toDate)).then(function (response) {
          console.log(response);
   	   renderMessage(response.output.text, 'right');
   	   console.log(response.output.text);
   	   $scope.USER_INPUT = response.context.USER_INPUT;
   	   $scope.SHOW_PATH = response.context.SHOW_PATH;
   	   $scope.SHOW_PATH = response.context.SHOW_PATH;
     		console.log("abl el if");
     		if(response.context.SHOW_PATH == "TRUE"){
     			console.log("gowa el if");
     			var jsonData = response.context.PATH.path;
     			console.log(response);
     			for(var i = 0; i< jsonData.length ; i++)
  				{
  					locations.push(jsonData[i]);
  				}
  			initialize();
     		}
      });
      
  };
   
   var sendImage = function ($scope, formdata, conversationService) {
     
     renderMessage("Please wait while I analyze your image.....", 'right');
     
     conversationService.uploadFiles(formdata).then(function (response) {
      //   console.log(response);

  	   renderMessage(response.output.text, 'right');
  	   $scope.USER_INPUT = response.context.USER_INPUT;
  	   console.log("dakhal");
     });
     
 };
   var latt=0 ;
   var lon=0 ;
   var locations = [];
   function myMap() {
		// directionsDisplay = new google.maps.DirectionsRenderer();
		var mapProp= {
		    // center:new google.maps.LatLng(30.0752169,31.0192871),
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

		  google.maps.event.addListener(map, 'idle', function() {
		    infowindow.open(map, marker);
		    google.maps.event.trigger(map,'resize');
		  });
		}

   function initialize() {
		  directionsDisplay = new google.maps.DirectionsRenderer();
		  var directionsDisplay;
		  var directionsService = new google.maps.DirectionsService();
		  var map = new google.maps.Map(document.getElementById('googleMap2'), {
		    zoom: 14,
		    center: new google.maps.LatLng(locations[locations.length-1].lat, locations[locations.length-1].long),
		    mapTypeId: google.maps.MapTypeId.ROADMAP
		  });
		  directionsDisplay.setMap(map);
		  var infowindow = new google.maps.InfoWindow();

		  	  
		   var marker1 = new google.maps.Marker({
		      position: new google.maps.LatLng(locations[0].lat, locations[0].long),
		      
		      map : map
		    });
		  var  marker2 = new google.maps.Marker({
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

		    // Send requests to service to get route (for stations count <= 25
			// only one request will be sent)
		    for (var i = 0; i < parts.length; i++) {
		        // Waypoints does not include first station (origin) and last
				// station (destination)
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

		    google.maps.event.addListener(map, 'idle', function() {
			    infowindow.open(map, marker1);
			    infowindow.open(map, marker2);
			    google.maps.event.trigger(map,'resize');
			  });
		  locations = [];
		}

	var ConversationController = function($rootScope, $scope, $location, conversationService) {
		var userInput = "";
		
// $scope.uploadFiles = function () {
// $scope.loading=true;
// $http({
// method: 'POST',
// url: 'rest/conversation/analyze',
// data: formdata,
// headers: {
// 'Content-Type': undefined
// }
// })
// .success(function(data, status) {
// $scope.results = data;
// $scope.loading=false;
// console.log("fghj");
// })
// .error(function(data, status) {
// console.debug("Error");
// });
// }
		var formdata = new FormData();
    	
        $scope.getTheFiles = function ($files) {
            angular.forEach($files, function (value, key) {
                formdata.append("file", value);
                console.log("dfghj");
            });
        };
        
        $scope.sendImage = function(){
        	
        	
            
            return sendImage($scope, formdata, conversationService);
        }
    	
		
		$scope.sendMessage = function () {
            return sendMessage($scope, conversationService);
        };
        
        $scope.sendDate = function(){
        	return sendDate($scope, conversationService);
        };
        
        $scope.onKeypress = function(event) {
        	var key = event.which;
        	if (key === 13) 
        		return sendMessage($scope, conversationService);
    	};
        	
    	conversationService.initChat("Tony").then(function (response) {
    		renderMessage(response.output.text, 'right');
    		$scope.USER_INPUT = response.context.USER_INPUT;
    		$scope.SHOW_LOCATION = response.context.SHOW_LOCATION;
    		
        });
    	if(userInput !="")
    		{
	    		conversationService.converse(userInput).then(function (response) {
	    			
	        		renderMessage(response.output.text, 'left');
// if($scope.SHOW_LOCATION){
// $scope.SHOW_LOCATION =false ;
// }
	        		$scope.SHOW_LOCATION = response.context.SHOW_LOCATION;
	        		if(SHOW_LOCATION){
	        			latt =response.context.LAT ;
	        			lon =response.context.LONG ;
	        			myMap () ;
	        			
	        		}
	        		console.log(SHOW_LOCATION);
	            });
    		}	
    };
    angular.module('conversation.controller', ['ngRoute', 'ngSanitize', 'conversation.service' ]).controller('ConversationController', ConversationController);
}());
