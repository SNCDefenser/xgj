var MongoBaseDao = require("./mongoBaseDao.js");
var extend = require('util')._extend

var ItemInfoModel = {
    _id:null,
    owner:{},
    name:"default",
    type:0,// 0 - shopping list, 1 - stock, 2 - service
    tags:null,
    places:null,
    description:"",
    actionDate:null,
    provider:null,
    createdTime:null,
    lastModifiedTime:null
}

var ItemDao = function(){
    this.collectionName = 'ItemInfo';
}

ItemDao.prototype.getCollection = function(callback){
    var _ = this;
    MongoBaseDao.getConnection(function(db){
      _.collection = db.collection(_.collectionName);
      callback && callback(_.collection );
    }) 
}

ItemDao.prototype.findByPage = function(query, fields, sort, page, pageSize, callback){
    var _ = this;

    MongoBaseDao.findByPage(_.collectionName, query, fields, sort, page, pageSize, function(e, r){
        callback && callback(e, r);
    })
}

ItemDao.prototype.findOneById = function(id, fields, callback){
    var _ = this;
    MongoBaseDao.findOneById(_.collectionName, id, fields, function(e, r){
        callback && callback(e, r);
    })
};

ItemDao.prototype.findOneByOwner = function(owner,callback){
    this.getCollection(function(collection){
        collection.findOne({'owner':owner},function(e, r) {
            callback && callback(e, r);
        });
    });
};

ItemDao.prototype.insert = function(owner, name, tags, places, type, callback){
    
    var item = {
        owner: owner,
        name: name,
        tags: tags,
        places: places,
        type: type
    }
    
    var _ = this;
    MongoBaseDao.save(_.collectionName, item, function(e, r){
        callback && callback(null, {item: item});
    })
}

ItemDao.prototype.update = function(id, newValues, callback){
    var _ = this;
    MongoBaseDao.update(_.collectionName, id, newValues, function(e, r){
        callback && callback(e, r);
    })
}

ItemDao.prototype.delete = function(id, callback){
    var _ = this;
    MongoBaseDao.delete(_.collectionName, id, function(e, r){
        callback && callback(e, r);
    })
}

exports = module.exports = new ItemDao();