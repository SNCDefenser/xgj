
var passport = require('passport');
var JwtStrategy = require('passport-jwt').Strategy;
var ExtractJwt = require('passport-jwt').ExtractJwt;
var jwt = require('jsonwebtoken');
var UserDao = require("./dao/userDao.js");

var AuthorizeJWT = function(){
    this.secretOrKey = 'xgjlalala';
    this.issuer = 'xgj.com';
    this.audience = 'app.xgj.com'; 
}

AuthorizeJWT.prototype.init = function(app){

    app.use(passport.initialize());

    var _ = this
   
    passport.use(new JwtStrategy({
        jwtFromRequest: ExtractJwt.fromHeader("authorization"),
        secretOrKey: _.secretOrKey,
        issuer:_.issuer,
        audience: _.audience
    }, function(jwt_payload, done) {
        UserDao.findOneWithPassword(jwt_payload.sub, function(err, user) {
            if (err) {
                return done(err, false);
            }
            if (user) {
                return done(null, user);
            } else {
                return done(null, false);
                // or you could create a new account
            }
        });
    }));
};

AuthorizeJWT.prototype.authenticate = function(){
    return passport.authenticate('jwt', { session: false })
};

var signToken = function(auth, email, name, callback){
    user = {
        email : email,
        name: name
    };
    jwt.sign({ 
        sub: user.email,
        user: user
    }, 
    auth.secretOrKey,
    {   
        expiresIn: 60 * 60 * 24 * 365,
        audience: auth.audience, 
        issuer: auth.issuer
    },function(e, token){
        if (e) { return callback(e); }
        return callback(null, {token: token, user:user});
    });

}

AuthorizeJWT.prototype.signJWT = function(userIdentity, password, callback){
    var _ = this;
    UserDao.findOneWithPassword(userIdentity,function(e, user){
        if (e) { return callback(e); }
        if (!user) {
            return callback({ message: 'Incorrect userIdentity.' });
        }
        if(password!=user.password){
          return callback({ message: 'Incorrect password.' });
        }
        signToken(_, user.email, user.firstName, callback);
    });
};

AuthorizeJWT.prototype.verify = function(token, email, callback){
    // verify a token symmetric
    var _ = this
    jwt.verify(token, _.secretOrKey, { audience: _.audience, issuer: _.issuer}, function(err, decoded) {
        if(err){
            return callback(err);
        }

        if(email!=decoded.user.email){
            return callback({ message: 'Unauthorized' });
        }
        UserDao.findOneWithPassword(decoded.user.email,function(e, user){
            if (e) { return callback(e); }
            if (!user) {
                return callback({ message: 'Unauthorized' });
            }
            signToken(_, user.email, user.firstName, callback);
        
        });
    });
}

module.exports = new AuthorizeJWT();