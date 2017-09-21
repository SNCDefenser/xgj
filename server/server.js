var express = require('express');
var app = express();
var port = process.env.PORT || 3000;

// parse application/x-www-form-urlencoded
app.use(require('body-parser').urlencoded({ extended: false }))
// parse application/json
app.use(require('body-parser').json())

//initialize authorization module

var authJWT = require('./authJWT');
authJWT.init(app);

//initalize router module
var router = require('./router');
app.use('/', router);

app.use('/public', express.static('public'))

app.listen(port, function () {
  console.log('Surprise listening on port 3000!')
})

