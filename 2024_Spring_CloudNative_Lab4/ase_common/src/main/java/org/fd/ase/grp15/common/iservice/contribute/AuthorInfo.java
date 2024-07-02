package org.fd.ase.grp15.common.iservice.contribute;

import lombok.Getter;

import java.io.Serializable;

public class AuthorInfo implements Serializable {
    @Getter
    private String name;
    private String institutionName;
    private String area;
    private String email;

}