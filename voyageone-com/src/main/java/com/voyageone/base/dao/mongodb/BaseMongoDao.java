package com.voyageone.base.dao.mongodb;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class BaseMongoDao {

    protected BaseJomgoTemplate mongoTemplate;

    protected String collectionName;

    protected Class entityClass;

    @Autowired
    public void setMongoTemplate(BaseJomgoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        if (this.collectionName == null) {
            this.entityClass = getEntityClass();
            this.collectionName = mongoTemplate.getCollectionName(entityClass);
        }
    }

    public abstract Class getEntityClass();

    public DBCollection getDBCollection() {
        return mongoTemplate.getDBCollection(collectionName);
    }

    public DBCollection getDBCollection(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.getDBCollection(collectionName);
    }

    public <T> T selectOne() {
        return mongoTemplate.findOne((Class<T>) entityClass, collectionName);
    }

    public <T> T selectOneWithQuery(String strQuery) {
        return mongoTemplate.findOne(strQuery, (Class<T>)entityClass, collectionName);
    }

    public <T> List<T> selectAll() {
        return mongoTemplate.findAll((Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursorAll() {
        return mongoTemplate.findCursorAll((Class<T>) entityClass, collectionName);
    }

    public <T> List<T> select(final String strQuery) {
        return mongoTemplate.find(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    public <T> List<T> selectWithProjection(final String strQuery, String projection) {
        return mongoTemplate.find(strQuery, projection, (Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursor(final String strQuery) {
        return mongoTemplate.findCursor(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    public <T> T selectById(String id) {
        return mongoTemplate.findById(id, (Class<T>) entityClass, collectionName);
    }

    public <T> T selectOne(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne((Class<T>) entityClass, collectionName);
    }

    public <T> T selectOneWithQuery(String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findOne(strQuery, (Class < T >)entityClass, collectionName);
    }

    public <T> List<T> selectAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findAll((Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursorAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursorAll((Class<T>) entityClass, collectionName);
    }

    public <T> List<T> select(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    public <T> List<T> selectWithProjection(final String strQuery, String projection, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.find(strQuery, projection, (Class<T>) entityClass, collectionName);
    }

    public <T> Iterator<T> selectCursor(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findCursor(strQuery, null, (Class<T>) entityClass, collectionName);
    }

    public <T> T selectById(String id, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.findById(id, (Class<T>) entityClass, collectionName);
    }

    public long count() {
        return mongoTemplate.count(collectionName);
    }

    public long countByQuery(final String strQuery) {
        return mongoTemplate.count(strQuery, collectionName);
    }

    public long count(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.count(collectionName);
    }

    public long countByQuery(final String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.count(strQuery, collectionName);
    }

    public WriteResult insert(BaseMongoModel model) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
        return mongoTemplate.insert(model, collectionName);
    }

    public WriteResult insertWithList(Collection<? extends Object> models) {
        if (models != null && models.size() > 0) {
            BaseMongoModel model = (BaseMongoModel)models.iterator().next();
            String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
            return mongoTemplate.insert(models, collectionName);
        }
        return null;
    }

    public WriteResult update(BaseMongoModel model) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
        model.setModified(DateTimeUtil.getNowTimeStamp());
        return mongoTemplate.save(model, collectionName);
    }

    public WriteResult delete(BaseMongoModel model) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, model);
        return mongoTemplate.removeById(model.get_id(), collectionName);
    }

    public WriteResult deleteById(String id) {
        return mongoTemplate.removeById(id, collectionName);
    }

    public CommandResult deleteAll() {
        String commandStr = String.format("{ delete:\"%s\", deletes:[ { q: { }, limit: 0 } ] }", collectionName);
        return mongoTemplate.executeCommand(commandStr);
    }

    public WriteResult deleteWithQuery(String strQuery) {
        return mongoTemplate.remove(strQuery, collectionName);
    }

    public WriteResult deleteById(String id, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.removeById(id, collectionName);
    }

    public CommandResult deleteAll(String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        String commandStr = String.format("{ delete:\"%s\", deletes:[ { q: { }, limit: 0 } ] }", collectionName);
        return mongoTemplate.executeCommand(commandStr);
    }

    public WriteResult deleteWithQuery(String strQuery, String channelId) {
        String collectionName = mongoTemplate.getCollectionName(this.collectionName, channelId);
        return mongoTemplate.remove(strQuery, collectionName);
    }

    public CommandResult executeCommand(String jsonCommand) {
        return mongoTemplate.executeCommand(jsonCommand);
    }

}