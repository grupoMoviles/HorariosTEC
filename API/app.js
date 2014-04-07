// Se definen todas las variables iniciales.
var ruta_aplicacion = __dirname,
	express 	= require('express'),
	app 		= express(),
	path		= require('path'),
	http		= require('http'),
	server 		= http.createServer(app);
	guiaDeHorarios = 'guiaDeHorariosPrueba',
	colecciones	= ['usuarios','grupos','cursos','profesores','administradores'],
	cons 		= require('consolidate'),
	db 			= require('mongojs').connect(guiaDeHorarios,colecciones);

// Variable que permite el CORS.
var allowCrossDomain = function(req,res,next)
{
	res.header("Access-Control-Allow-Origin","*");
	res.header("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS");
	res.header("Access-Control-Allow-Headers","Content-Type,Authorization,X-Requested-With,Content-Length");
	
	
	if('OPTIONS'==req.method)
	{
		res.send(200);
	}	
	else
	{
		next();
	}
}

// Variable que se encargara del manejo de los errores.
var errorHandler = function(err,req,res,next)
{
	console.error(err.message);
	console.error(err.stack);
	res.status(500);
	res.render('error_template',{error:err});
}

// Configuracion inicial de la aplicacion.
app.configure(function(){

	app.engine('html',cons.swig);
	app.set('view engine','html');
	app.set('views',__dirname + '/views');
	app.use(allowCrossDomain);
	app.use(express.bodyParser());
	app.use(express.methodOverride());
	app.use(app.router);
	app.use(express.static(path.join(ruta_aplicacion+"/public")));
	app.use(errorHandler);

});

process.env.NODE_ENV = 'development';


// Inicia la aplicacion que contiene el API
app.get('/',function(req,res){
	res.send("API working on the GUIA DE HORARIOS mongo database.");	
});



// FUNCIONES QUE CONECTAN CON LA BASE DE DATOS



// Devolver todos los usuarios del sistema.
// Entradas: Ninguna.
// Salidas: Un arreglo con todos los usuarios.
findAllUsers = function(req,res)
{
	db.usuarios.find({},function(err,users)
	{
		if(err || !users)
			res.send(400);
		else
			res.json(users);
	});
};


// Encontrar un usuario por medio del correo electronico.
// Entradas: Correo Electronico.
// Salidas: Documento JSON con el usuario relacionado a esa direccion de correo electronico.
findUser = function(req,res)
{
	var email = req.param("correo");
	db.usuarios.find({"correo":email},function(err,usuario){
		if(err || !usuario){
			res.send(400);
		}
		else
		{
			res.json(usuario);
		}
	});
}



// Devolver todos los profesores del sistema.
// Entradas: Ninguna.
// Salidas: Un arreglo con todos los profesores.
findAllProfessors = function(req,res)
{
	db.profesores.find({},function(err,professors)
	{
		if(err || !professors)
			res.send(400);
		else
			res.json(professors);
	});
};






// Encontrar un profesor por medio del nombre.
// Entradas: Nombre del profesor por buscar.
// Salidas: Toda la informacion del profesor en un JSON.
findProfessor = function(req,res)
{
	var name = req.param("nombre");
	db.profesores.find({"nombre":name},function(err,professor){
		if(err || !professor){
			res.send(400);
		}
		else
		{
			res.json(professor);
		}
	});
}


// Devolver todos los grupos del sistema.
// Entradas: Ninguna.
// Salidas: Un arreglo con todos los grupos.
findAllGroups = function(req,res)
{
	db.grupos.find({},function(err,groups)
	{
		if(err || !groups)
			res.send(400);
		else
			res.json(groups);
	});
};


// Devolver todos los cursos del sistema.
// Entradas: Ninguna.
// Salidas: Un arreglo con todos los cursos.
findAllCourses = function(req,res)
{
	db.cursos.find({},function(err,courses)
	{
		if(err || !courses)
			res.send(400);
		else
			res.json(courses);
	});
};



// Encontrar un curso por medio del codigo.
// Entradas: Codigo del curso buscado.
// Salidas: Toda la informacion del curso en un JSON.
findCourse = function(req,res)
{
	var code = req.param("codigo");
	db.cursos.find({"codigo":code},function(err,course){
		if(err || !course){
			res.send(400);
		}
		else
		{
			res.json(course);
		}
	});
}



// Encontrar los cursos de cierta escuela.
// Entradas: Codigo de la escuela.
// Salidas: Todos los cursos impartidos por dicha escuela.
findCoursesBySchool = function(req,res)
{
	var code = req.param("escuela");
	db.cursos.find({"escuela":code},function(err,courses){
		if(err || !courses){
			res.send(400);
		}
		else
		{
			res.json(courses);
		}
	});
}



// Encontrar todos los grupos de un solo curso.
// Entradas: El codigo del curso.
// Salidas: Todos los grupos disponibles para dicho curso.
findGroupsByCourse = function(req,res)
{
	var course = req.param("curso");
	db.grupos.find({"curso":course},function(err,groups){
		if(err || !groups){
			res.send(400);
		}
		else
		{
			res.json(groups);
		}
	});
}


// Encontrar un grupo de acuerdo al curso y al numero de grupo.
// Entradas: Codigo del curso y numero del grupo.
// Salidas: Toda la informacion del grupo en un JSON.
findGroupByNumber = function(req,res)
{
	var groupNumber = req.param("numero");
	var course = req.param("curso");
	db.grupos.find({"curso":course,"numero":groupNumber},function(err,group){
		if(err || !group){
			res.send(400);
		}
		else
		{
			res.json(group);
		}
	});
}







// Agregar un profesor al sistema.
// Entradas: Nombre del profesor.
// Salidas: El JSON del profesor.
addProfessor = function(req,res,next){

	var name = req.param("nombre");
	
	db.profesores.find({"nombre":name},function(err,professors){
		if(professors.length > 0)
		{
			res.send(400);
		}
		else
		{
			db.profesores.save({"nombre":name,"cantidadCalificaciones":0,"calificacionTotal":0},function(err,saved){
				if(err || !saved)
				{
					res.send(400);
				}
				else
				{
					res.json(saved);
				}
			});		
		}
	});
};



// Agrega un curso al sistema.
// Entradas: Nombre, codigo, cantidad de creditos y escuela del curso.
// Salidas: JSON con la informacion del curso.
addCourse = function(req,res,next){

	var name = req.param("nombre");
	var code = req.param("codigo");
	var school = req.param("escuela");
	var credits = req.param("creditos");
	
	db.cursos.find({"codigo":code},function(err,courses){
		if(courses.length > 0)
		{
			res.send(400);
		}
		else
		{
			db.cursos.save({"nombre":name,"codigo":code,"escuela":school,"creditos":credits},function(err,saved){
				if(err || !saved)
				{
					res.send(400);
				}
				else
				{
					res.json(saved);
				}
			});		
		}
	});
};


// Agrega un grupo a la base de datos.
// Entradas: Numero, curso, profesor, horario y sede del grupo agregado.
// Salidas: La informacion del grupo en un JSON.
addGroup = function(req,res,next){

	var sede = req.param("sede");
	var number = req.param("numero");
	var course = req.param("curso");
	var schedule = req.param("horario");
	var professor = req.param("profesor");
	
	db.grupos.find({"curso":course,"numero":number},function(err,groups){
		if(groups.length > 0)
		{
			res.send(400);
		}
		else
		{
			db.grupos.save({"curso":course,"horario":schedule,"sede":sede,"numero":number,"profesor":professor},function(err,saved){
				if(err || !saved)
				{
					res.send(400);
				}
				else
				{
					res.json(saved);
				}
			});		
		}
	});
};





// Agrega un usuario al sistema.
// Entradas: Nombre, password, correo electronico y facebook del usuario.
// Salidas: JSON del usuario creado.
addUser = function(req,res,next){

	var username = req.param("username");
	var pass = req.param("password");
	var email = req.param("correo");
	var face = req.param("facebook");
	
	db.usuarios.find({"correo":email},function(err,users){
		if(users.length > 0)
		{
			res.send(400);
		}
		else
		{
			db.usuarios.find({"username":username},
				function(err,users2){
					if(users2.length > 0)
					{
						res.send(400);
					}
					else{
						db.usuarios.save({"username":username,"correo":email,"password":pass,"facebook":face,"grupos":[],"cursos":[]},function(err,saved){
							if(err || !saved)
								res.send(400);
							else
								res.json(saved);
						});
					}
				});
				
		}
	});
};








// Elimina grupo introduciendo curso y numero de grupo.
// Entradas: El codigo del curso y el numero del grupo.
// Salidas: Nada.
deleteGroup = function(req,res)
{
	var course = req.param("curso");
	var number = req.param("numero");
	db.grupos.remove({"curso":course,"numero":number},false,function(err,deleted){
		res.send(200);
	});
};


// Elimina el curso introduciendo su codigo.
// Entradas: El codigo del curso.
// Salidas: Nada.
deleteCourse = function(req,res)
{
	var code = req.param("codigo");
	db.cursos.remove({"codigo":code},false,function(err,deleted){
		res.send(200);
	});
};


// Elimina un usuario al introducir su correo electronico.
// Entradas: El correo del usuario por eliminar.
// Salidas: Nada.
deleteUser = function(req,res)
{
	var email = req.param("correo");
	db.usuarios.remove({"correo":email},false,function(err,deleted){
		res.send(200);
	});
};


// Elimina un profesor introduciendo su respectivo nombre.
// Entradas: El nombre del profesor por eliminar.
// Salidas: Nada.
deleteProfessor = function(req,res)
{
	var name = req.param("nombre");
	db.profesores.remove({"nombre":name},false,function(err,deleted){
		res.send(200);
	});
};



// Actualiza los datos de un usuario.
// Entradas: El correo del usuario por actualizar y todos los datos que se actualizaran.
// Salidas: Nada.
updateUser = function(req,res)
{
	var email = req.param("correo");
	var username = req.param("username");
	var pass = req.param("password");
	var face = req.param("facebook");
	db.usuarios.update({"correo":email},{$set:{"correo":email,"username":username,"password":pass,"facebook":face}},{multi:true},function(err,changed){
		if(err || !changed){
			res.send(400);
		}
		else
		{
			res.json(changed);
		}
	});
};


// Actualiza los datos de un profesor.
// Entradas: El nuevo nombre del profesor.
// Salidas: Nada.
updateProfessor = function(req,res)
{
	var name = req.param("nombre");
	var newName = req.param("nuevo");
	db.profesores.update({"nombre":name},{$set:{"nombre":newName}},{multi:true},function(err,changed){
		if(err || !changed){
			res.send(400);
		}
		else
		{
			res.json(changed);
		}
	});
};


// Actualiza los datos de un curso.
// Entradas: El codigo, escuela, creditos y nombre del curso.
// Salidas: Nada.
updateCourse = function(req,res)
{
	var code = req.param("codigo");
	var school = req.param("escuela");
	var credits = req.param("creditos");
	var name = req.param("nombre");
	db.cursos.update({"codigo":code},{$set:{"nombre":name,"creditos":credits,"escuela":school}},{multi:true},function(err,changed){
		if(err || !changed){
			res.send(400);
		}
		else
		{
			res.json(changed);
		}
	});
};


// Actualiza los datos de un grupo.
// Entradas: El numero, profesor, sede y curso de un grupo.
// Salidas: Nada.
updateGroup = function(req,res)
{
	var code = req.param("curso");
	var professor = req.param("profesor");
	var number = req.param("numero");
	var sede = req.param("sede");
	db.grupos.update({"numero":number,"curso":code},{$set:{"profesor":professor,"sede":sede}},{multi:true},function(err,changed){
		if(err || !changed){
			res.send(400);
		}
		else
		{
			res.json(changed);
		}
	});
};


app.post('/update/user',updateUser);
app.post('/update/professor',updateProfessor);
app.post('/update/group',updateGroup);
app.post('/update/course',updateCourse);
app.post('/delete/professor',deleteProfessor);
app.post('/delete/user',deleteUser);
app.post('/delete/course',deleteCourse);
app.post('/delete/group',deleteGroup);
app.post('/professor',findProfessor);
app.post('/professors',findAllProfessors);
app.post('/group',findGroupByNumber); 
app.post('/groups',findAllGroups);
app.post('/groups/bycourse',findGroupsByCourse);
app.post('/course',findCourse);
app.post('/courses',findAllCourses);
app.post('/courses/school',findCoursesBySchool);
app.post('/user',findUser);
app.post('/users',findAllUsers);
app.post('/add/user',addUser);
app.post('/add/professor',addProfessor);
app.post('/add/course',addCourse);



server.listen(1212,function(){
	console.log("Node server running on localhost:1212");
});