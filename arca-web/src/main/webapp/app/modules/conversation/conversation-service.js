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
        	//console.log("skjdhaskldjalk");
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
        var uploadFiles = function (formdata) {

        	var text = {text : "image"};
            var requestBody = {input: text, context: conversationContext};
            
        	formdata.append("body", JSON.stringify(requestBody));
        	return $http.post('rest/conversation/analyze', formdata,{headers:{'Content-Type': undefined}}).then(function (response) {
	       		console.log(response);
	       		console.log("reg33")
	       		conversationContext = response.data.context;
	       		return response.data;
				}, function (errorResponse) {
	            });
        }
        
        var buildConversationObjectWithDate = function(fromMS,toMS){
        	conversationContext["FROM_DATE"] = fromMS + "";
        	conversationContext["TO_DATE"] = toMS + "";
	 
        	var text = {text : "fromto"};
            var requestBody = {input: text, context: conversationContext};
            return requestBody;
        };
        
        var buildConversationObject = function(userInput){
        	var text = {text : userInput};
            var requestBody = {input: text, context: conversationContext};
            
            return requestBody;
        };
        
        var buildConversationObjectWithImg= function(formdata){
        	formdata.append("body", conversationContext);
        	console.log(formdata);
        };
        
        return {
            'initChat': initChat,
            'converse': converse,
            'uploadFiles':uploadFiles
        };
    });
}());