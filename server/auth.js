var ensureLoggedIn = require('connect-ensure-login').ensureLoggedIn();

// Configuration for passport-local module
var passport = require('passport')
var LocalStrategy = require('passport-local').Strategy;

//bcrypt for passwaord encryption
//var bcrypt = require('bcrypt');

// load up the models we need: CustomerDao model.
var UserDao = require("./dao/userDao.js");

// create a new instance
var User = new UserDao();

// passport session setup
//  required for persistent login sessions
// passport needs ability to serialize and unserialize users
// out of session.

// local login
// we are using named strategies since we have one for login and one for signup
// by default, if there was no name, it would just be called "local"
var Authorize = function(){
  // by default, usernameField is username, here we change parameters - usernameField
  //  is userIdentity. It could be one of username, email, telephone.
  passport.use("local",new LocalStrategy({
      usernameField: 'userIdentity',
      passwordField: 'password',
    },
    function(userIdentity, password, done) {
        User.findOneWithPassword(userIdentity,function(err, user){
          if (err) { return done(err); }
          if (!user) {
              return done(null, false, { message: 'Incorrect userIdentity.' });
          }
          //var match =bcrypt.compareSync(password, user.password); // true 
          //if (!match) {
              //return done(null, false, { message: 'Incorrect password.' });
          //}
          if(password!=user.password){
            return done(null, false, { message: 'Incorrect password.' });
          }
          return done(null, user);
      });
    }
  ));

// used to serialize the user for the session.
  passport.serializeUser(function(user, cb) {
    var key = user.id;
    cb(null, key);
  });

// used to deserialize the user
  passport.deserializeUser(function(key, cb) {
    User.findOneById(key.id, function (err, user) {
        if (err) { return cb(err); }
        cb(null, user);
      });
  });
  console.log("Authorize init.");
}
var bodyParser = require('body-parser');
Authorize.prototype.init = function(app){
  // parse application/x-www-form-urlencoded
  app.use(bodyParser.urlencoded({ extended: false }))
  // parse application/json
  app.use(bodyParser.json())
  app.use(require('express-session')({ secret: 'xgjlalala', resave: false, saveUninitialized: false }));
  app.use(passport.initialize());
  app.use(passport.session());
};
Authorize.prototype.ensureLoggedIn = function(options){
  if (typeof options == 'string') {
    options = { redirectTo: options }
  }
  options = options || {};
  
  var url = options.redirectTo || '/login';
  var setReturnTo = (options.setReturnTo === undefined) ? true : options.setReturnTo;
  
  return function(req, res, next) {
    if (!req.isAuthenticated || !req.isAuthenticated()||req.user.type!=options.type) {
      if (setReturnTo && req.session) {
        req.session.returnTo = req.originalUrl || req.url;
      }
      return res.redirect(url);
    }
    next();
  }
};

Authorize.prototype.authenticate = function(type, opt){
  return passport.authenticate(type, opt);
};

Authorize.prototype.generateHash = function(password,callback){
  callback && callback(null,password);   
  // bcrypt.hash(password, 10, function(err, hash) {
  //   callback && callback(err,hash);   
  // });
}

// expose this function to our app using module.exports
module.exports = new Authorize();
