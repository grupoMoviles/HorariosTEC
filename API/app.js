// Se definen todas las variables iniciales.
var connect = require('connect'),
	ruta_aplicacion = __dirname,
	express = require("express"),
	logfmt = require("logfmt"),
	app = express(),
	
	ruta_aplicacion = __dirname,
	path		= require('path'),
	http		= require('http'),
	server 		= http.createServer(app);
	
	
	guiaDeHorarios = 'mongodb://heroku_app24183267:ifjr7siso432arcdghdjco14v0@ds053438.mongolab.com:53438/heroku_app24183267',
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
var env = process.env.NODE_ENV || 'development';

if ('development' == env) {
   	app.use(logfmt.requestLogger());
	app.engine('html',cons.swig);
	app.set('view engine','html');
	app.set('views',__dirname + '/views');
	app.use(allowCrossDomain);
	app.use(require('body-parser')());
	app.use(require('method-override')());
	app.use(express.static(path.join(ruta_aplicacion+"/public")));
	app.use(require('errorhandler')());
	//app.use(app.router);
	
   
}


var mongo = require('mongodb');

var mongoUri = process.env.MONGOLAB_URI ||
  process.env.MONGOHQ_URL ||
  'mongodb://heroku_app24183267:ifjr7siso432arcdghdjco14v0@ds053438.mongolab.com:53438/heroku_app24183267';

// Inicia la aplicacion que contiene el API
app.get('/',function(req,res){
	res.send("API working on the GUIA DE HORARIOS mongo database.");	
});





// FUNCIONES QUE CONECTAN CON LA BASE DE DATOS

//obtiene los usuarios con cierto grupo
//entradas usuario, curso y numero
getUsersInGroup = function(req, res){
    var curso=req.param("codigo");
    var numero=req.param("numero");

    db.usuarios.find({"grupos":{"codigo":curso,"numero":numero}},function(err,usuario){
        if(err || !usuario){
            res.send(400);
        }
        else{
            res.json(usuario);
            }

        });
};


//obtiene los usuarios con cierto grupo
//entradas id facebook, curso y numero
getUsersInGroupFacebook = function(req, res){
    var curso=req.param("codigo");
    var numero=req.param("numero");

    db.usuarios.find({"grupos":{"codigo":curso,"numero":numero}},function(err,usuario){
        if(err || !usuario){
            res.send(400);
        }
        else{
            res.json(usuario);
            }

        });
};

//califica al un profesor
//entradas: nombre del profesor y la calificacion
rateProfessor = function(req,res)
{
    var pass=req.param("password");
    var email=req.param("correo");
    var nombre=req.param("nombre");
    var ratingP=req.param("calificacion");    
    var i,total,cant,rating;
    
    db.usuarios.find({"password":pass, "correo":email},function(err,usuario){
		if(!(usuario.length>0)){
			res.json("pff");
		}
		else
		{
            db.profesores.find({ "nombre":nombre},function(err,profe){
                if(err || !profe){
                    res.send(400);    
                }
                else{
                    for(i=0;i<profe.length;i++){
                        rating=parseFloat(ratingP);
                        total=parseFloat(profe[i].calificacionTotal);
                        cant=parseFloat(profe[i].cantidadCalificaciones);
                    }
                    db.profesores.update({"nombre":nombre},{$set:{"calificacionTotal":total+rating,                 "cantidadCalificaciones":cant+1}},function(err,profeR){
                        if(err || !profeR){
                            res.send(400);
                        }
                        else{
                            res.json(profeR);
                        }
                        
                    }
                               );
                }
            });
        }            
	});
};

//califica al un profesor
//entradas: nombre del profesor y la calificacion
rateProfessorFacebook = function(req,res)
{
    var face=req.param("facebookID");
    var nombre=req.param("nombre");
    var ratingP=req.param("calificacion");    
    var i,total,cant,rating;  
    
    db.usuarios.find({"facebook":face},function(err,usuario){
		if(!(usuario.length>0)){
			res.send(400);
		}
		else
		{
              db.profesores.find({ "nombre":nombre},function(err,profe){
                if(err || !profe){
                    res.send(400);    
                }
                else{
                    for(i=0;i<profe.length;i++){
                        rating=parseFloat(ratingP);
                        total=parseFloat(profe[i].calificacionTotal);
                        cant=parseFloat(profe[i].cantidadCalificaciones);
                    }
                    db.profesores.update({ "nombre":nombre },{ $set: { "calificacionTotal": total+rating,                 "cantidadCalificaciones":cant+1}},function(err,profeR){
                        if(err || !profeR){
                            res.send(400);
                        }
                        else{
                            res.json(profeR);
                        }
                        
                    }
                               );
                }
            });
        }            
	});
};


//retorna todos los cursos de un usuario
getCoursesByUser= function(req, res){
    var correo=req.param("correo");
    var password=req.param("password");
    var cursos;
    var cursosFinal = []; 
    var i, j, k;
    
    db.usuarios.find({"correo":correo,"password":password},function(err,usuario){
        if(err || !usuario){
            res.send(400);
        }
        else{

            for (j=0;j<usuario.length;j++){
                cursos=usuario[j].cursos;
                db.cursos.find({"codigo":{$in: cursos}},function(err,curso){
                        if(err || !curso){
                            res.json("pff");
                        }
                        else{
                            res.json(curso);
                        }
                               });

            }

        }
    });
}

//retorna todos los cursos de un usuario con facebook
getCoursesByUserFacebook= function(req, res){   
    var faceID= req.param("facebookID");
    
        db.usuarios.find({"facebook":faceID},function(err,usuario){
        if(err || !usuario){
            res.send(400);
        }
        else{
            var cursos;
            var cursosFinal = []; 
			var i, j;
            for (j=0;j<usuario.length;j++){
                cursos=usuario[j].cursos;
                db.cursos.find({"codigo":{$in: cursos}},function(err,curso){
                        if(err || !curso){
                            res.json("pff");
                        }
                        else{
                            res.json(curso);
                        }
                               });

            }
            
        }
    });
}

//obtiene todos los grupos de un usuario
getGroupsByUser = function(req, res){
    var correo=req.param("correo");
    var password=req.param("password");
    var grupos=[];
    
    db.usuarios.find({"correo":correo,"password":password},function(err,usuario){
        if(err || !usuario){
            res.send(400);
        }
        else{
            for (j=0;j<usuario.length;j++){
                grupos=usuario[j].grupos;
                 
            }
            res.json({"grupos":grupos}); 
        }
            });
            
};

//obtiene todos los grupos de un usuario
getGroupsByUserFacebook = function(req, res){
    var face= req.param("face");
    var grupos=[];
    db.usuarios.find({"facebook":face},function(err,usuario){
        if(err || !usuario){
            res.send(400);
        }
        else{
                        
            for (j=0;j<usuario.length;j++){
                grupos=usuario[j].grupos;
                
            }
            res.json({"grupos":grupos});  
        }
    });
}



//agregar id de facebook
//entradas, usuario, password, id de facebook
insertFacebook = function(req, res){
    var correo=req.param("correo");
    var password=req.param("password");
    var faceID= req.param("facebookID");
    
    db.usuarios.update({"correo":correo,"password":password},{$set:{"facebook":faceID}},function(err,usuario){
        if(err || !usuario){
            res.send(400);
        }
        else{
            res.send(usuario);
        }
    });
}

//inserta al usuario el curso seleccionado
//entradas: password del usuario, codigo del curso
insertGroup = function(req,res)
{
    var pass=req.param("password");
    var email=req.param("correo");
    var course=req.param("curso");
    var number=req.param("numero");    
    
    db.usuarios.find({ "correo":email,"password": pass, "cursos":course },function(err,usuarioGuardar){
        if(!(usuarioGuardar.length>0)){
            res.send(400);
        }
        else{
            db.usuarios.update({"correo":email,"password":pass},{$addToSet:{"grupos":{"codigo":course,"numero":number}}},function(err,usuario){
                if(err || !usuario){
                    res.send(400);
            }
                else{
                    res.json(usuario);
                }
            });    
        }
    });

};

        
//inserta al usuario el curso seleccionado
//entradas: password del usuario, codigo del curso
insertGroupFacebook = function(req,res)
{
    var face=req.param("facebookID");
    var course=req.param("curso");
    var number=req.param("numero");    
    
    db.usuarios.find({ "facebook":face, "cursos":course },function(err,usuarioGuardar){
        if(!(usuarioGuardar.length>0)){
            res.send(400);
        }
        else{
                db.usuarios.update({"facebook":face},{$addToSet:{"grupos":{"codigo":course,"numero":number}}},function(err,usuario){
                if(err || !usuario){
                    res.send(400);
            }
                else{
                    res.json(usuario);
                }
            });    
        }
    });
};

        
//inserta al usuario el curso seleccionado
//entradas: password del usuario, codigo del curso
insertCourse = function(req,res)
{
    var pass=req.param("password");
    var curso=req.param("codigo");   
    var email=req.param("correo");
    
    
    db.usuarios.update({ "correo":email,"password": pass },{$addToSet: { "cursos":curso  }},function(err,usuario){
                if(err || !usuario){
                    res.send(400);
            }
                else{
                    res.json(usuario);
                }
            });

};

//INSERTS CON FACEBOOK


        
//inserta al usuario el curso seleccionado
//entradas: password del usuario, codigo del curso
insertCourseFacebook = function(req,res)
{
    var face=req.param("facebookID");
    var curso=req.param("codigo");   
    
    
    db.usuarios.update({ "facebook":face },{$addToSet: { "cursos":curso  }},function(err,usuario){
                if(err || !usuario){
                    res.send(400);
            }
                else{
                    res.json(usuario);
                }
            });
};
     

//BORRAR GRUPO Y CURSO

//borra  al usuario el curso seleccionado
//entradas: password del usuario, codigo del curso
deleteGroupUser = function(req,res)
{
    var pass=req.param("password");
    var email=req.param("correo");
    var course=req.param("curso");
    var number=req.param("numero");    
    
    db.usuarios.update({ "correo":email,"password": pass },{$pull: { "grupos":{"codigo":course,"numero":number}  }},function(err,usuario){
                if(err || !usuario){
                    res.send(400);
            }
                else{
                    res.json(usuario);
                }
            });
};

//borra al usuario el curso seleccionado
//entradas: password del usuario, codigo del curso
deleteGroupFacebook = function(req,res)
{
    var face=req.param("facebookID");
    var course=req.param("curso");
    var number=req.param("numero");    
    
    db.usuarios.update({ "facebook":face},{$pull: { "grupos":{"codigo":course,"numero":number}   }},function(err,usuario){
                if(err || !usuario){
                    res.send(400);
            }
                else{
                    res.json(usuario);
                }
            });
};
     
//borra al usuario el curso seleccionado
//entradas: password del usuario, codigo del curso
deleteCourseUser = function(req,res)
{
    var pass=req.param("password");
    var curso=req.param("codigo");   
    var email=req.param("correo");
    
    
    db.usuarios.update({ "correo":email,"password": pass },{$pull: { "cursos":curso  }},function(err,usuario){
                if(err || !usuario){
                    res.send(400);
            }
                else{
                    res.json(usuario);
                }
            });

};



        
//borra al usuario el curso seleccionado
//entradas: password del usuario, codigo del curso
deleteCourseFacebook = function(req,res)
{
    var face=req.param("facebookID");
    var curso=req.param("codigo");   
    
    
    db.usuarios.update({ "facebook":face },{$pull: { "cursos":curso  }},function(err,usuario){
                if(err || !usuario){
                    res.send(400);
            }
                else{
                    res.json(usuario);
                }
            });
};


//LOGIN


//hace el login del administrador
//entrada passoword creado de la union del usuario con la contrase;a con md5
loginAdmin = function(req,res)
{
	var pass= req.param("password");
	db.administradores.find({"password":pass},function(err,usuario){
		if(err || !usuario){
			res.send(400);
		}
		else
		{
			res.json(usuario);
		}
	});
};

//hace el login del usuario
login = function(req,res)
{
	var pass= req.param("password");
    var email=req.param("correo");
	db.usuarios.find({"password":pass, "correo":email},function(err,usuario){
		if(err || !usuario){
			res.send(400);
		}
		else
		{
			res.json(usuario);
		}
	});
};
    
//hace el login del usuario con el id de faceboook
/*Q solo reciba el Facebookid y que si ese usuario no existe todavía entonces que lo cree y que si ya existe entonces no lo crea! Y q siempre me devuelve una colección usuario! Si el usuario ya existe me devuelve los datos de ese usuario y si no existe entonces me devuelve un usuario con cursos y grupos vacíos*/
loginFacebook = function(req,res)
{
	var face= req.param("facebookID");
	db.usuarios.find({"facebook":face},function(err,usuario){
		if(!(usuario.length>0)){
			     db.usuarios.save({"facebook":face,"grupos":[],"cursos":[]},function(err,saved){
                                    if(err || !saved)
                                        res.send(400);
                                    else
                                        res.json(saved);
                                });
		}
		else
		{
			res.json(usuario);
		}
	});
};


//GETS

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
};



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
			res.json(group[0]);
		}
	});
}



//ADDS



// Agregar un profesor al sistema.
// Entradas: Nombre del profesor.
// Salidas: El JSON del profesor.
addProfessor = function(req,res,next){

	var name = req.param("nombre");
    var pass= req.param("password");
	db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
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
    var pass= req.param("password");
	
    db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
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
    var pass= req.param("password");
	
    db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
				
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
		}
	});

};

//agrega un administrador al sistema
addAdmin = function(req,res,next){

	var username = req.param("username");
	var pass = req.param("password");
	
    db.administradores.save({"username":username,"password":pass},function(err,saved){
							if(err || !saved)
								res.send(400);
							else
								res.json(saved);
						});
					};



// Agrega un usuario al sistema.
// Entradas: Nombre, password, correo electronico y facebook del usuario.
// Salidas: JSON del usuario creado.
addUser = function(req,res,next){

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
                        db.usuarios.save({"correo":email,"password":pass,"facebook":face,"grupos":[],"cursos":[]},function(err,saved){
                                    if(err || !saved)
                                        res.send(400);
                                    else
                                        res.json(saved);
                                });

                }
            });
	
            
};




//DELETES



// Elimina grupo introduciendo curso y numero de grupo.
// Entradas: El codigo del curso y el numero del grupo.
// Salidas: Nada.
deleteGroup = function(req,res)
{
	var course = req.param("curso");
	var number = req.param("numero");
    var pass= req.param("password");
	
    db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
				db.grupos.remove({"curso":course,"numero":number},false,function(err,deleted){
		          res.send(200);
	});
		}
	});

};


// Elimina el curso introduciendo su codigo.
// Entradas: El codigo del curso.
// Salidas: Nada.
deleteCourse = function(req,res)
{
	var code = req.param("codigo");
    var pass= req.param("password");
	
    db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
			db.cursos.remove({"codigo":code},false,function(err,deleted){
                res.send(200);
            });
		}
	});
            
};


// Elimina un usuario al introducir su correo electronico.
// Entradas: El correo del usuario por eliminar.
// Salidas: Nada.
deleteUser = function(req,res)
{
	var email = req.param("correo");
    var pass= req.param("password");
	
    db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
			db.usuarios.remove({"correo":email},false,function(err,deleted){
                res.send(200);
            });
		}
	});
            
};


// Elimina un profesor introduciendo su respectivo nombre.
// Entradas: El nombre del profesor por eliminar.
// Salidas: Nada.
deleteProfessor = function(req,res)
{
	var name = req.param("nombre");
    var pass= req.param("password");
	
    db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
				db.profesores.remove({"nombre":name},false,function(err,deleted){
		      res.send(200);
	       });
		}
	});

    
};

//UPDATES

// Actualiza los datos de un usuario.
// Entradas: El correo del usuario por actualizar y todos los datos que se actualizaran.
// Salidas: Nada.
updateUser = function(req,res)
{
	var email = req.param("correo");
	var pass = req.param("password");
	var face = req.param("facebook");
    
    var passADM= req.param("passwordADM");
	
    db.administradores.find({"password":passADM},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
			db.usuarios.update({"correo":email},{$set:{"correo":email,"password":pass,"facebook":face}},{multi:true},function(err,changed){
                if(err || !changed){
                    res.send(400);
                }
                else
                {
                    res.json(changed);
                }
            });
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
    var pass= req.param("password");
    
	
    db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
			db.profesores.update({"nombre":name},{$set:{"nombre":newName}},{multi:true},function(err,changed){
                if(err || !changed){
                    res.send(400);
                }
                else
                {
                    res.json(changed);
                }
            });
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
    var pass= req.param("password");
	
    db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
			db.cursos.update({"codigo":code},{$set:{"nombre":name,"creditos":credits,"escuela":school}},{multi:true},function(err,changed){
                if(err || !changed){
                    res.send(400);
                }
                else
                {
                    res.json(changed);
                }
            });
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
    var pass= req.param("password");
	
    db.administradores.find({"password":pass},function(err,usuario){
		if(!(usuario.length > 0)){
			res.send(400);
		}
		else
		{
			db.grupos.update({"numero":number,"curso":code},{$set:{"profesor":professor,"sede":sede}},{multi:true},function(err,changed){
                if(err || !changed){
                    res.send(400);
                }
                else
                {
                    res.json(changed);
                }
            });
		}
	});
            
};

//login
app.post('/login/administrador',loginAdmin);
app.post('/login/usuario',login);
app.post('/login/usuario/facebook',loginFacebook);

//insertar grupos y cursos
app.post('/insert/group',insertGroup);
app.post('/insert/course',insertCourse);
app.post('/insert/group/facebook',insertGroupFacebook);
app.post('/insert/course/facebook',insertCourseFacebook);

//borrar grupos y cursos
app.post('/delete/group/user',deleteGroupUser);
app.post('/delete/course/user',deleteCourseUser);
app.post('/delete/group/user/facebook',deleteGroupFacebook);
app.post('/delete/course/user/facebook',deleteCourseFacebook);

//udate
app.post('/update/user',updateUser);
app.post('/update/professor',updateProfessor);
app.post('/update/group',updateGroup);
app.post('/update/course',updateCourse);
app.post('/insert/facebook',insertFacebook);
app.post('/rate/professor',rateProfessor);
app.post('/rate/professor/facebook',rateProfessorFacebook);


//delete
app.delete('/delete/professor',deleteProfessor);
app.delete('/delete/user',deleteUser);
app.delete('/delete/course',deleteCourse);
app.delete('/delete/group',deleteGroup);

//get
app.post('/courses/user/facebook',getCoursesByUserFacebook);
app.post('/courses/user',getCoursesByUser);
app.post('/groups/user',getGroupsByUser);
app.post('/groups/user/facebook',getGroupsByUserFacebook);

app.post('/users/group',getUsersInGroup);
app.post('/users/group/facebook',getUsersInGroupFacebook);

app.get('/professor',findProfessor);
app.get('/professors',findAllProfessors);
app.post('/group',findGroupByNumber); 
app.post('/groups',findAllGroups);
app.get('/groups/bycourse',findGroupsByCourse);
app.get('/course',findCourse);
app.get('/courses',findAllCourses);
app.get('/courses/school',findCoursesBySchool);
app.get('/user',findUser);
app.get('/users',findAllUsers);

//add
app.post('/add/user',addUser);
app.post('/add/professor',addProfessor);
app.post('/add/course',addCourse);
app.post('/add/admin',addAdmin);



server.listen((process.env.PORT || 5000),function(){
	console.log("Node server running");
});
