'use strict';

var app = angular.module('BodyApp',[]);

// Variable con el password encriptado del usuario logueado.
var pass = '';


// Configuracion inicial de la aplicacion que controlara la pagina web.
app.config(function($locationProvider,$routeProvider){
	$locationProvider.html5Mode(true);
	$routeProvider.when('/',{controller:"loginCtrl" , templateUrl:"/partials/login.html"});
    $routeProvider.when('/crearCurso',{controller:"crearCursoCtrl" , templateUrl:"/partials/creaCurso.html"});
    $routeProvider.when('/crearGrupo',{controller:"crearGrupoCtrl" , templateUrl:"/partials/crearGrupo.html"});
    $routeProvider.when('/crearProfe',{controller:"crearProfeCtrl" , templateUrl:"/partials/creaProfe.html"});
    $routeProvider.when('/crearUsuario',{controller:"crearUsuarioCtrl" , templateUrl:"/partials/creaUsuario.html"});
    
    $routeProvider.when('/editarCurso',{controller:"editarCursoCtrl" , templateUrl:"/partials/editarCurso.html"});
    $routeProvider.when('/editarGrupo',{controller:"editarGrupoCtrl" , templateUrl:"/partials/editarGrupo.html"});
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



function loginCtrl($scope,$http,$templateCache,$location,$window)
{
    
	$scope.registro = function()
	{
        var passT = hex_md5($scope.username + $scope.password);
        
		if($scope.username == null || $scope.password== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://felialoismobile.herokuapp.com/login/administrador';
			var formData = {
                "password": passT,
			};

			var jdata = JSON.stringify(formData);

			$http({
				method: post,
				url: urlReg,
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
                ($window.mockWindow || $window).alert("Conectado con exito");
                
				$location.path('/crearCurso');
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
		}
	};
}


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
			var urlReg = 'http://felialoismobile.herokuapp.com/add/course';
			var formData = {
				"nombre": $scope.nombre,
                "codigo": $scope.codigo,
				"creditos": $scope.creditos,
				"escuela": $scope.escuela,
                "password":pass,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/crearCurso');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/crearCurso');
			});
		}
	};
}

//controlador de crear profesor 
function crearProfeCtrl($scope,$http,$templateCache,$location,$window)
{
	$scope.registro = function()
	{
        //($window.mockWindow || $window).alert(pass);
		if($scope.nombre == null || $scope.apellidos== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://felialoismobile.herokuapp.com/add/professor';
			var formData = {
				"nombre": $scope.apellidos+" "+$scope.nombre,
                "password":pass,
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
				
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/crearProfe');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/crearProfe');
			});
		}
	};
}

//controlador de crear grupo
function crearGrupoCtrl($scope,$http,$templateCache,$location,$window)
{
    $scope.getCursos = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/courses';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.cursos = data;
		}).
		error(function(data,status)
		{
			$scope.cursos = data || "Request Failed";
		});
	}  
    $scope.getCursos();
    
    $scope.getProfesores = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/professors';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.profesores = data;
		}).
		error(function(data,status)
		{
			$scope.profesores = data || "Request Failed";
		});
	}
    $scope.getProfesores();
    
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
			var urlReg = 'http://felialoismobile.herokuapp.com/addplayer';
			var formData = {
				"numero": $scope.numero,
				"aula": $scope.aula,
                "password":pass,
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
				$location.path('/crearGrupo');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/crearGrupo');
			});
		}
	};
}

//controlador de crear usuario

function crearUsuarioCtrl($scope,$http,$templateCache,$location,$window)
{
	$scope.registro = function()
	{

		if($scope.email == null || $scope.password== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://felialoismobile.herokuapp.com/add/user';
			var formData = {
                "correo":$scope.email,
				"username": $scope.username,
                "password": hex_md5($scope.email + $scope.password),
                "facebook":"false",
                "passwordADM":pass,
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
				
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/crearUsuario');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/crearUsuario');
			});
		}
	};
}



//controlador de editar curso
function editarCursoCtrl($scope,$http,$templateCache,$location,$window)
{
    $scope.getCursos = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/courses';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.cursos = data;
		}).
		error(function(data,status)
		{
			$scope.cursos = data || "Request Failed";
		});
	}  
    $scope.getCursos();
    $scope.registro = function()
	{
        
		if($scope.nombre == null || $scope.creditos== null || $scope.escuela == null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://felialoismobile.herokuapp.com/update/course';
            var selectedID= document.getElementById("slCurso").value;
            //alert(selectedID);
            
			var formData = {
                "codigo": selectedID,
				"nombre": $scope.nombre,
				"creditos": $scope.creditos,
				"escuela": $scope.escuela,
                "password":pass,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/editarCurso');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/editarCurso');
			});
		}
	};
}


//controlador de editar grupo
function editarGrupoCtrl($scope,$http,$templateCache,$location,$window)
{
    $scope.getGrupos = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/groups';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.grupos = data;
		}).
		error(function(data,status)
		{
			$scope.grupos = data || "Request Failed";
		});
	}  
    $scope.getGrupos();
    $scope.getCursos = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/courses';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.cursos = data;
		}).
		error(function(data,status)
		{
			$scope.cursos = data || "Request Failed";
		});
	}
    
    $scope.getCursos();
    
    $scope.getProfesores = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/professors';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.profesores = data;
		}).
		error(function(data,status)
		{
			$scope.profesores = data || "Request Failed";
		});
	}
    $scope.getProfesores();

    $scope.registro = function()
	{
        
		if($scope.sede==null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://felialoismobile.herokuapp.com/update/group';
            var selectedPRF= document.getElementById("slProfe").value;
            var selectedID= document.getElementById("slGrupo").value;
            var selectedNUM= document.getElementById("slNum").value;

			var formData = {
                "curso":selectedID,
                "numero":selectedNUM,
                "profesor":selectedPRF,
				"sede": $scope.sede,
                "password":pass,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/editarCurso');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/editarCurso');
			});
		}
	};
}

//controlador de editar profesor
function editarProfeCtrl($scope,$http,$templateCache,$location,$window)
{
    $scope.getProfesores = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/professors';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.profesores = data;
		}).
		error(function(data,status)
		{
			$scope.profesores = data || "Request Failed";
		});
	}
    $scope.getProfesores();
    $scope.registro = function()
	{
        
		if($scope.nombre == null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://felialoismobile.herokuapp.com/update/professor';
            var selectedID= document.getElementById("slProfesor").value;
			var formData = {
                "nombre": selectedID,
				"nuevo": $scope.apellidos+" "+$scope.nombre,
                "password":pass,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/editarProfe');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/editarProfe');
			});
		}
	};
}

//controlador de editar Usuario
function editarUsuarioCtrl($scope,$http,$templateCache,$location,$window)
{
    $scope.getUsuarios = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/users';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.usuarios = data;
		}).
		error(function(data,status)
		{
			$scope.usuarios = data || "Request Failed";
		});
	}
    $scope.getUsuarios();
    $scope.registro = function()
	{
        
		if($scope.username == null || $scope.password== null)
		{
			($window.mockWindow || $window).alert("ATENCION: Todos los campos deben estar llenos para poder realizar el registro.");
		}
		else{
			var post = 'POST';
			var urlReg = 'http://felialoismobile.herokuapp.com/update/user';
            var selectedID= document.getElementById("slUsuario").value;
			var formData = {
                "correo":selectedID,
				"username": $scope.username,
				"password": $scope.password,
                "facebook":"false",
                "passwordADM":pass,
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
				($window.mockWindow || $window).alert("EXITO");
				$location.path('/editarUsuario');	
			}).
			error(function(response)
			{	
				$scope.codeStatus = response || "Request failed";
				($window.mockWindow || $window).alert("ERROR");
				$location.path('/editarUsuario');
			});
		}
	};
}

//controller de borrar
function borrarCtrl($scope,$http,$templateCache,$location,$window)
{
    $scope.getUsuarios = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/users';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.datos = data;
		}).
		error(function(data,status)
		{
			$scope.datos = data || "Request Failed";
		});
	}
     $scope.getGrupos = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/groups';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.datos = data;
		}).
		error(function(data,status)
		{
			$scope.datos = data || "Request Failed";
		});
	}  
    $scope.getCursos = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/courses';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.datos = data;
		}).
		error(function(data,status)
		{
			$scope.datos = data || "Request Failed";
		});
	}
    $scope.getProfesores = function()
	{	
		var url = 'http://felialoismobile.herokuapp.com/professors';
		var method = 'POST';

		$http({
			method: method, 
			url: url, 
			cache: $templateCache
		}).
		success(function(data,status)
		{
			$scope.datos = data;
		}).
		error(function(data,status)
		{
			$scope.datos = data || "Request Failed";
		});
	}

    $scope.tipos = [{"id":"0", "nombre": "Curso" }, {"id":"1", "nombre": "Grupo" }, {"id":"2", "nombre": "Profesor" },{"id":"3", "nombre": "Usuario" }];
    $scope.seleccion = function()
	{
            var selected=document.getElementById("slTipos").value;
            switch(selected){
                    case "Curso":
                        $scope.getCursos();
                        break;
                    case "Grupo":
                        $scope.getGrupos();
                        break;
                    case "Profesor":
                        $scope.getProfesores();
                        break;
                    case "Usuario":
                        $scope.getUsuarios();
                        break;
            }
		
	};
    $scope.register = function(){
        
        
    }
}

