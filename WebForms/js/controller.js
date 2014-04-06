'use strict';

var app = angular.module('BodyApp',[]);

// Variable con el password encriptado del usuario logueado.
var pass = '';
var loggedName = '';
var loggedLastName = '';
var loggedEmail = '';


// Configuracion inicial de la aplicacion que controlara la pagina web.
app.config(function($locationProvider,$routeProvider){
	$locationProvider.html5Mode(true);
	$routeProvider.when('/',{controller:"offlineCtrl" , templateUrl:"/partials/creaCurso.html"});
    $routeProvider.when('/crearGrupo',{controller:"offlineCtrl" , templateUrl:"/partials/crearGrupo.html"});
    $routeProvider.when('/crearProfe',{controller:"offlineCtrl" , templateUrl:"/partials/creaProfe.html"});
    $routeProvider.when('/crearUsuario',{controller:"offlineCtrl" , templateUrl:"/partials/creaUsuario.html"});
    $routeProvider.when('/editarCurso',{controller:"offlineCtrl" , templateUrl:"/partials/editarCurso.html"});
    $routeProvider.when('/editarGrupo',{controller:"offlineCtrl" , templateUrl:"/partials/editarGrupo.html"});
    $routeProvider.when('/editarProfe',{controller:"offlineCtrl" , templateUrl:"/partials/editarProfe.html"});
    $routeProvider.when('/editarUsuario',{controller:"offlineCtrl" , templateUrl:"/partials/editarUsuario.html"});
    $routeProvider.when('/borrar',{controller:"offlineCtrl" , templateUrl:"/partials/borrar.html"});
    $routeProvider.otherwise({redirectTo:'/'});
});


app.config(['$httpProvider',function($httpProvider){
	$httpProvider.defaults.useXDomain = true;
	delete $httpProvider.defaults.headers.common['X-Requested-With'];	
}]);


// CONTROLADORES DE LAS DIFERENTES VISTAS.


// Controlador de cuando el usuario escogera el equipo para competir.
function competeCtrl($scope,$http,$templateCache,$location)
{

	$scope.logged = pass;
	$scope.loggedName = loggedName;
	$scope.loggedLastName = loggedLastName;

	// Funcion que cierra la actual sesion.
	$scope.cerrarSesion = function()
	{
		$location.path('/');
	}


	// Funcion que nos lleva a la pagina para crear equipo.
	$scope.creacion = function()
	{
		$location.path('/createam');
	}


	// Funcion que busca los equipos a los cuales forma parte el usuario logueado.
	$scope.findTeams = function()
	{
		var url = 'http://localhost:1212/viewteams/';
		var method = 'POST';
		var formData = {

			"email": loggedEmail
		};
		var jdata = JSON.stringify(formData);

		$http({
			method: method, 
			url: url, 
			data: jdata,
			header:{'Content-Type':'application/x-www-form-urlencoded'},
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.teams = data ;
		}).
		error(function(data,status)
		{
			$scope.teams = data || "Request Failed";
		});
	};


	$scope.findTeams();

}

// Controlador del main page cuando NO se haya iniciado sesion.
function offlineCtrl($scope,$http,$templateCache,$location,$window)
{

	// Funcion que obtiene las ultimas carreras para mostrarlas.
	$scope.getLastRaces = function()
	{	
		var url = 'http://localhost:1212/lastRaces/';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.carreras = data;
		}).
		error(function(data,status)
		{
			$scope.carreras = data || "Request Failed";
		});
	}


	// Funcion que inicia sesion.
	$scope.login = function()
	{
		var url = 'http://localhost:1212/jugador/';
		var pass = hex_md5($scope.correo + $scope.password);
		var method = 'POST';
		var formData = {

			"pass": pass 
		};
		var jdata = JSON.stringify(formData);

		$http({
			method: method, 
			url: url, 
			data: jdata,
			header:{'Content-Type':'application/x-www-form-urlencoded'},
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.usuarios = data;
			if($scope.usuarios.length > 0)
			{
				pass = $scope.usuarios[0].password;
				loggedName = $scope.usuarios[0].nombre;
				loggedLastName = $scope.usuarios[0].apellido1;
				loggedEmail = $scope.usuarios[0].email;
				$location.path('/main');
			}
			else
			{
				($window.mockWindow || $window).alert("ERROR: El usuario no esta registrado en el sistema.");
				$scope.correo = '';
				$scope.password = '';
			}
		}).
		error(function(data,status)
		{
			$scope.usuarios = data || "Request Failed";
		});
	};


	$scope.getLastRaces();

}


// Controlador cuando el usuario haya iniciado sesion.
function onlineCtrl($scope,$http,$templateCache,$location)
{

	$scope.logged = pass;
	$scope.loggedName = loggedName;
	$scope.loggedLastName = loggedLastName;
	$scope.loggedEmail = loggedEmail;

	$scope.cerrarSesion = function()
	{
		$location.path('/');
	};


	$scope.competir = function()
	{
		$location.path('/compete');
	};


	$scope.obtenerCorreos = function()
	{
		var url = 'http://localhost:1212/seemails/';
		var method = 'POST';
		var formData = {

			"email": $scope.loggedEmail 
		};
		var jdata = JSON.stringify(formData);

		$http({
			method: method, 
			url: url, 
			data: jdata,
			header:{'Content-Type':'application/x-www-form-urlencoded'},
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.destinos = data.correos;
		}).
		error(function(data,status)
		{
			$scope.destinos = data || "Request Failed";
		});
	};


	$scope.obtenerCorreos();

}


// Controlador de la pagina de registro.
function registerCtrl($scope,$http,$templateCache,$location,$window)
{

	// Funcion que registra a un jugador en la base de datos.
	$scope.registro = function()
	{

		if($scope.username == null || $scope.lastname1 == null || $scope.lastname2 == null || $scope.email == null || $scope.password == null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"nombre": $scope.username,
				"apellido1": $scope.lastname1,
				"apellido2": $scope.lastname2,
				"email": $scope.email,
				"password": hex_md5($scope.email + $scope.password),
				"pais": "Costa Rica",
			};

			var jdata = JSON.stringify(formData);

			$http({
				method: post,
				url: urlReg,
				data: jdata,
				header:{'Content-Type':'application/x-www-form-urlencoded'},
				cache: $templateCache
			}).
			success(function(response)
			{
				$scope.newUser = response;
				pass = $scope.newUser.password;
				loggedName = $scope.newUser.nombre;
				loggedLastName = $scope.newUser.apellido1;
				loggedEmail = $scope.newUser.email;
				($window.mockWindow || $window).alert("EXITO: El jugador ha sido registrado en BIG RACE. Ahora puedes competir.");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR: El correo electronico ya esta registrado en el sistema.");
				$location.path('/');
			});
		}
	};


	// Funcion que inicia sesion.
	$scope.login = function()
	{
		var urlLogin = 'http://localhost:1212/jugador/';
		var pass = hex_md5($scope.correo + $scope.contrasena);
		var post = 'POST';
		var formData = {

			"pass": pass 
		};
		var jdata = JSON.stringify(formData);

		$http({
			method: post, 
			url: urlLogin,
			data: jdata,
			header:{'Content-Type':'application/x-www-form-urlencoded'}, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.usuarios = data;	
			if($scope.usuarios.length > 0)
			{
				pass = $scope.usuarios[0].password;
				loggedName = $scope.usuarios[0].nombre;
				loggedLastName = $scope.usuarios[0].apellido1;
				$location.path('/main');
			}
			else
			{
				($window.mockWindow || $window).alert("ERROR: El usuario no esta registrado en el sistema.");
				$scope.correo = '';
				$scope.password = '';
			}
		}).
		error(function(data,status)
		{
			$scope.usuarios = data || "Request Failed";
		});
	};

}



// Controlador de la pagina donde se crea un equipo.
function teamCtrl($scope,$http,$templateCache,$location,$window)
{

	$scope.logged = pass;
	$scope.loggedName = loggedName;
	$scope.loggedLastName = loggedLastName;
	$scope.loggedEmail = loggedEmail;

	// Funcion que cierra la actual sesion.
	$scope.cerrarSesion = function()
	{
		$location.path('/');
	}


	$scope.competir = function()
	{
		$location.path('/compete');
	}


	// Funcion que registra a un jugador en la base de datos.
	$scope.createTeam = function()
	{

		if($scope.teamname == null || $scope.logourl == null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder terminar la creacion.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/createam';
			var formData = {
				"nombre": $scope.teamname,
				"logo": $scope.logourl,
				"email": $scope.loggedEmail
			};

			var jdata = JSON.stringify(formData);

			$http({
				method: post,
				url: urlReg,
				data: jdata,
				header:{'Content-Type':'application/x-www-form-urlencoded'},
				cache: $templateCache
			}).
			success(function(response)
			{
				$scope.newTeam = response;
				($window.mockWindow || $window).alert("EXITO: El equipo esta listo para unirse a las carreras.");
				$location.path('/compete');
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR: El nombre del equipo ya existe en el sistema.");
				$location.path('/createam');
			});
		}
	};

}


