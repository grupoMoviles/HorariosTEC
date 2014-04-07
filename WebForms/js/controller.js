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
	$routeProvider.when('/',{controller:"crearCursoCtrl" , templateUrl:"/partials/creaCurso.html"});
    $routeProvider.when('/crearCurso',{controller:"crearCursoCtrl" , templateUrl:"/partials/creaCurso.html"});
    $routeProvider.when('/crearGrupo',{controller:"crearGrupoCtrl" , templateUrl:"/partials/crearGrupo.html"});
    $routeProvider.when('/crearProfe',{controller:"crearProfeCtrl" , templateUrl:"/partials/creaProfe.html"});
    $routeProvider.when('/crearUsuario',{controller:"crearUsuarioCtrl" , templateUrl:"/partials/creaUsuario.html"});
    
    $routeProvider.when('/editarCurso',{controller:"editarCursoCtrl" , templateUrl:"/partials/editarCurso.html"});
    $routeProvider.when('/editarGrupo',{controller:"crearGrupoCtrl" , templateUrl:"/partials/editarGrupo.html"});
    $routeProvider.when('/editarProfe',{controller:"editarProfeCtrl" , templateUrl:"/partials/editarProfe.html"});
    $routeProvider.when('/editarUsuario',{controller:"editarUsuarioCtrl" , templateUrl:"/partials/editarUsuario.html"});
    $routeProvider.when('/borrar',{controller:"borrarCtrl" , templateUrl:"/partials/borrar.html"});
    $routeProvider.otherwise({redirectTo:'/'});
});


app.config(['$httpProvider',function($httpProvider){
	$httpProvider.defaults.useXDomain = true;
	delete $httpProvider.defaults.headers.common['X-Requested-With'];	
}]);


// CONTROLADORES DE LAS DIFERENTES VISTAS.

//controlador de crear curso
function crearCursoCtrl($scope,$http,$templateCache,$location,$window)
{
	$scope.registro = function()
	{

		if($scope.nombre == null || $scope.creditos== null || $scope.escuela == null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"nombre": $scope.nombre,
				"creditos": $scope.creditos,
				"escuela": $scope.escuela,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/');
			});
		}
	};
}

//controlador de crear profesor 
function crearProfeCtrl($scope,$http,$templateCache,$location,$window)
{
	$scope.registro = function()
	{

		if($scope.nombre == null || $scope.apellidos== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"nombre": $scope.nombre,
				"apellidos": $scope.apellidos,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/');
			});
		}
	};
}

//controlador de crear grupo
function crearGrupoCtrl($scope,$http,$templateCache,$location,$window)
{
    //TODO: HACER EL GET DE LOS CURSOS
    $scope.cursos = [{ "nombre": "Elementos de computacion" }, { "nombre": "Aplicaciones Moviles" }];
    
    //TODO: HAcer el GET DE LOS PROFES
    $scope.profesores = [{ "nombre": "Carlos Benavides" }, { "nombre": "Rodrigo Nuñez" }];
    
    //LLENADO DE LAS HORAS y los dias
    $scope.dias =[{"nombre": "Lunes"}, {"nombre": "Martes"}, {"nombre": "Miércoles"}, {"nombre": "Jueves"}, {"nombre": "Viernes"},
                  {"nombre": "Sabado"}];
    $scope.horas =[{"h": "7"},{"h": "8"},{"h": "9"},{"h": "10"},{"h": "11"},{"h": "12"},{"h": "13"},{"h": "14"},{"h": "15"},{"h": "16"},{"h": "17"},{"h": "18"},{"h": "19"},{"h": "20"},{"h": "21"}];
    $scope.minutos=[{"m":"00"},{"m":"20"},{"m":"30"},{"m":"50"}];
    
    
    $scope.registro = function()
	{
        
        
		if($scope.numero == null || $scope.aula== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"numero": $scope.numero,
				"aula": $scope.aula,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/');
			});
		}
	};
}

//controlador de crear usuario

function crearUsuarioCtrl($scope,$http,$templateCache,$location,$window)
{
	$scope.registro = function()
	{

		if($scope.correo == null || $scope.password== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"correo": $scope.username,
				"password": $scope.password,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/');
			});
		}
	};
}



//controlador de editar curso
function editarCursoCtrl($scope,$http,$templateCache,$location,$window)
{
    //TODO: HACER EL GET DE LOS CURSOS
    $scope.cursos = [{ "nombre": "Elementos de computacion" }, { "nombre": "Aplicaciones Moviles" }];
    
    $scope.registro = function()
	{
        
		if($scope.nombre == null || $scope.creditos== null || $scope.escuela == null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"nombre": $scope.nombre,
				"creditos": $scope.creditos,
				"escuela": $scope.escuela,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/');
			});
		}
	};
}


//controlador de editar grupo
function editarGrupoCtrl($scope,$http,$templateCache,$location,$window)
{
    //TODO: HACER EL GET DE LOS GRUPOS
    $scope.grupos = [{ "numero":"2" }, { "numero":"3" }];
    
    //TODO: HACER EL GET DE LOS CURSOS
    $scope.cursos = [{ "nombre": "Elementos de computacion" }, { "nombre": "Aplicaciones Moviles" }];
    
    //TODO: HAcer el GET DE LOS PROFES
    $scope.profesores = [{ "nombre": "Carlos Benavides" }, { "nombre": "Rodrigo Nuñez" }];
    
    //LLENADO DE LAS HORAS y los dias
    $scope.dias =[{"nombre": "Lunes"}, {"nombre": "Martes"}, {"nombre": "Miércoles"}, {"nombre": "Jueves"}, {"nombre": "Viernes"},
                  {"nombre": "Sabado"}];
    $scope.horas =[{"h": "7"},{"h": "8"},{"h": "9"},{"h": "10"},{"h": "11"},{"h": "12"},{"h": "13"},{"h": "14"},{"h": "15"},{"h":       "16"},{"h": "17"},{"h": "18"},{"h": "19"},{"h": "20"},{"h": "21"}];
    $scope.minutos=[{"m":"00"},{"m":"20"},{"m":"30"},{"m":"50"}];
    
    
    $scope.registro = function()
	{
        
		if($scope.numero == null || $scope.aula== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"numero": $scope.numero,
				"aula": $scope.aula,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/');
			});
		}
	};
}

//controlador de editar profesor
function editarProfeCtrl($scope,$http,$templateCache,$location,$window)
{

    //TODO: HAcer el GET DE LOS PROFES
    $scope.profesores = [{ "nombre": "Carlos Benavides" }, { "nombre": "Rodrigo Nuñez" }];

    $scope.registro = function()
	{
        
		if($scope.nombre == null || $scope.apellidos== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"nombre": $scope.numero,
				"apellidos": $scope.aula,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/');
			});
		}
	};
}

//controlador de editar Usuario
function editarUsuarioCtrl($scope,$http,$templateCache,$location,$window)
{

    //TODO: HAcer el GET DE LOS USUARIOS
    $scope.usuarios = [{ "username": "honCBC" }, { "username": "meerkat" }];

    $scope.registro = function()
	{
        
		if($scope.correo == null || $scope.password== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"correo": $scope.username,
				"password": $scope.password,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/');
			});
		}
	};
}

//controller de borrar
//controlador de editar Usuario
function borrarCtrl($scope,$http,$templateCache,$location,$window)
{

    $scope.tipos = [{ "nombre": "Curso" }, { "nombre": "Grupo" }, { "nombre": "Profesor" },{ "nombre": "Usuario" }];

    $scope.registro = function()
	{

			var post = 'POST';
			var urlReg = 'http://localhost:1212/addplayer';
			var formData = {
				"numero": $scope.numero,
				"aula": $scope.aula,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/main');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/');
			});
	};
}

