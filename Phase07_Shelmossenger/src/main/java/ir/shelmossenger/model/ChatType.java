package ir.shelmossenger.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChatType {
    PV("PV"),
    GROUP("Group"),
    CHANNEL("Channel");

    private final String typeName;
}
