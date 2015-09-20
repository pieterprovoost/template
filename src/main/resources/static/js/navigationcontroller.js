app.controller('navigation', function($rootScope, $scope, $http, $location) {

    $scope.getClass = function(path) {
        if ($location.path().substr(0, path.length) == path) {
            return "active"
        } else {
            return ""
        }
    };

    // Tries to load a resource when the page is loaded to check if the user
    // is already authenticated. Sets or unsets the authenticated flag.
    // When called from login() the callback navigates to either home or login.

    var authenticate = function(callback) {
        $http.get('/api/user').success(function(data) {
            if (data.username) {
                $rootScope.authenticated = true;
            } else {
                $rootScope.authenticated = false;
            }
            callback && callback();
        }).error(function() {
            $rootScope.authenticated = false;
            callback && callback();
        });
    };

    // Sends the credentials and accepts a cookie in return.

    $scope.login = function() {
        $http.post('login', $.param($scope.credentials), {
            headers : {
                "content-type" : "application/x-www-form-urlencoded"
            }
        }).success(function(data) {
            authenticate(function() {
                if ($rootScope.authenticated) {
                    $location.path("/home");
                    $scope.error = false;
                } else {
                    $location.path("/login");
                    $scope.error = true;
                }
            });
        }).error(function(data) {
            $location.path("/login");
            $scope.error = true;
            $rootScope.authenticated = false;
        })
    };

    $scope.logout = function() {
        $http.post('logout', {}).success(function() {
            $rootScope.authenticated = false;
            $location.path("/home");
        }).error(function(data) {
            $rootScope.authenticated = false;
        });
    };

    $scope.credentials = {};

    authenticate();

});