'use strict';
(function () {
	var CoreController = function($rootScope, $scope, $location, coreService) {
    	coreService.me().then(function (response) {
    		$scope.user = response;
    	});
    };
    angular.module('core.controller', ['ngRoute', 'ngSanitize', 'core.service' ]).controller('CoreController', CoreController);
}());
