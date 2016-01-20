var app = angular.module("app", [ "ngRoute" ]).config(function($routeProvider) {

    $routeProvider.when("/home", {
        templateUrl : "home.html",
        controller : "home"
    }).when("/login", {
        templateUrl : "login.html",
        controller : "navigation"
    }).when("/admin", {
        templateUrl : "admin.html",
        controller : "admin"
    }).otherwise("/home");

});