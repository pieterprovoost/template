app.controller("admin", function($scope, $http) {

    $http.get("/api/users").success(function(data) {
        $scope.users = data;
    });

});