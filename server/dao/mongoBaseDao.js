var MongoClient = require('mongodb').MongoClient;

var MongoBaseDao = function(){
    this.db = null;
    console.log('MongoBaseDao');
}

var ObjectID = require('mongodb').ObjectID;

MongoBaseDao.prototype.getConnection = function(callback){
    var _ = this;
    if(_.db == null){
        MongoClient.connect("mongodb://localhost:27017/xgj", {  
            poolSize: 10
        },function(err, db) {
            if(!err) {
            console.log("Mongodb connected.");
            }
            
            _.db = db;
            callback && callback(_.db);
        });
    }else{
        callback && callback(_.db);
    }
}

MongoBaseDao.prototype.parseSort = function(sort){
    if(!sort)return {};
    if(typeof sort == "string"){
        sort = [sort];
    }
    var r = {};
    for(var i=0;i<sort.length;i++){
        var s = sort[i].split('_');
        if(s.length!=2){
            continue;
        }
        var f = s[0];
        var o = s[1].toLowerCase();
        if(o !='asc' && o!='desc'){
            continue;
        }
        r[f] = o=='asc'?1:-1;
    }
    return r;
}

MongoBaseDao.prototype.parseField = function(field){
    if(!field)return {};
    if(typeof field != "string"){
        return field;
    }
    var fs = field.split(',');
    var r = {};
    for(var i=0;i<fs.length;i++){
        r[fs[i].trim()] = true;
    }
    return r;
}

MongoBaseDao.prototype.parseQuery = function(query){
    if(!query)return {};
    if(query._id){
        query._id = new ObjectID(query._id);
    }
    return query;
}

MongoBaseDao.prototype.findByPage = function(collectionName, query, fields, sort, page, pageSize, callback){
    var _ = this;
    query = _.parseQuery(query);
    fields = _.parseField(fields);
    sort =  _.parseSort(sort);
    var pageSize = pageSize<3? 3: pageSize;
    var page = page<1? 1: page;
    var skip = (page-1)*pageSize;
    _.getConnection(function(db){
        var collection = db.collection(collectionName);

        collection.find(query, fields).sort(sort).skip(skip).limit(pageSize).toArray(function(error, result) {
            if(error){
                callback && callback(error,null); 
            }
            collection.count(query, function(err, count) {
                if(error){
                    callback && callback(error,null); 
                }
                var r = {
                    page:page,
                    pageSize:pageSize,
                    totalCount : count,
                    totalPage:Math.ceil(count/pageSize),
                    items:result
                }
                result = r;
                
                callback && callback(error,result); 
            });
        });

    })
}

MongoBaseDao.prototype.findOneById = function(collectionName, id, fields, callback){
    var _ = this;
    _.getConnection(function(db){
        var collection = db.collection(collectionName);
        collection.findOne({'_id': new ObjectID(id)}, _.parseField(fields), function(e, r) {
            callback && callback(e,r);
        });
    });
};

MongoBaseDao.prototype.save = function(collectionName, doc, callback){
    var _ = this;
    doc.createdTime = new Date();
    doc.lastModifiedTime = new Date();
    _.getConnection(function(db){
        var collection = db.collection(collectionName);
        collection.insertOne(doc, function(e, r){
            callback && callback(e, r);
        });
    });
}

MongoBaseDao.prototype.update = function(collectionName, id, newValues, callback){
    var _ = this;
    doc.lastModifiedTime = new Date();
    _.getConnection(function(db){
        var collection = db.collection(collectionName);
        collection.updateOne({'_id': new ObjectID(id)}, {$set:newValues}, function(e, r){
            callback && callback(e, r);
        });
    });
}

MongoBaseDao.prototype.delete = function(collectionName, id, callback){
    var _ = this;
    _.getConnection(function(db){
        var collection = db.collection(collectionName);
        collection.deleteOne({'_id': new ObjectID(id)}, function(e, r){
            callback && callback(e, r);
        });
    });
}

module.exports = new MongoBaseDao();

exports = module.exports;
