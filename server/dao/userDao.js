var MongoBaseDao = require("./mongoBaseDao.js");

var UserInfoModel = {
    _id:null,
    owner:{},
    name:"default",
    type:0,// 0 - shopping list, 1 - stock, 2 - service
    tags:null,
    description:"",
    actionDate:null,
    provider:null,
    createdTime:null,
    lastModifiedTime:null
}

var UserDao = function(){
    this.collectionName = 'UserInfo';
}

UserDao.prototype.getCollection = function(callback){
    var _ = this;
    MongoBaseDao.getConnection(function(db){
      _.collection = db.collection(_.collectionName);
      callback && callback(_.collection );
    }) 
}

UserDao.prototype.findOneWithPassword = function(email,callback){
    this.getCollection(function(collection){
        collection.findOne({'email':email}, {fields:{'email':1, 'firstName':1, 'password':1}}, function(e, r) {
            callback && callback(e, r);
        });
    });
};

UserDao.prototype.findOneByEmail = function(email,callback){
    this.getCollection(function(collection){
        collection.findOne({'email':email},{fields:{'password':0}},function(err, res) {
            callback && callback(err, res);
        });
    });
};

UserDao.prototype.signUp = function(email, password,firstName, lastName, callback){
    var doc = {
        email:email,
        password:password,
        firstName:firstName,
        lastName:lastName
    }
    var _ = this;
    MongoBaseDao.save(_.collectionName, doc, function(e, r){
        callback && callback(e, r);
    })
}

exports = module.exports = new UserDao();