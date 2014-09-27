var express = require('express');
var api = require('./routes/api');
var util = require('util');
var logger = function(req, res, next){
console.log("starting : ", req.path, req.query);
next();
};

var app = express();
app.configure(function () {  
  app.use(logger);
	app.use(express.logger('default'));     /* 'default', 'short', 'tiny', 'dev' */	
  app.use(express.compress());
	app.use(express.json());
	app.use(express.urlencoded());
	app.use(app.router);
});

app.get('/trends/news', api.getNewsForCategory);
app.get('/trends/news/top_stories', api.getNews);

app.listen(7777);

console.log('started listening at 7777');

