conn = new Mongo("localhost");
db = conn.getDB("guiaDeHorariosPrueba");

// REINICIO DE COLLECIONES
db.usuarios.save({});
db.administradores.save({});
db.cursos.save({});
db.grupos.save({});
db.profesores.save({});

db.usuarios.remove({});
db.administradores.remove({});
db.cursos.remove({});
db.grupos.remove({});
db.profesores.remove({});




