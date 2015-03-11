var app = angular.module('app', [ 'ngRoute' ]).config(function($routeProvider) {

    $routeProvider.when('/home', {
        templateUrl : 'home.html',
        controller : 'home'
    }).when('/login', {
        templateUrl : 'login.html',
        controller : 'navigation'
    }).otherwise('/home');

});