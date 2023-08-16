package com.viliavin.webflux.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceObject {

    private int id;
    private String value;
    private String path;

}
