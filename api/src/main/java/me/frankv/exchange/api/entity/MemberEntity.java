package me.frankv.exchange.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@Entity
@Table(
        name = "member"
)
public class MemberEntity implements Serializable {
    @Id
    private ObjectId id;

}
