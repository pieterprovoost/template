app.controller('home', function($scope, $http) {

    $http.get('/api/user').success(function(data) {
        $scope.user = data;
    });

    $http.get('/api/users').success(function(data) {
        $scope.users = data;
    });

});