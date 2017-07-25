'use strict';
(function () {
	var FindMyCarController = function($rootScope, $scope, $location, findMyCarService) {
    	findMyCarService.me().then(function (response) {
    		console.log(response);
    		$scope.location = "my car location";
    	});
    };
    angular.module('find-my-car.controller', ['ngRoute', 'ngSanitize', 'find-my-car.service' ]).controller('FindMyCarController', FindMyCarController);
}());
