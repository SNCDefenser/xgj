var express = require('express');
var router = express.Router();
var authJWT = require('./authJWT');
var UserDao = require("./dao/userDao.js");
var passport = require('passport');

router.use(function timeLog (req, res, next) {
    next()
});

router.post('/login', 
    function(req, res) {
        var userIdentity = req.body.userIdentity;
        var password = req.body.password;
        authJWT.signJWT(userIdentity,password, function(e, r){
            if(e){
                res.status(200).send({error:true, errorMsg:e});
                return;
            }
            res.status(200).send(r);
        })
});

router.post('/signUp', 
    function(req, res) {
        try{
            var email = req.body.email;
            var firstName = req.body.firstName;
            var lastName = req.body.lastName;
            var password = req.body.password;
           
            UserDao.signUp(email, password, firstName, lastName,function(e,r){
                if(e){
                    throw e;
                }
                authJWT.signJWT(email, password, function(e, r){
                    if(e){
                        res.status(200).send({error:true, errorMsg:e});
                        return;
                    }
                    res.status(200).send(r);
                })  
            });
        }catch(e){
            var error={msg:e.message,stack:e.stack};
            res.send(500,error);
        }  
});

router.post('/Authorize', 
function(req, res) {
    var token = req.body.token;
    var email = req.body.email;
    authJWT.verify(token,email, function(e, r){
        if(e){
            res.status(200).send({error:true, errorMsg:e});
            return;
        }
        res.status(200).send(r);
    })
});

router.get('/test', authJWT.authenticate(),
    function(req, res) {
        res.send(req.user.profile);
    }
);



module.exports = router;