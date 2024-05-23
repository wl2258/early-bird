package com.ssonzm.coremodule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImageFile {

    @Column(length = 100)
    private String storeFileName;

    @Column(length = 250)
    private String storeFileUrl;
}
