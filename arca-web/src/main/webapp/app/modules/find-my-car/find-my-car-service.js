'use strict';
(function () {
    angular.module('find-my-car.service', [])
    .service('findMyCarService', function ($http, $q) {

        var me = function () {
       	 return $http.get('rest/user/location').then(function (response) {
       		 	
       			return response.data;
			}, function (errorResponse) {
	       	 	console.log("Unable to load user data.");
	       	 	return {"userName":"John Doe","profileImageTitle":"Profile_John Doe.png"};
            });
        };
        
        return {
            'me': me
        };
    });
}());
