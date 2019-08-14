package com.arch.mvc.repositories;

import com.arch.mvc.constants.CollectionConstants;
import com.arch.mvc.utils.MongoImpl;
import org.bson.Document;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends MongoImpl {

    public boolean insertUser(String key, Document updateDoc) throws Exception {
        return super.insertSingleDocumentIfNotExist(CollectionConstants.USERS, key, updateDoc);
    }

}
