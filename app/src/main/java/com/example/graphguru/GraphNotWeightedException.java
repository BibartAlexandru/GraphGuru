package com.example.graphguru;

import androidx.annotation.Nullable;

public class GraphNotWeightedException extends Exception{
    @Nullable
    @Override
    public String getMessage() {
        return "EXCEPTION : Graph is not weighted" ;
    }
}
