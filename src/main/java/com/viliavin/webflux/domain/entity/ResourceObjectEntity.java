package com.viliavin.webflux.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResourceObjectEntity {

    @MongoId
    @Id
    private Integer id;
    private String value;
    private String path;


}
