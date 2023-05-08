package me.frankv.exchange.common.model;

import org.bson.types.ObjectId;

import java.io.Serializable;

public interface Member extends Serializable {

    ObjectId getId();
    void setId(ObjectId id);

}
