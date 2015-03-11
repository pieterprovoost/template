app.controller('home', function($scope, $http) {
    $http.get('/api/currentuser/').success(function(data) {
        $scope.user = data;
    })
});