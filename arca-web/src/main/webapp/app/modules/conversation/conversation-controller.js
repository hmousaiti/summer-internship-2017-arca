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
//    		if(SHOW_LOCATION){
//    			$scope.SHOW_LOCATION =false ;
//    		}
    		$scope.SHOW_LOCATION = response.context.SHOW_LOCATION;
    		
    		console.log($scopeSHOW_LOCATION);
    		
    	});
        
    };
    var sendDate = function ($scope, conversationService) {
//       if ($scope.userInput.trim() === '') {
//           return;
//       }
//       $scope.userInput = "";
       
       renderMessage("FROM"+ $scope.fromDate +" TO "+ $scope.toDate, 'left');
       conversationService.converse(null, convertDateToMS($scope.fromDate), convertDateToMS($scope.toDate)).then(function (response) {
    	   renderMessage(response.output.text, 'right');
    	   $scope.USER_INPUT = response.context.USER_INPUT;
    	   
       });
       
   };
	var ConversationController = function($rootScope, $scope, $location, conversationService) {
		var userInput = "";
		
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
    		console.log(SHOW_LOCATION);
        });
    	if(userInput !="")
    		{
	    		conversationService.converse(userInput).then(function (response) {
	    			
	        		renderMessage(response.output.text, 'left');
//	        		if($scope.SHOW_LOCATION){
//	    			$scope.SHOW_LOCATION =false ;
//	    		}
	        		$scope.SHOW_LOCATION = response.context.SHOW_LOCATION;
	        		console.log(SHOW_LOCATION);
	            });
    		}	
    };
    angular.module('conversation.controller', ['ngRoute', 'ngSanitize', 'conversation.service' ]).controller('ConversationController', ConversationController);
}());
