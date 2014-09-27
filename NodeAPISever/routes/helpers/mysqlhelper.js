var mysql = require('mysql');
var util = require('util');

var pool2 = mysql.createPool({
   connectionLimit : 20,
   host: 'localhost',
   user: 'kukkudu',
   password: 'Kukkudu123!',
   database: 'test'
});

var helper = {};

helper.executeQuery = function(query, cb){
   pool2.getConnection(function(err, connection){
    if(err) cb(err);
    else {
      connection.query(query, function(err, rows){
        if(err) cb(err);
        else cb(null, rows);
        connection.release();
      });
    }
  });
}

helper.updateQuery = function(query, params, cb){
  pool2.getConnection(function(err, connection){
    if(err) cb(err);
    else {
      connection.query(query, params, function(err, rows){
        if(err) cb(err);
        else cb(null, rows);
        connection.release();
      });
    }
  });
}

module.exports = helper;
