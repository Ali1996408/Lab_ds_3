package com.han.gateway.faulttolerance;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FTDelayedCommand {
    private FTCommandState type;
    private UUID dataUid;
    private String username;

    public FTDelayedCommand(FTCommandState type, UUID dataUid, String username) {
        this.type = type;
        this.dataUid = dataUid;
        this.username = username;
    }
}
