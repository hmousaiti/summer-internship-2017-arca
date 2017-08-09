'use strict';
(function () {
    angular.module('conversation.service', [])
    .service('conversationService', function ($http, $q) {
    	var conversationContext;
    	
        var initChat = function (userName) {
        	conversationContext = {};
       	 	conversationContext["USERNAME"] = userName;
       	 	
       	 	var conversationObject = buildConversationObject("");
	       	return $http.post('rest/conversation/converse', conversationObject).then(function (response) {
	       		console.log(response);
	       		conversationContext = response.data.context;
	       		return response.data;
				}, function (errorResponse) {
	            });
        };
        
        var converse = function(userInput, fromMS, toMS){
        	console.log("skjdhaskldjalk");
        	var conversationObject;
        	if(fromMS && toMS){
        		conversationObject = buildConversationObjectWithDate(fromMS,toMS);
        	}
        	else{
        		conversationObject = buildConversationObject(userInput);
        	}
	       	return $http.post('rest/conversation/converse', conversationObject).then(function (response) {
	       		console.log(response);
	       		conversationContext = response.data.context;
	       		return response.data;
				}, function (errorResponse) {
	            });
        };
        
        var buildConversationObjectWithDate = function(fromMS,toMS){
        	conversationContext["FROM_DATE"] = fromMS;
        	conversationContext["TO_DATE"] = toMS;
	 
        	var text = {text : ""};
            var requestBody = {input: text, context: conversationContext};
            return requestBody;
        };
        
        var buildConversationObject = function(userInput){
        	var text = {text : userInput};
            var requestBody = {input: text, context: conversationContext};
            
            return requestBody;
        };
        
        return {
            'initChat': initChat,
            'converse': converse
        };
    });
}());