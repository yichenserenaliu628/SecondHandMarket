package com.example.secondhandmarketwebapp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class DeleteImagesRequest {
    @NotNull
    List<String> uuids;

}
