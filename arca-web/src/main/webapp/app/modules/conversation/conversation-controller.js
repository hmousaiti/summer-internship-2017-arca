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
    var sendMessage = function ($scope, conversationService) {
    	var userInput = $scope.userInput;
        if ($scope.userInput.trim() === '') {
            return;
        }
        $scope.userInput = "";
        
        renderMessage(userInput, 'left');

        conversationService.me().then(function (response) {
        	renderMessage("message from conversation", 'right');
    	});
    };
	var ConversationController = function($rootScope, $scope, $location, conversationService) {
        $scope.sendMessage = function () {
            return sendMessage($scope, conversationService);
        };
        
        $scope.onKeypress = function(event) {
        	var key = event.which;
        	if (key === 13) 
        		return sendMessage($scope, conversationService);
        	};
    };
    angular.module('conversation.controller', ['ngRoute', 'ngSanitize', 'conversation.service' ]).controller('ConversationController', ConversationController);
}());
