var MongoBaseDao = require("./mongoBaseDao.js");

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

UserDao.prototype.findOneWithPassword = function(userIdentity,callback){
    this.getCollection(function(collection){
        collection.findOne({'email':userIdentity}, {fields:{'name': 1, 'email':1, 'password':1}}, function(err, res) {
            callback && callback(err, res);
        });
    });
};

UserDao.prototype.findOneById = function(id,callback){
    this.getCollection(function(collection){
        collection.findOne({'email':userIdentity},{fields:{'password':0}}),function(err, res) {
            callback && callback(err, res);
        };
    });
};

exports = module.exports = UserDao;