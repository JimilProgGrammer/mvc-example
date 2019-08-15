package com.arch.mvc.repositories;

import com.arch.mvc.constants.CollectionConstants;
import com.arch.mvc.utils.MongoImpl;
import org.bson.Document;
import org.springframework.stereotype.Repository;

/**
 * Repository class for CRUD operations
 * on the users collection.
 *
 * @author jimil
 */
@Repository
public class UserRepository extends MongoImpl {

    /**
     * Inserts a new user into the database provided
     * he/she doesn't already exist.
     *
     * @param queryDoc
     * @param updateDoc
     * @return
     * @throws Exception
     */
    public void insertNewUser(Document queryDoc, Document updateDoc) throws Exception {
        super.updateRecords(queryDoc, updateDoc, CollectionConstants.USERS);
    }

    /**
     * Updates an existing user record
     *
     * @param queryDoc
     * @param updateDoc
     * @throws Exception
     */
    public void updateUser(Document queryDoc, Document updateDoc) throws Exception {
        super.updateRecords(queryDoc, updateDoc, CollectionConstants.USERS);
    }

    /**
     * Given an auth token, this method finds a corressponding
     * user from the USERS collection.
     *
     * @param queryDoc
     * @return
     * @throws Exception
     */
    public Document findUserByToken(Document queryDoc) throws Exception {
        return super.findOneRecord(CollectionConstants.USERS, queryDoc);
    }

    /**
     * Given a username, this method finds a corressponding
     * user from the USERS collection.
     *
     * @param queryDoc
     * @return
     * @throws Exception
     */
    public Document findUserByUsername(Document queryDoc) throws Exception {
        return super.findOneRecord(CollectionConstants.USERS, queryDoc);
    }

}
