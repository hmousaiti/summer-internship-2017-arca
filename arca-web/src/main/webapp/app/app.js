'use strict';
(function () {
  angular.module('app', [
    'ngRoute',
    'core',
    'conversation',
    'find-my-car'
  ])
  .config(['$locationProvider',
                
        function($locationProvider) {

			$locationProvider.html5Mode(true);
    	}
	])
    .config(function ($routeProvider) {
      $routeProvider.when('/', {
          'templateUrl': 'app/modules/core/core.html',
          'reloadOnSearch': false
      }).when('/analyze', {
          'templateUrl': 'app/modules/core/analyze.html',
          'reloadOnSearch': false
      }).when('/conversation', {
          'templateUrl': 'app/modules/conversation/conversation.html',
          'reloadOnSearch': false
      }).when('/find-my-car', {
          'templateUrl': 'app/modules/find-my-car/find-my-car.html',
          'reloadOnSearch': false
      }).otherwise({ 'redirectTo': '/' });;
    })
    .run(function ($rootScope) {
    });
}());
