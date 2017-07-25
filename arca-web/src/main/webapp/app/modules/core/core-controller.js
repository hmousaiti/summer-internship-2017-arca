
'use strict';
(function () {
	var CoreController = function($rootScope, $scope, $http, coreService) {
    	coreService.me().then(function (response) {
    		$scope.user = response;
    	});
    	
    	var formdata = new FormData();
        $scope.getTheFiles = function ($files) {
            angular.forEach($files, function (value, key) {
                formdata.append("file", value);
            });
        };

        $scope.uploadFiles = function () {

        	$http({ 
        				method: 'POST', 
        				url: 'rest/visual/analyze',
        				data: formdata,
        				headers: {
                          'Content-Type': undefined
                      }
        			})
            .success(function(data, status) {
                $scope.results = data;
             })
            .error(function(data, status) {
                console.debug("Error");
            });
        }
    };
    angular.module('core.controller', ['ngRoute', 'ngSanitize', 'core.service' ])
    .directive('ngFiles', ['$parse', function ($parse) {

        function fn_link(scope, element, attrs) {
            var onChange = $parse(attrs.ngFiles);
            element.on('change', function (event) {
                onChange(scope, { $files: event.target.files });
            });
        };

        return {
            link: fn_link
        }
    }])
    .controller('CoreController', CoreController);
}());
