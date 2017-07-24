'use strict';
(function () {
	var ConversationController = function($rootScope, $scope, $location, conversationService) {
		conversationService.me().then(function (response) {
    		console.log(response);
    		$scope.conversation = "dialog goes here";
    	});
    };
    angular.module('conversation.controller', ['ngRoute', 'ngSanitize', 'conversation.service' ]).controller('ConversationController', ConversationController);
}());
