package com.example.api.helper.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Error {
    private String message;
    private List<Locations> locations;
    private List<String> path;
    private Extensions extensions;
}
