'use strict';
(function () {
    angular.module('core.service', [])
    .service('coreService', function ($http, $q) {

        var me = function () {
       	 return $http.get('rest/user/me/double-vision-cam').then(function (response) {
       		 	userName = response.data.userName;
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
