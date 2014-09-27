var MysqlHelper = require('./helpers/mysqlhelper');
var util = require('util');

var api = {}
api.getNewsForCategory = function(req, res){

   var limit = parseInt(req.query.limit); 
   var catId = parseInt(req.query.category_id); 
   var subCatId = parseInt(req.query.sub_category_id); 
   
   var query;
   if(subCatId != undefined && subCatId.toString() != "NaN")
     query = "SELECT * FROM `news` WHERE sub_category_id = "+subCatId
              +" AND pub_date >= DATE_SUB(NOW(),INTERVAL 48 HOUR) ";
   
   else if(catId != undefined && catId.toString() != "NaN")
     query = "SELECT * FROM `news` WHERE category_id = "+catId
              +" AND pub_date >= DATE_SUB(NOW(),INTERVAL 48 HOUR) ";
   
   else {
      util.log("No valid parameter");
      res.send(422, {error :"No valid params avialable"});
   }  


   if(limit != undefined && limit.toString() != "NaN")
      query +=" limit "+limit;
   else
      query +=" limit 100";

   MysqlHelper.executeQuery(query, function(err, news) {
      if(err) res.send(502, {error : err});
      else {
         res.send(200, {news : news})
      }
   });    
}

api.getNews =  function(req, res){

   var limit = parseInt(req.query.limit); 
   
   var query = "SELECT * FROM `news` WHERE source = 'top_stories' "
              +" AND pub_date >= DATE_SUB(NOW(),INTERVAL 48 HOUR) ";

   if(limit != undefined && limit.toString() != "NaN")
      query +=" limit "+limit;
   else
      query +=" limit 100";
   
   MysqlHelper.executeQuery(query, function(err, news) {
      if(err) res.send(502, {error : "error in getting news  "+err});
      else {
         res.send(200, {news : news})
      }
   });
}
module.exports = api;
