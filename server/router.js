var express = require('express');
var router = express.Router();
var auth = require('./auth');

router.use(function timeLog (req, res, next) {
    next()
});

router.get("/",auth.ensureLoggedIn(),
    function(req,res){
        res.status(200).send("Hey!!!");
});

router.get("/login",
function(req,res){
    res.status(200).send("login!!!");
});

router.post('/login', 
    auth.authenticate('local', { failureRedirect: '/login' }),
    function(req, res) {
        res.redirect('/');
});

module.exports = router;